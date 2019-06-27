package org.dojo.livingdoc;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.dojo.livingdoc.annotation.Functionnality;
import org.dojo.livingdoc.annotation.Glossary;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetClassFromAnnotationTest {
    
    @Test
    public void should_get_class_with_annotation() {
        Reflections reflections = new Reflections("org.dojo.livingdoc");
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Glossary.class, false);
        assertTrue(typesAnnotatedWith.contains(Person.class));
        assertTrue(typesAnnotatedWith.contains(City.class));
        assertFalse(typesAnnotatedWith.contains(TechnicalStuff.class));
    }

}
