package org.dojo.livingdoc.demo;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import org.dojo.livingdoc.application.ClassToDocument;
import org.dojo.livingdoc.annotation.ClassDemo;

import java.io.File;
import java.util.stream.Collectors;

/**
 * Get description from javadoc comment with QDox.
 */
@ClassDemo(group="Extract javadoc", label = "JavaDoc with QDox")
public class DescriptionWithQDoxDoc {

    public static void main(String... args) {
        System.out.println(generateDoc(ClassToDocument.class));
    }

    private static String generateDoc(Class<?> aClass) {
        JavaProjectBuilder builder = new JavaProjectBuilder();
        builder.addSourceTree(new File("src/main/java"));

        JavaClass javaClass = builder.getClassByName(aClass.getCanonicalName());

        return String.format("%s: \n\t%s\n%s",
                javaClass.getName(),
                addTabulation(javaClass.getComment()),
                methodList(javaClass));

    }

    private static String methodList(JavaClass javaClass) {
        return javaClass.getMethods().stream()
                .map(javaMethod -> String.format("\t- %s: %s", javaMethod.getName(), javaMethod.getComment()))
                .collect(Collectors.joining("\n"));
    }

    private static String addTabulation(String comment) {
        return String.join("\n\t", comment.split("\n"));
    }
}
