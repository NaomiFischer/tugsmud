package net.tieupgames.tugsmud.parser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that this field should use a custom function to interpret JSON values.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ParseFieldWithConversionFunction {

    /**
     * @return the name of the conversion function to use. <strong>This method must
     * take two arguments, one of type JSONObject and one of type String.</strong>
     * It must return an object of the appropriate type (but can be declared as
     * returning anything, so long as it compiles). It may be static, but doesn't
     * have to be.
     * <p>The method should ignore any {@link ClassCastException}s, {@link org.json.JSONException}s,
     * or other exceptions related to reading JSON values. The parser will handle them.</p>
     */
    String value();

}