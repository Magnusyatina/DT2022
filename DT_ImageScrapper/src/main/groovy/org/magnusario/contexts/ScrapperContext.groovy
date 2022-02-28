package org.magnusario.contexts


import org.magnusario.listeners.TaskGenerator
import org.reflections.Reflections

import java.util.concurrent.ForkJoinPool

class ScrapperContext extends AbstractContext {

    Reflections scanner
    TaskGenerator taskGenerator
    ForkJoinPool forkJoinPool
    DefaultProperties defaultProperties

    ScrapperContext(String packageName, ForkJoinPool forkJoinPool) {
        this.scanner = new Reflections(packageName)
        this.forkJoinPool = forkJoinPool
        this.taskGenerator = new TaskGenerator(this)
        loadProperties()
        this.taskGenerator.defaultProperties = this.defaultProperties
    }

    def loadProperties() {
        Properties properties = new Properties()
        properties.load(new FileReader("./src/main/resources/application.properties"))
        DefaultProperties defaultProperties = new DefaultProperties()
        properties.each {   key, value ->
            println "$key: $value"
            defaultProperties.metaClass["$key"] = value
        }
        this.defaultProperties = defaultProperties
    }

    @Override
    def publish(Object event) {
        taskGenerator.publish(event)
    }

    Reflections getScanner() {
        scanner
    }

    @Override
    ForkJoinPool getExecutorService() {
        forkJoinPool
    }
}
