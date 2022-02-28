package org.magnusario.tasks

import org.magnusario.contexts.DefaultProperties
import org.magnusario.listeners.EventPublisher
import java.util.concurrent.RecursiveAction

abstract class Task<T> extends RecursiveAction {
    EventPublisher eventPublisher

    DefaultProperties defaultProperties

    Task(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher
    }
}
