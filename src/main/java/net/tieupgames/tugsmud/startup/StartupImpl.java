package net.tieupgames.tugsmud.startup;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

import java.lang.reflect.Constructor;
import java.util.*;

class StartupImpl {

    private final Set<Class<? extends StartupTask>> _tasksRequested;
    private final Set<Class<? extends StartupTask>> _tasksComplete;
    private final Map<Class<? extends StartupTask>, Set<Class<? extends StartupTask>>> _tasksDependedOn;
    private final Startup _startup;

    StartupImpl(Startup startup, FastClasspathScanner classpathScanner) {
        _startup = startup;
        _tasksDependedOn = new HashMap<>();
        _tasksRequested = new HashSet<>();
        _tasksComplete = new HashSet<>();

        classpathScanner
                .matchClassesImplementing(StartupTask.class,
                        task -> _tasksRequested.add(task.asSubclass(StartupTask.class)))
                .scan();

        for (Class<? extends StartupTask> task: _tasksRequested) {
            StartupTaskDependency[] data = task.getAnnotationsByType(StartupTaskDependency.class);
            for (StartupTaskDependency datum: data) {
                _tasksDependedOn.computeIfAbsent(task, anything -> new HashSet<>()).add(datum.value());
            }
        }
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
        constructor.newInstance().run(_startup);
        _tasksComplete.add(task);
    }

    private Set<Class<? extends StartupTask>> getUnsatisfiedDependenciesFor(Class<? extends StartupTask> task) {
        Set<Class<? extends StartupTask>> result = _tasksDependedOn.computeIfAbsent(task, anything -> new HashSet<>());
        result.removeAll(_tasksComplete);
        return result;
    }
}
