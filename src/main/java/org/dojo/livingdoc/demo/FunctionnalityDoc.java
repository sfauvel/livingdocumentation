package org.dojo.livingdoc.demo;

import org.dojo.livingdoc.annotation.ClassDemo;
import org.dojo.livingdoc.annotation.Functionnality;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;

import java.lang.reflect.Method;
import java.util.Set;

import static java.util.stream.Collectors.joining;


/**
 * Display method with annotation that contains attribute.
 *
 * We need to create an annotation (here Functionnality) that could be use on each method.
 * This annotation contains attributs that could be use to specify some information.
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
@ClassDemo(group="Annotation", label="Annotated method demo")
public class FunctionnalityDoc {
    public static void main(String[] args) {
        Set<Method> typesAnnotatedWith = getAnnotatedMethod();


        String doc = typesAnnotatedWith.stream()
                .map(m -> formatDoc(m))
                .collect(joining("\n"));

        System.out.println(doc);

    }

    // tag::example[]
    /// Retrieve methods with a specific annotation.
    private static Set<Method> getAnnotatedMethod() {
        String packageToScan = "org.dojo.livingdoc";
        Class<Functionnality> annotationToSearch = Functionnality.class;

        Reflections reflections = new Reflections(packageToScan, new MethodAnnotationsScanner());

        return reflections.getMethodsAnnotatedWith(annotationToSearch);
    }

    /// Extract information from annotation parameters (here the attribute name).
    private static String formatDoc(Method method) {
        return method.getName() + ":"
                + method.getDeclaredAnnotation(Functionnality.class).name();
    }
    // end::example[]
}
