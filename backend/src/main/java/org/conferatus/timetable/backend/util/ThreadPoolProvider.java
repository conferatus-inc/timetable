package org.conferatus.timetable.backend.util;

import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Component
public class ThreadPoolProvider {
    private final Executor lowPriority;

    private final Executor highPriority;


    public ThreadPoolProvider() {
        lowPriority = Executors.newFixedThreadPool(2);
        highPriority = Executors.newCachedThreadPool();
        //todo: setup required
    }

    public Executor highPriority() {
        return highPriority;
    }

    public Executor lowPriority() {
        return lowPriority;
    }
}
