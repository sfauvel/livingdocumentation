package org.dojo.livingdoc.demo;

import org.dojo.livingdoc.Configuration;
import org.dojo.livingdoc.annotation.ClassDemo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;


/**
 * Execute some code to retrieve information.
 *
 * An object instance is created and all getters are called using reflexion.
 * Values returned are defaults values returned by the object.
 */
@ClassDemo
public class ExecuteDoc {

    public static void main(String[] args) throws IllegalAccessException {

        System.out.println("Default Configuration");
        System.out.println(generateDoc(new Configuration()));
    }

    // tag::example[]
    private static String generateDoc(Configuration configuration) {
        return Arrays.stream(Configuration.class.getDeclaredMethods())
                    .filter(ExecuteDoc::isGetter)
                    .map(m -> formatFn(configuration, m))
                    .collect(Collectors.joining());
    }

    private static boolean isGetter(Method m) {
        return m.getName().startsWith("get") && Modifier.isPublic(m.getModifiers());
    }

    private static String format(Configuration configuration, Method m)
            throws IllegalAccessException, InvocationTargetException {

        return m.getName() + ":" + m.invoke(configuration);
    }
    // end::example[]


    private static String formatFn(Configuration configuration, Method m) {
        try {
            return format(configuration, m);
        } catch (IllegalAccessException | InvocationTargetException e) {
            return "Value could not be retrieve";
        }
    }
}
