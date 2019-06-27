package org.dojo.livingdoc.demo;

import org.dojo.livingdoc.annotation.ClassDemo;
import org.dojo.livingdoc.annotation.Functionnality;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;

import java.lang.reflect.Method;
import java.util.Set;

import static java.util.stream.Collectors.joining;


/**
 * Display annotated method using annotation with attribute.
 *
 *
 * \@Functionnality(name="Living Documentation")
 *
 */
@ClassDemo
public class FunctionnalityDoc {
    public static void main(String[] args) {
        Set<Method> typesAnnotatedWith = getAnnotatedMethod();


        String doc = typesAnnotatedWith.stream()
                .map(m -> formatDoc(m))
                .collect(joining("\n"));

        System.out.println(doc);

    }

    // tag::example[]
    private static Set<Method> getAnnotatedMethod() {
        String packageToScan = "org.dojo.livingdoc";
        Class<Functionnality> annotationToSearch = Functionnality.class;

        Reflections reflections = new Reflections(packageToScan, new MethodAnnotationsScanner());

        return reflections.getMethodsAnnotatedWith(annotationToSearch);
    }
    // end::example[]

    private static String formatDoc(Method m) {
        return m.getName() + ":" + m.getDeclaredAnnotation(Functionnality.class).name();
    }
}
