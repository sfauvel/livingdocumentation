package org.dojo.livingdoc.demo;

import org.dojo.livingdoc.annotation.ClassDemo;
import org.dojo.livingdoc.annotation.Functionnality;
import org.dojo.livingdoc.annotation.GenerateDoc;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;

import java.lang.reflect.Method;
import java.util.Set;

import static java.util.stream.Collectors.joining;


/**
 * Display method with annotation that contains attribute.
 *
 * We need to create an annotation (here Functionnality) that be used on each method to document.
 * This annotation contains attributs to specify additional information.
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
 * include::{sourcedir}/org/dojo/livingdoc/demo/FunctionnalityDoc.java[tags=usage]
 * ----
 */
@ClassDemo(group = "Annotation", label = "Annotated method demo")
public class FunctionnalityDoc {

    public static void main(String[] args) {
        FunctionnalityDoc functionnalityDoc = new FunctionnalityDoc();

        System.out.println(functionnalityDoc.generateFunctionnalities());
    }

    /// Show a method using annotation.
    // tag::usage[]
    @Functionnality(name = "Living Documentation")
    public void functionnalityToDocument() {
        // ...
    }
    // end::usage[]

    @GenerateDoc(name = "Functionnalities to document.")
    // tag::example[]
    public String generateFunctionnalities() {
        Set<Method> annotatedMethod = getAnnotatedMethod();

        return annotatedMethod.stream()
                .map(m -> formatDoc(m))
                .collect(joining("\n\n"));
    }

    /// Retrieve methods with a specific annotation.
    private Set<Method> getAnnotatedMethod() {

        String packageToScan = "org.dojo.livingdoc";
        Reflections reflections = new Reflections(packageToScan, new MethodAnnotationsScanner());

        return reflections.getMethodsAnnotatedWith(Functionnality.class);
    }

    /// Extract information from annotation parameters (here the attribute name).
    private String formatDoc(Method method) {
        return String.format("*%s* (%s): %s",
                method.getName(),
                method.getDeclaringClass().getSimpleName(),
                method.getDeclaredAnnotation(Functionnality.class).name());
    }
    // end::example[]
}
