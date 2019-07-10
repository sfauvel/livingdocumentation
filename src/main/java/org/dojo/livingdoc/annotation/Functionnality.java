package org.dojo.livingdoc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// tag::example[]
@Retention(RetentionPolicy.RUNTIME)
public @interface Functionnality {
    String name();
}
// end::example[]
