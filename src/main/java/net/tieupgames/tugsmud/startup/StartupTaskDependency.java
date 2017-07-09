package net.tieupgames.tugsmud.startup;

import java.lang.annotation.*;

/**
 * Indicates that a {@linkplain StartupTask startup task} should run only after some other task.
 * Recursive dependencies are handled as expected.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(StartupTaskDependencies.class)
public @interface StartupTaskDependency {

    Class<? extends StartupTask> value();

}
