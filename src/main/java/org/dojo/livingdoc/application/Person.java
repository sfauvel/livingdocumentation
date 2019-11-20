package org.dojo.livingdoc.application;

import org.dojo.livingdoc.annotation.Glossary;

/**
 * A physical person.
 */
@Glossary
public class Person {
    private final String firstName;
    private final String lastName;

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
