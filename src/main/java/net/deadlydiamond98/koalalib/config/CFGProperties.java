package net.deadlydiamond98.koalalib.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to assign custom properties to fields in config class
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CFGProperties {

    boolean hasDesc() default true;
    double min() default Integer.MIN_VALUE;
    double max() default Integer.MAX_VALUE;
}
