package org.dojo.livingdoc.demo;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaSource;
import org.dojo.livingdoc.annotation.ClassDemo;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;

import java.io.File;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Display all test methods with comment.
 *
 * This class should be into src/test/java to see test classes.
 */
@ClassDemo
public class TestDoc {

    public static Collector<CharSequence, ?, String> JOIN_AS_LINES = Collectors.joining("\n");
    private final Collection<JavaSource> sources;


    public TestDoc() {
        JavaProjectBuilder builder = new JavaProjectBuilder();
        builder.addSourceTree(new File("src/test/java"));
        sources = builder.getSources();
    }

    public static void main(String... args) {
        new TestDoc().execute();

    }

    private void execute() {
        Reflections reflections = new Reflections("org.dojo.livingdoc",
                new MethodAnnotationsScanner());

        Set<Method> typesAnnotatedWith = reflections.getMethodsAnnotatedWith(Test.class);

        Map<? extends Class<?>, List<Method>> methodsPerClass = typesAnnotatedWith.stream().collect(Collectors.groupingBy(method -> method.getDeclaringClass()));


        String doc =  methodsPerClass.entrySet().stream()
                .map(type -> type.getKey() +"\n" + format(type.getValue()))
                .collect(JOIN_AS_LINES);

        System.out.println(doc);
    }

    private String format(List<Method> methods) {
        return methods.stream()
                .map(this::formatWithComment)
                .collect(JOIN_AS_LINES);
    }


    public String formatWithComment(Method method) {

        Optional<JavaSource> javaSource = getJavaSourceOf(method.getDeclaringClass());
        JavaClass javaClass = javaSource.get().getClasses().get(0);
        JavaMethod javaMethod = javaClass.getMethodBySignature(method.getName(), null);

        Optional<String> comment = Optional.ofNullable(javaMethod.getComment());
        return "\t- " + method.getName().replaceAll("_", " ")
                + comment.map(c -> "\n\t// " + c).orElse("")
                +"\n";
    }

    private Optional<JavaSource> getJavaSourceOf(Class<?> classToSearch) {
        return sources.stream()
                .filter(source -> source.getURL().getPath().endsWith(classToSearch.getSimpleName() + ".java"))
                .findFirst();
    }
}
