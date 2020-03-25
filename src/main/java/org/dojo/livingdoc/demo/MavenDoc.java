package org.dojo.livingdoc.demo;

import org.dojo.livingdoc.annotation.ClassDemo;
import org.dojo.livingdoc.annotation.GenerateDoc;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Execute Maven command to find dependencies informations.
 *
 * Here, we execute mvn dependency:list to retrieve dependencies of each module in project.
 * Then, we draw a graph with these dependencies.
 */
@ClassDemo(group = "Execute to get information", label = "Execute maven command")
public class MavenDoc {
    public static void main(String[] args) throws IOException, SAXException {
        System.out.println(new MavenDoc().generateDendencies());
    }

    @GenerateDoc(name = "Dependencies executing maven command")
    // tag::example[]
    public String generateDendencies()
            throws IOException, SAXException {

        Path PROJECT_PATH = Path.of("src", "main", "resources", "project");
        List<String> modules = getModules(PROJECT_PATH);

        String dependenciesGraph = modules.stream()
                .flatMap(module -> findDependencies(PROJECT_PATH.resolve(module)).stream()
                        .map(dependency -> String.format("\"%s\" -> \"%s\"", module, dependency))
                )
                .collect(Collectors.joining("\n"));

        return String.join("\n",
                "",
                "[graphviz]",
                "----",
                "digraph g {",
                dependenciesGraph,
                "}",
                "----");
    }

    // end::example[]

    // tag::example[]
    private List<String> findDependencies(Path path) {

        List<String> recorder = executeCommand(path, List.of("mvn", "dependency:list"));

        return recorder.stream()
                .map(s -> s.replaceFirst("^\\[INFO\\]", ""))
                .map(String::trim)
                .filter(isADependencyLine())
                .map(this::extractArtifact)
                .collect(Collectors.toList());
    }

    private List<String> getModules(Path path) {
        return Arrays.stream(path.toFile().listFiles(java.io.File::isDirectory))
                .filter(f -> Paths.get(f.getPath(), "pom.xml").toFile().exists())
                .map(java.io.File::getName)
                .collect(Collectors.toList());
    }
    // end::example[]

    private Predicate<String> isADependencyLine() {
        return s -> s.matches("([\\w-]+\\.)+([\\w-]+):([\\w-]+):+([\\w-]+)+:([\\w-]+).*");
    }

    private String extractArtifact(String s) {
        return s.split(":")[1];
    }

    private static class StreamGobbler implements Runnable {
        private InputStream inputStream;
        private Consumer<String> consumer;

        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines()
                    .forEach(consumer);
        }
    }

    private List<String> executeCommand(Path path, List<String> command) {


        boolean isWindows = System.getProperty("os.name")
                .toLowerCase().startsWith("windows");

        List<String> execute = isWindows
                ? List.of("cmd.exe", "/c")
//                : List.of("sh", "-c");
                : List.of();


        List<String> fullCommand = Stream.concat(
                execute.stream(),
                command.stream())
                .collect(Collectors.toList());


        System.out.println(String.format("Command: %s", fullCommand.stream().collect(Collectors.joining(" "))));

        List<String> recorder = new ArrayList<>();

        ProcessBuilder builder = new ProcessBuilder();
        builder.directory(path.toFile());
        builder.command(fullCommand);

        Process process = null;
        ExecutorService executorService = null;
        try {

            process = builder.start();
            StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), e -> {
                System.out.println(e);
                recorder.add(e);
            });

            executorService = Executors.newSingleThreadExecutor();
            executorService.submit(streamGobbler);

            int exitCode = process.waitFor();
            assert exitCode == 0;

            return recorder;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (process != null) process.destroy();
            if (executorService != null) executorService.shutdown();
        }

    }

}
