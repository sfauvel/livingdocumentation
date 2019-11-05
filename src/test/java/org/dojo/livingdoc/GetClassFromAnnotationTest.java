package org.dojo.livingdoc;

import java.util.Set;

import org.dojo.livingdoc.annotation.Glossary;
import org.dojo.livingdoc.application.City;
import org.dojo.livingdoc.application.Person;
import org.dojo.livingdoc.application.TechnicalStuff;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

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
