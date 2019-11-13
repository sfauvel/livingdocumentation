package org.dojo.livingdoc.demo;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import org.dojo.livingdoc.annotation.GenerateDoc;
import org.dojo.livingdoc.application.ClassToDocument;
import org.dojo.livingdoc.annotation.ClassDemo;

import java.io.File;
import java.util.stream.Collectors;

/**
 * Get description from javadoc comment with QDox.
 */
@ClassDemo(group="Extract javadoc", label = "JavaDoc with QDox")
public class DescriptionWithQDoxDoc {

    private final Class<?> classToDocument = ClassToDocument.class;

    public static void main(String... args) {
        DescriptionWithQDoxDoc descriptionWithQDoxDoc = new DescriptionWithQDoxDoc();
        System.out.println(descriptionWithQDoxDoc.generateDoc());
    }

    @GenerateDoc(name = "Javadoc extracted from class with QDox")
    // tag::example[]
    public String generateDoc() {
        JavaProjectBuilder builder = new JavaProjectBuilder();
        builder.addSourceTree(new File("src/main/java"));

        JavaClass javaClass = builder.getClassByName(classToDocument.getCanonicalName());

        return String.format("%s: \n%s\n\n%s",
                javaClass.getName(),
                javaClass.getComment(),
                methodList(javaClass));
    }

    private static String methodList(JavaClass javaClass) {
        return javaClass.getMethods().stream()
                .map(javaMethod -> String.format("- %s: %s",
                        javaMethod.getName(),
                        javaMethod.getComment()))
                .collect(Collectors.joining("\n"));
    }
    // end::example[]

}
