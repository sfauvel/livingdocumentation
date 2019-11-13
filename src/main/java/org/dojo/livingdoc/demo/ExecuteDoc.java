package org.dojo.livingdoc.demo;

import org.dojo.livingdoc.application.Configuration;
import org.dojo.livingdoc.annotation.ClassDemo;
import org.dojo.livingdoc.annotation.GenerateDoc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;


/**
 * Execute some code to retrieve information.
 *
 * Sometimes, it's not possible or too difficult to find information directly from the code.
 * It could be easier to execute the code to get information.
 *
 * In this demonstration, we are creating a configuration object to get default values.
 *
 * An object instance is created and all getters are called using reflexion.
 * Values returned are defaults values returned by the object.
 */
@ClassDemo(label = "Get information executing code.")
public class ExecuteDoc {

    public static void main(String[] args) throws IllegalAccessException {
        System.out.println(new ExecuteDoc().generateDoc());
    }

    @GenerateDoc(name = "Default values of getter methods")
    public String generateDoc() {
        return generateDoc(new Configuration());
    }

    // tag::example[]
    public String generateDoc(Object instance) {
        return String.format("Default values of %s class\n\n", instance.getClass().getSimpleName())
                + String.format("[options=\"header\"]\n|===\n|Field|Default value\n%s\n|===\n",
                    Arrays.stream(Configuration.class.getDeclaredMethods())
                        .filter(this::isGetter)
                        .map(m -> formatRow(instance, m))
                        .collect(Collectors.joining("\n")));
    }

    private String formatRow(Object instance, Method method) {
        try {
            return String.format("|%s|%s", method.getName(), method.invoke(instance));

        } catch (IllegalAccessException | InvocationTargetException e) {
            return "Value could not be retrieve";
        }
    }

    private boolean isGetter(Method m) {
        return (m.getName().startsWith("get") || m.getName().startsWith("is"))
                && Modifier.isPublic(m.getModifiers());
    }
    // end::example[]

}
