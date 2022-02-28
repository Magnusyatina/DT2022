package org.magnusario.tasks

import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import org.magnusario.listeners.EventPublisher

import java.util.stream.Collectors

class ImageScrapperTask extends Task<Page> {

    Page page

    static String imgRegEx = "img[src~=(?i)\\.(png|jpe?g|gif|svg)]";

    ImageScrapperTask(EventPublisher eventListener, Page page) {
        super(eventListener)
        this.page = page
    }

    @Override
    protected void compute() {
        Elements elemImages = page.document.select(imgRegEx)
        if (elemImages.isEmpty())
            return
        def imageUrls = elemImages.stream()
                .map(elem -> elem.attr("src"))
                .map(elem -> !elem.startsWith("/") ? elem : page.url + elem)
                .filter(elem -> elem.startsWith("http://") || elem.startsWith("https://"))
                .collect(Collectors.toList())
        eventPublisher.publish(new Images(imageUrls))
    }
}
