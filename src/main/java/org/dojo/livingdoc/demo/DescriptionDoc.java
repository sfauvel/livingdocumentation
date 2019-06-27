package org.dojo.livingdoc.demo;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaSource;
import org.dojo.livingdoc.annotation.ClassDemo;

import java.io.File;

/**
 * Get description from javadoc comment.
 */
@ClassDemo
public class DescriptionDoc {

    public static void main(String... args) {
        JavaDocBuilder builder = new JavaDocBuilder();
        builder.addSourceTree(new File("src/main/java"));
        JavaSource[] javaSources = builder.getSources();


        JavaSource javaSource = javaSources[0];
        JavaClass javaClass = javaSource.getClasses()[0];

        String comment = javaClass.getComment();

        System.out.println(javaClass.getName() + ": " + comment);

        for (JavaMethod javaMethod : javaClass.getMethods()) {
            System.out.println("\t- " + javaMethod.getName() + ": " + javaMethod.getComment());

        }
    }
}
