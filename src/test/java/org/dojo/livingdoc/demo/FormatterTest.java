package org.dojo.livingdoc.demo;

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
}