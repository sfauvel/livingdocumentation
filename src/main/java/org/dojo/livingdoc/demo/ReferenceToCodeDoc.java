package org.dojo.livingdoc.demo;

import org.dojo.livingdoc.annotation.ClassDemo;


/**
 * Extract a code fragment to include in documentation.
 *
 * To identify code to include into documentation, it have to be surrounded by tag::[TAG] and end::[TAG].
 *
 * .Define code to include
 * [source,java,indent=0]
 * ----
 *     tag::InterestingCode[]
 *     public void doNothing() {
 *         // Really interesting code.
 *     }
 *     end::InterestingCode[]
 * ----
 */
@ClassDemo
public class ReferenceToCodeDoc {
    // tag::example[]
    public static void main(String[] args) throws IllegalAccessException {
        String doc =
                "[source,java,indent=0]\n" +
                ".Best practice to follow\n" +
                "----\n" +
                "include::{sourcedir}/org/dojo/livingdoc/TechnicalStuff.java[tags=InterestingCode]\n" +
                "----\n";
    }
    // end::example[]

}
