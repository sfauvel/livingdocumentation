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
 * [source,java,indent=0]
 * .Annotation declaration
 * ----
 * include::{sourcedir}/org/dojo/livingdoc/annotation/Functionnality.java[tags=example]
 * ----
 *
 * [source,java,indent=0]
 * .Annotation usage
 * ----
 * &#064;Functionnality(name="Living Documentation")
 * public void generateDoc() {
 *     // ...
 * }
 * ----
 *
 */
@ClassDemo(label="Annotated method demo")
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
