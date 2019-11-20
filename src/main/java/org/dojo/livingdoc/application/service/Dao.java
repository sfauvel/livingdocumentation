package org.dojo.livingdoc.application.service;

import org.dojo.livingdoc.application.Person;

import java.util.List;
import java.util.Optional;

public interface Dao {
    Optional<Person> findById(long id);

    List<Person> searchWithName(String lastName);
}
