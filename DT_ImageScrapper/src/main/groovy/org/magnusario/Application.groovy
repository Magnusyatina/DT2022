package org.magnusario

import org.magnusario.contexts.ScrapperContext

import java.util.concurrent.ForkJoinPool
import java.util.concurrent.TimeUnit

class Application {

    static void main(String[] args) {
        def pool = new ForkJoinPool(12)
        ScrapperContext context = new ScrapperContext("org.magnusario", pool)
        context.publish("https://yandex.ru/images/search?text=%D1%80%D0%B8%D0%B0%D1%81%20%D0%B3%D1%80%D0%B5%D0%BC%D0%BE%D1%80%D0%B8&from=tabbar&pos=5&img_url=https%3A%2F%2Fpbs.twimg.com%2Fmedia%2FDaRYw9rUQAACmjJ.jpg&rpt=simage")
        pool.awaitQuiescence(1l, TimeUnit.MINUTES)
        pool.awaitTermination()
    }
}
