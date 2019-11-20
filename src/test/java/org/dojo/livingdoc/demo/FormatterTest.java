package org.dojo.livingdoc.demo;

import org.dojo.livingdoc.tools.Formatter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FormatterTest {

    Formatter.AsciidoctorFormatter formatter = new Formatter.AsciidoctorFormatter();

    @Test
    public void should_include_a_code_fragment() {
        String asciidoc = formatter.sourceFragment("org/dojo/livingdoc/TechnicalStuff.java", "InterestingCode");

        assertEquals("\n----\n" +
                "include::{sourcedir}/org/dojo/livingdoc/TechnicalStuff.java[tags=InterestingCode]\n" +
                "----\n", asciidoc);
    }


    @Test
    public void should_build_source_fragment() {
        Formatter.Source source = formatter.source("org/dojo/livingdoc/TechnicalStuff.java");
        assertEquals("\n----\n" +
                "include::{sourcedir}/org/dojo/livingdoc/TechnicalStuff.java\n" +
                "----\n", source.toString());
    }

    @Test
    public void should_build_source_fragment_with_tag() {
        Formatter.Source source = formatter.source("org/dojo/livingdoc/TechnicalStuff.java")
                .withTag("example");

        assertEquals("\n----\n" +
                "include::{sourcedir}/org/dojo/livingdoc/TechnicalStuff.java[tags=example]\n" +
                "----\n", source.toString());
    }

    @Test
    public void should_build_source_fragment_with_full_information() {
        Formatter.Source source = formatter.source("org/dojo/livingdoc/TechnicalStuff.java")
                .withTag("example")
                .withLanguage("java")
                .withLegend("My description");

        assertEquals( "\n[source,java,indent=0]\n" +
                ".My description\n" +
                "----\n" +
                "include::{sourcedir}/org/dojo/livingdoc/TechnicalStuff.java[tags=example]\n" +
                "----\n", source.toString());
    }

}