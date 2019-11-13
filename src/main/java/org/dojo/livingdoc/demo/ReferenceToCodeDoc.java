package org.dojo.livingdoc.demo;

import org.dojo.livingdoc.annotation.ClassDemo;
import org.dojo.livingdoc.annotation.GenerateDoc;


/**
 * Extract a code fragment to include in documentation.
 *
 * To identify code to include into documentation, it have to be surrounded by tag::[TAG] and end::[TAG].
 *
 * .Define code to include
 * [source,java,indent=0]
 * ----
 * // tag::InterestingCode[]
 * public void doNothing() {
 * // Really interesting code.
 * }
 * // end::InterestingCode[]
 * ----
 */
@ClassDemo
public class ReferenceToCodeDoc {
    public static void main(String[] args) throws IllegalAccessException {
        new ReferenceToCodeDoc().includeCodeToDoc();
    }

    @GenerateDoc(name = "Include a fragment of code")
    // tag::example[]
    public String includeCodeToDoc() {
        return String.join("\n",
                "[source,java,indent=0]",
                ".Best practice to follow",
                "----",
                "include::{sourcedir}/org/dojo/livingdoc/application/TechnicalStuff.java[tags=InterestingCode]",
                "----");
    }
    // end::example[]

}
