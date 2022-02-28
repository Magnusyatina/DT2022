package org.magnusario.tasks

import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.magnusario.listeners.EventPublisher

import java.util.concurrent.ConcurrentSkipListSet

class ContentReceiverTask extends Task<String> {

    private static ConcurrentSkipListSet<String> visitedLinks = new ConcurrentSkipListSet<>()

    String url

    ContentReceiverTask(EventPublisher eventListener, String url) {
        super(eventListener)
        this.url = url
    }

    @Override
    protected void compute() {
        if (visitedLinks.contains(url))
            return
        try {
           // println "Подключение к странице $url"
            Document document = getDefaultConnection(url).get()
            eventPublisher.publish(new Page(url: url, document: document))
            visitedLinks.add(url)
            Elements elements = document.select("a[href]")
            elements.stream()
                    .map(this::toUrl)
                    .filter(this::filterUrls)
                    .forEach((e) -> new ContentReceiverTask(eventPublisher, e).fork())
        } catch (Exception ignored) {
//            println "Ошибка при посещение сайта $url"
        }
    }


    static def filterUrls(String url) {
        url.startsWith("http://") || url.startsWith("https://")
    }

    static def toUrl(Element element) {
        element.attr("href")
    }

    static Connection getDefaultConnection(String url) {
        Jsoup.connect(url)
                .data("query", "Java")
                .userAgent("Mozilla")
                .cookie("auth", "token")
                .timeout(5000)
    }

}

