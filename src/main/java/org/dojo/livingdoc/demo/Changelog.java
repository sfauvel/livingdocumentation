package org.dojo.livingdoc.demo;


import org.dojo.livingdoc.annotation.ClassDemo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * A simple way to make a change log is to have a changelog file with an asciidoctor file.
 *
 * It needs to be strict to update file on each changes.
 * But, if merge request is used in development process, it could be verify it was updated before accepting request.
 * It also easier to update a change log file than rewrite git history when there is something to correct.
 *
 * To find some information on how to write a change log:
 * link:https://keepachangelog.com[]
 *
 * .Changelog example file
 * ====
 *
 * include::CHANGELOG.adoc[]
 * ====
 *
 * _Example of changelog file when included_
 *
 * :leveloffset: 4
 *
 * include::CHANGELOG.adoc[]
 *
 * :leveloffset: 0
 */
@ClassDemo(group = "Change log", label = "Include a changelog file")
public class Changelog {

    // tag::example[]
    public String generatePomDescription() throws IOException {
        Files.copy(Paths.get("CHANGELOG.adoc"),
                Paths.get("./target/doc/CHANGELOG.adoc"),
                StandardCopyOption.REPLACE_EXISTING);

        return "include::CHANGELOG.adoc[]";
    }
    // end::example[]
}
