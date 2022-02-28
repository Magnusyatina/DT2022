package org.magnusario.listeners

import org.magnusario.contexts.AbstractContext
import org.magnusario.contexts.DefaultProperties
import org.magnusario.tasks.Task
import org.reflections.Reflections


import java.lang.reflect.Constructor

class TaskGenerator implements EventPublisher {

    AbstractContext context

    DefaultProperties defaultProperties

    Map<Class<?>, Class<? extends Task>> registeredTaskClasses = new HashMap<>()

    TaskGenerator(AbstractContext context) {
        this.context = context
        configure()
    }

    def configure() {
        Reflections scanner = context.getScanner()
        Set<Class<? extends Task>> taskImplementations = scanner.getSubTypesOf(Task.class)
        taskImplementations.forEach {
            Class<?> genericType = it.getGenericSuperclass().getActualTypeArguments()[0]
            registeredTaskClasses.put(genericType, it)
        }
    }

    @Override
    def publish(Object event) {
        Class<? extends Task> taskClass = registeredTaskClasses[event.getClass()]
        if (taskClass == null)
            return
        Constructor constructor = taskClass.getConstructor(EventPublisher.class, event.class)
        if (constructor == null)
            return
        Task task = constructor.newInstance(this, event)
        task.defaultProperties = defaultProperties
        context.getExecutorService().submit(task)
    }
}
