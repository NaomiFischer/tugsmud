package net.tieupgames.tugsmud.parser;

/**
 * {@code Identified} implementations have custom behavior for computing registry IDs.
 * This special implementation is used only when registering an object to the registry.
 */
public interface Identified {

    int idForNewRegistryEntry();

}
