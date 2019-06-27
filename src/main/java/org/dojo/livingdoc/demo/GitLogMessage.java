package org.dojo.livingdoc.demo;

import org.dojo.livingdoc.annotation.ClassDemo;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.StreamSupport;


/**
 * Extract commit messages from Git.
 *
 * It can be used to generate release note.
 *
 * JGit:  https://git-scm.com/book/fr/v2/Embarquer-Git-dans-vos-applications-JGit
 */
@ClassDemo
public class GitLogMessage {

    public final static File GIT_DIR = new File("../.git");

    public static void main(String[] args) throws IOException, GitAPIException {

        Repository repository = new FileRepositoryBuilder()
                .setGitDir(GIT_DIR)
                .setMustExist(true)
                .build();

        Iterable<RevCommit> log = new Git(repository).log().call();

        StreamSupport.stream(log.spliterator(), false)
                .limit(100)
                .map(rev ->  idAndMessage(rev, "dd/MM/yyyy"))
                .forEach(System.out::println);
    }


    public static String idAndMessage(RevCommit rev, String dateFormat) {
        Date authorDate = rev.getAuthorIdent().getWhen();

        String format = new SimpleDateFormat(dateFormat).format(authorDate);
        return rev.getName().substring(0, 10) + " " + format + ": " + rev.getShortMessage();
    }
}
