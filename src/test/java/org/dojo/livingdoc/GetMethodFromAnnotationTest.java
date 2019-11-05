package org.dojo.livingdoc;

import org.dojo.livingdoc.annotation.Functionnality;
import org.dojo.livingdoc.application.ClassToDocument;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetMethodFromAnnotationTest {

    @Test
    public void should_get_method_with_annotation_with_parameters() throws NoSuchMethodException {
        Reflections reflections = new Reflections("org.dojo.livingdoc", new MethodAnnotationsScanner());
        Set<Method> methodsAnnotatedWith = reflections.getMethodsAnnotatedWith(Functionnality.class);
        assertTrue(methodsAnnotatedWith.contains(ClassToDocument.class.getMethod("functionnalityToDocument")));

        Functionnality declaredAnnotation = new ArrayList<>(methodsAnnotatedWith).get(0).getDeclaredAnnotation(Functionnality.class);

        assertEquals("Living Documentation", declaredAnnotation.name());
    }
}
