package net.tieupgames.tugsmud.parser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a field should be left unchanged if the JSON configuration
 * doesn't specify a value for it. It is otherwise a parser error to not specify
 * one.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OptionalParseField {}
