package org.dojo.livingdoc.demo;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import org.dojo.livingdoc.annotation.ClassDemo;
import org.dojo.livingdoc.annotation.GenerateDoc;
import org.dojo.livingdoc.annotation.Glossary;
import org.reflections.Reflections;

import java.io.File;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

/**
 * Display annotated classes.
 *
 * Retrieve all classes annotated (annotation Glossary) to be included into glossary.
 */
@ClassDemo(group = "Annotation", label = "Glossary demo")
public class GlossaryDoc {

    private final JavaProjectBuilder builder;

    public static void main(String[] args) {
        System.out.println(new GlossaryDoc().generateGlossary());
    }

    public GlossaryDoc() {
        builder = new JavaProjectBuilder();
        builder.addSourceTree(new File("src/main/java"));
    }

    @GenerateDoc(name = "Glossary generated")
    // tag::example[]
    public String generateGlossary() {

        return new Reflections("org.dojo.livingdoc")
                .getTypesAnnotatedWith(Glossary.class, false)
                .stream()
                .map(this::formatGlossary)
                .collect(joining());
    }

    /// Format class to generate glossary information.
    private String formatGlossary(Class<?> classToDocument) {
        return classToDocument.getSimpleName() + "::" + getDescription(classToDocument) + "\n";
    }
    // end::example[]

    private String getDescription(Class<?> classToDocument) {
        JavaClass javaClass = builder.getClassByName(classToDocument.getCanonicalName());
        return " " + Optional.ofNullable(javaClass.getComment()).orElse("");

    }
}
