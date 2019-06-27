package org.dojo.livingdoc.demo;

import org.asciidoctor.Asciidoctor;

import java.io.File;
import java.util.HashMap;

import static org.asciidoctor.jruby.AsciidoctorJRuby.Factory.create;

/**
 * https://github.com/asciidoctor/asciidoctorj
 */
public class AsciidoctorGeneration {

    public static void main(String[] args) {
        Asciidoctor asciidoctor = create();

        String html = asciidoctor.convertFile(
                new File("src/main/resources/sample.adoc"),
                new HashMap<String, Object>());

        System.out.println(html);
    }
}

