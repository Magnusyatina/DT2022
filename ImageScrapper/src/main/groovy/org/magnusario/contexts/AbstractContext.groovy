package org.magnusario.contexts

import org.magnusario.listeners.EventPublisher
import org.reflections.Reflections

import java.util.concurrent.ForkJoinPool

abstract class AbstractContext implements EventPublisher {
    abstract Reflections getScanner()

    abstract ForkJoinPool getExecutorService()
}
