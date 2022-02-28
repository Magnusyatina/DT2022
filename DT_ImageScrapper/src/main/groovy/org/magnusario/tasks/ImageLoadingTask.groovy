package org.magnusario.tasks

import org.jsoup.Connection
import org.jsoup.Jsoup
import org.magnusario.listeners.EventPublisher

import java.util.concurrent.ConcurrentSkipListSet
import java.util.stream.Collectors

class ImageLoadingTask extends Task<Images> {

    Images images

    private static ConcurrentSkipListSet<String> uploadedImages = new ConcurrentSkipListSet<>()

    ImageLoadingTask(EventPublisher eventListener, Images images) {
        super(eventListener)
        this.images = images
    }

    @Override
    protected void compute() {
        if (images.imageUrls.isEmpty())
            return
        if (defaultProperties.maxUploadedImagesInSingleThread as Integer >= images.imageUrls.size()) {
            images.imageUrls.stream()
                    .map(imageUrl -> new Tuple2(imageUrl, getImageName(imageUrl)))
                    .filter(tuple -> !uploadedImages.contains(tuple.v2))
                    .forEach(tuple -> uploadImage(tuple.v1 as String, tuple.v2 as String))
        } else {
            List<ImageLoadingTask> separatedTasks = separateTask(images)
            separatedTasks.forEach(task -> task.fork())
        }
    }

    void uploadImage(String imageUrl, String imageName) {
        try {
            Connection.Response response = Jsoup.connect(imageUrl).ignoreContentType(true).execute()
            byte[] bytes = response.bodyAsBytes()
            if (bytes.length > defaultProperties.maximumImageBytes as Integer) {
                println "Картинка $imageName не будет загружена. Размер превышает допустимый максимум"
                return
            }
            println "Загрузка картинки $imageName"
            try (FileOutputStream out = new FileOutputStream(defaultProperties.outputDir + imageName)) {
                out.write(bytes)
                uploadedImages.add(imageName)
            }
        } catch (Exception exception) {
            println exception.getMessage()
        }
    }

    String getImageName(String imageUrl) {
        String imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1)
        def index = imageName.indexOf("?")
        if (index != -1)
            imageName = imageName.substring(0, index)
        imageName = imageName.replaceAll("[\\\\/]", "_")
        return imageName
    }

    List<ImageLoadingTask> separateTask(Images images) {
        List<ImageLoadingTask> tasks = new LinkedList<>()
        Images currentImages = new Images()
        for (int i = 0; i < images.imageUrls.size(); i++) {
            if (currentImages.imageUrls.size() >= defaultProperties.maxUploadedImagesInSingleThread) {
                tasks.add(new ImageLoadingTask(eventPublisher, currentImages))
                currentImages = new Images()
            }
            currentImages.imageUrls.add(images.imageUrls[i])
        }
        tasks.add(new ImageLoadingTask(eventPublisher, currentImages))
        return tasks
    }
}
