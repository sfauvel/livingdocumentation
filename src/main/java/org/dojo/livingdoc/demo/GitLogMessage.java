package org.dojo.livingdoc.demo;

import org.dojo.livingdoc.annotation.ClassDemo;
import org.dojo.livingdoc.annotation.GenerateDoc;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


/**
 * Extract commit messages from Git.
 *
 * It can be used to generate release note.
 *
 * JGit:  https://git-scm.com/book/fr/v2/Embarquer-Git-dans-vos-applications-JGit
 */
@ClassDemo(group = "Change log", label = "Extract changelog from git messages")
public class GitLogMessage {

    private final Path gitPath = Path.of(".git");

    public static void main(String[] args) throws IOException, GitAPIException {

        GitLogMessage gitLogMessage = new GitLogMessage();

        System.out.println(gitLogMessage.generateGitMessages());
    }

    @GenerateDoc(name = "Git history:")
    // tag::example[]
    public String generateGitMessages() throws IOException, GitAPIException {
        Repository repository = new FileRepositoryBuilder()
                .setGitDir(gitPath.toFile())
                .setMustExist(true)
                .build();

        Iterable<RevCommit> logs = new Git(repository).log().call();

        return StreamSupport.stream(logs.spliterator(), false)
                .limit(10)
                .map(rev -> formatMessage(rev, "dd/MM/yyyy"))
                .collect(Collectors.joining("\n"));
    }

    public String formatMessage(RevCommit rev, String dateFormat) {
        Date authorDate = rev.getAuthorIdent().getWhen();

        String dateFormatted = new SimpleDateFormat(dateFormat).format(authorDate);
        return String.format("* *%s* (%s): %s",
                dateFormatted,
                rev.getName().substring(0, 10),
                rev.getShortMessage());
    }
    // end::example[]
}
