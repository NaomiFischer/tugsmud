package net.tieupgames.tugsmud.startup;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class Startup {

    private final Set<Class<? extends StartupTask>> _tasksRequested;
    private final Set<Class<? extends StartupTask>> _tasksComplete;
    private final Map<Class<? extends StartupTask>, Set<Class<? extends StartupTask>>> _tasksDependedOn;

    Startup(FastClasspathScanner classpathScanner) {
        _tasksDependedOn = new HashMap<>();
        _tasksRequested = new HashSet<>();
        _tasksComplete = new HashSet<>();

        classpathScanner
                .matchClassesImplementing(StartupTask.class,
                        task -> _tasksRequested.add(task.asSubclass(StartupTask.class)))
                .scan();
    }

    void run() throws Exception {
        for (Class<? extends StartupTask> task: _tasksRequested) {
            run(task);
        }
    }

    private void run(Class<? extends StartupTask> task) throws Exception {
        if (_tasksComplete.contains(task)) {
            return;
        }
        for (Class<? extends StartupTask> subtask: getUnsatisfiedDependenciesFor(task)) {
            run(subtask);
        }
        Constructor<? extends StartupTask> constructor = task.getDeclaredConstructor();
        constructor.setAccessible(true);
        constructor.newInstance().run();
        _tasksComplete.add(task);
    }

    private Set<Class<? extends StartupTask>> getUnsatisfiedDependenciesFor(Class<? extends StartupTask> task) {
        Set<Class<? extends StartupTask>> result = _tasksDependedOn.computeIfAbsent(task, anything -> new HashSet<>());
        result.removeAll(_tasksComplete);
        return result;
    }
}
