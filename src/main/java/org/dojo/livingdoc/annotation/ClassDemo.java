package org.dojo.livingdoc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Define an annotation ti identify a demo.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ClassDemo {
    String label() default "";
}
