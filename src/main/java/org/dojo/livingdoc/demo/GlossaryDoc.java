package org.dojo.livingdoc.demo;

import org.dojo.livingdoc.annotation.ClassDemo;
import org.dojo.livingdoc.annotation.Glossary;
import org.reflections.Reflections;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

/**
 * Display annotated classes.
 *
 * Retrieve all classes annotated (annotation Glossary) to be included into glossary.
 */
@ClassDemo(label = "Glossary demo")
public class GlossaryDoc {
    public static void main(String[] args) {
        System.out.println(generateGlossary());
    }

    // tag::example[]
    private static String generateGlossary() {

        return new Reflections("org.dojo.livingdoc")
                .getTypesAnnotatedWith(Glossary.class, false)
                .stream()
                .map(GlossaryDoc::formatGlossary)
                .collect(joining());
    }

    /// Format class to generate glossary information.
    private static String formatGlossary(Class<?> classToDocument) {
        return classToDocument.getSimpleName() + "\n";
    }
    // end::example[]
}
