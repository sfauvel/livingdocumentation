package org.dojo.livingdoc.demo;

import org.dojo.livingdoc.annotation.ClassDemo;
import org.dojo.livingdoc.annotation.Glossary;
import org.reflections.Reflections;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

/**
 * Display annotated classes.
 */
@ClassDemo
public class GlossaryDoc {
    public static void main(String[] args) {
        Reflections reflections = new Reflections("org.dojo.livingdoc");
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Glossary.class, false);

        String doc = typesAnnotatedWith.stream()
                .map(Class::getSimpleName)
                .collect(joining("\n"));

        System.out.println(doc);

    }
}
