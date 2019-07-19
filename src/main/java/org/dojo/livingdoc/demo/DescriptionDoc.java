package org.dojo.livingdoc.demo;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaSource;
import org.dojo.livingdoc.annotation.ClassDemo;

import java.io.File;
import java.util.Collection;

/**
 * Get description from javadoc comment.
 */
@ClassDemo
public class DescriptionDoc {

    public static void main(String... args) {
        JavaProjectBuilder builder = new JavaProjectBuilder();
        builder.addSourceTree(new File("src/main/java"));
        Collection<JavaSource> javaSources = builder.getSources();


        JavaSource javaSource = javaSources.iterator().next();
        JavaClass javaClass = javaSource.getClasses().get(0);

        String comment = javaClass.getComment();

        System.out.println(javaClass.getName() + ": " + comment);

        for (JavaMethod javaMethod : javaClass.getMethods()) {
            System.out.println("\t- " + javaMethod.getName() + ": " + javaMethod.getComment());

        }
    }
}
