package net.tieupgames.tugsmud.startup;

/**
 * Classes implementing this interface are automatically run during startup. Such
 * a class must have a nullary constructor.
 * @see StartupTaskDependency
 */
public interface StartupTask {

    void run(Startup startup) throws Exception;

}