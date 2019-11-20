package org.dojo.livingdoc.application.service;

import org.dojo.livingdoc.application.Person;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A service calling other things.
 */
public class Service {

    private final Dao dao;
    private final Notifier notifier;

    public Service(Dao dao, Notifier notifier) {
        this.dao = dao;
        this.notifier = notifier;
    }


    public void findHomonyms(long id) {
        Optional<Person> person = dao.findById(id);

        person.ifPresentOrElse(
                p -> {
                    String lastName = p.getLastName();
                    List<Person> homonyms = dao.searchWithName(lastName);
                    Stream<String> firstNames = homonyms.stream()
                            .map(homonym -> homonym.getFirstName())
                            .distinct();

                    notifier.send(String.join("\n",
                            "There is " + homonyms.size() + " homonyms. ",
                            "Other first names are: ",
                            firstNames.collect(Collectors.joining("\n- ", "\n- ", ""))));
                },
                () ->  notifier.send("No person with id:" + id)
        );

    }
}
