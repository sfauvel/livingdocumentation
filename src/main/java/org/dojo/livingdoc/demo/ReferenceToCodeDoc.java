package org.dojo.livingdoc.demo;

import org.dojo.livingdoc.Configuration;
import org.dojo.livingdoc.annotation.ClassDemo;

import java.lang.reflect.Field;


/**
 * Extract a code fragment to include in documentation.
 *
 */
@ClassDemo
public class ReferenceToCodeDoc {
    public static void main(String[] args) throws IllegalAccessException {

        String doc = "[source,java,indent=0]\n" +
                ".Best practice to follow\n" +
                "----\n" +
                "include::{sourcedir}/org/dojo/livingdoc/TechnicalStuff.java[tags=InterestingCode]\n" +
                "----\n";

    }

}
