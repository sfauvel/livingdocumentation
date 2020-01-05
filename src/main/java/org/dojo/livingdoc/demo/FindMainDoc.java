package org.dojo.livingdoc.demo;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import org.dojo.livingdoc.annotation.ClassDemo;
import org.dojo.livingdoc.annotation.GenerateDoc;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

/**
 * Display classes with a main method.
 *
 * Retrieve all main methods in project using Reflections library.
 * We search all classes in given package and retain only those who have a main method.
 * Result is display in a list.
 */
@ClassDemo(group = "Reflexion", label = "Classes with main method")
public class FindMainDoc {

    private final JavaProjectBuilder builder;

    public static void main(String[] args) {
        System.out.println(new FindMainDoc().generate());
    }

    // tag::example[]
    public FindMainDoc() {
        builder = new JavaProjectBuilder();
        builder.addSourceTree(new File("src/main/java"));
    }

    // end::example[]

    @GenerateDoc(name = "Classes with main methods generated")
    // tag::example[]
    public String generate() {
        return new Reflections("org.dojo.livingdoc", new SubTypesScanner(false))
                .getSubTypesOf(Object.class).stream()
                .filter(this::isContainMainMethod)
                .map(o -> o.getSimpleName())
                .collect(joining("\n* ", "* ", ""));
    }

    private boolean isContainMainMethod(Class<?> aClass) {
        return Arrays.stream(aClass.getDeclaredMethods())
                .anyMatch(m -> Modifier.isStatic(m.getModifiers())
                            && m.getName().equals("main")
                );
    }
    // end::example[]

}
