package org.dojo.livingdoc.demo;

import org.dojo.livingdoc.Configuration;
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
 * Sometimes, it's not possible or to difficult to find information directly into the code.
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
        return "Default value of " + instance.getClass().getSimpleName() + "\n"
                + Arrays.stream(Configuration.class.getDeclaredMethods())
                    .filter(ExecuteDoc::isGetter)
                    .map(m -> format(instance, m))
                    .collect(Collectors.joining("\n"));
    }

    private static boolean isGetter(Method m) {
        return (m.getName().startsWith("get") || m.getName().startsWith("is"))
                && Modifier.isPublic(m.getModifiers());
    }

    private static String format(Object instance, Method method) {
        try {
            return "\t- " + method.getName() + ":" + method.invoke(instance);

        } catch (IllegalAccessException | InvocationTargetException e) {
            return "Value could not be retrieve";
        }
    }
    // end::example[]

}
