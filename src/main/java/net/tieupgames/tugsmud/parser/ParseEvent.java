package net.tieupgames.tugsmud.parser;

import java.lang.annotation.*;

/**
 * Indicates that the annotated class describes a parse event. While parsing data
 * files, whenever a JSON object with a given {@linkplain #name() type} is encountered,
 * an appropriate parse event is constructed, and any non-{@code static}, non-{@code final},
 * non-{@code transient} fields of the event are initialized from that JSON object's properties.
 * After those fields are populated, it is used to get an instance of some other type.
 * <strong>The annotated type must declare a constructor taking a single argument of type
 * String.</strong> The argument will be set from the name supplied in the configuration JSON,
 * or to {@code null} when no name is given. (Note that the {@code transient} property here
 * is only used to configure the parser.)
 * <p>The precise procedure for initializing parse event fields is fairly complicated. In
 * general, each applicable field (as defined above) must be of one of the following types:
 * <ul>
 *     <li>any primitive type or boxed primitive type;</li>
 *     <li>{@code String};</li>
 *     <li>a class which a parse event can get;</li>
 *     <li>a Collection or array type of one of these types;</li>
 *     <li>a class declaring a constructor taking only one argument of one of these types.</li>
 * </ul>
 * In any case, the JSON field to read is exactly the name of the field itself.</p>
 * <p>For example, consider this input file fragment:
 * <code>{
 *     "ExampleType":{
 *         "ExampleInstance":{
 *             "foo":"bar",
 *             "someInt":1
 *         }
 *     }
 * }</code>
 * If such a fragment were parsed, it would trigger a parse event named "ExampleType". If
 * a class annotated with {@code @ParseEvent(name="ExampleType", callback=...)} can be found,
 * then an instance of that class would be created. Among that object's properties must be
 * one called "foo" and one called "someInt"; these must be, respectively, assignable from
 * String.class and int.class, or constructable from those types. The instance's constructor
 * would be called with "ExampleInstance" as its only argument.</p>
 * @see #produces() for requirements on the produced type
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParseEvent {

    /**
     * @return the type of fields that should trigger this event.
     */
    String name();

    /**
     * @return a class to use the annotated class to get instances of. In order for this to
     * happen, one of two things must be true:
     * <ul>
     *     <li>the other class must have a constructor, taking a single argument of the annotated type; or</li>
     *     <li>the annotated type must implement {@link java.util.function.Supplier}&lt;T&gt;, where T is
     *     the annotated type.</li>
     * </ul>
     * If both conditions hold, the {@code Supplier} implementation is preferred.
     */
    Class<?> produces();
}