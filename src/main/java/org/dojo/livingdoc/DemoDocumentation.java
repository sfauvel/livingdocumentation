package org.dojo.livingdoc;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import com.github.javaparser.utils.SourceRoot;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaSource;
import org.dojo.livingdoc.annotation.ClassDemo;
import org.dojo.livingdoc.annotation.GenerateDoc;
import org.dojo.livingdoc.annotation.GenerateGraph;
import org.dojo.livingdoc.demo.Formatter;
import org.dojo.livingdoc.demo.GraphvizGenerator;
import org.reflections.Reflections;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

/**
 * This class generate documentation of this project.
 */
public class DemoDocumentation {

    private final Collection<JavaSource> javaSources;
    private final org.dojo.livingdoc.demo.Formatter formatter = new Formatter.AsciidoctorFormatter();
    private final Reflections reflections = new Reflections("org.dojo.livingdoc");

    private final Path docPath = Paths.get("./target/doc");
    private final String docName = "demo.adoc";

    public static void main(String... args) throws IOException {
        new DemoDocumentation().execute();
    }

    public DemoDocumentation() {
        JavaProjectBuilder builder = new JavaProjectBuilder();
        builder.addSourceTree(new File("src/main/java"));
        javaSources = builder.getSources();

    }


    private String getGroup(Class<?> clazz) {
        return clazz.getDeclaredAnnotation(ClassDemo.class).group();
    }


    private void execute() throws IOException {

        String doc = formatter.standardOptions() +
                formatter.tableOfContent() +
                formatter.title(1, "Living documentation") +
                formatter.include(docPath.relativize(Paths.get(".", "README.adoc")).toString()) +

                getAvailableDemosChapter() +


                formatter.title(2, "Specifics behavior") +
                includeCodeFragment() +

                formatter.title(2, "Library dependencies") +
                includeGraph() +

                formatter.title(2, "Change log") +
                formatter.include("CHANGELOG.adoc");
        ;

        Files.createDirectories(Path.of("target", "doc"));
        Files.copy(Paths.get("CHANGELOG.adoc"), Paths.get("./target/doc/CHANGELOG.adoc"), StandardCopyOption.REPLACE_EXISTING);

        generateStyle();
        generateReport(doc);


    }

    /**
     * Create chapter containing all available demos.
     *
     * @return
     */
    private String getAvailableDemosChapter() {

        return formatter.title(2, "Available demos") +
                formatter.paragraph("List of demo classes available in this project.") +
                formatter.paragraph("Each demo is a simple program that extract some documentation from the code.",
                        "It illustrates a use case or a technique.",
                        "It contains a 'main' to make it a standalone application to be able to see what is generated",
                        " and to execute it independently.",
                        "We try to not use utilities classes to keep all generation code into a single class",
                        " to have all information necessary to reproduce example",
                        "",
                        "These demonstrations are minimalist.",
                        "They just show what is possible to do but may not worked on more generic cases.",
                        "") +

                Stream.concat(
                        findDemoClasses().stream()
                                .filter(c -> getGroup(c).isEmpty())
                                .map(clazz -> formatDemoClass(clazz, 3)),
                        findDemoClasses().stream()
                                .filter(Predicate.not(c -> getGroup(c).isEmpty()))
                                .collect(Collectors.groupingBy(c -> getGroup(c))).entrySet().stream()
                                .map(c -> {
                                    return formatter.title(3, c.getKey().toString()) +
                                            c.getValue().stream()
                                                    .map(clazz -> formatDemoClass(clazz, 4))
                                                    .sorted()
                                                    .collect(joining("\n"))
                                            ;
                                })
                ).sorted()
                        .collect(joining("\n"));


    }

    /**
     * Generate [name]-docinfo.html file that contains style to add to header.
     *
     * This file is included by adding metadata :docinfo: at the beginning of the adoc file.
     *
     * @throws IOException
     */
    private void generateStyle() throws IOException {
        Files.createDirectories(docPath);

        String style = String.join("\n",
                "<style>",
                ".sourceFile {",
                "    color:grey;",
                "    //display:none;",
                "}",
                "</style>"
        );

        File adocFile = docPath.resolve(docName.replace(".adoc", "-docinfo.html")).toFile();
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(adocFile))) {
            writer.append(style);
        }

    }


    private void generateReport(String doc) throws IOException {
        System.out.println(doc);
        Files.createDirectories(docPath);

        File adocFile = docPath.resolve(docName).toFile();
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(adocFile))) {
            writer.append(doc);
        }

    }

    private String includeCodeFragment() {
        return "\n[source,java,indent=0]\n" +
                ".Some interesting code to show\n" +
                formatter.sourceFragment("org/dojo/livingdoc/TechnicalStuff.java", "InterestingCode");

    }


    private String includeGraph() {

        JavaParser javaParser = new JavaParser();
        SourceRoot sourceRoot = new SourceRoot(Paths.get("src/main/java"));

        Map<Class<?>, List<String>> demoClasses = findDemoClasses().stream().collect(Collectors.toMap(a -> a, a -> new ArrayList()));
        for (Map.Entry<Class<?>, List<String>> entry : demoClasses.entrySet()) {

            Class<?> aClass = entry.getKey();

            CompilationUnit cu = sourceRoot.parse(aClass.getPackage().getName(), aClass.getSimpleName() + ".java");

            cu.accept(importVisitor, entry.getValue());

            System.out.println(aClass.getSimpleName() +
                    entry.getValue().stream().map(s -> "  import " + s).collect(Collectors.joining("\n", "\n", "")));

        }

        GraphvizGenerator graphvizGenerator = new GraphvizGenerator();
        Set<String> importsToShow = Set.of(
                "org.eclipse.jgit",
                "com.github.javaparser",
                "org.reflections",
                "com.thoughtworks.qdox"
        );

        Stream<GraphvizGenerator.Link> linkStream = demoClasses.entrySet().stream().flatMap(classListEntry -> {
            Stream<String> importsInClass = classListEntry.getValue().stream()
                    .map(classImports -> keepImportsOfClass(importsToShow, classImports))
                    .distinct()
                    .filter(Objects::nonNull);

            return importsInClass.map(e ->
                    new GraphvizGenerator.Link(
                            classListEntry.getKey().getSimpleName(),
                            "\"" + e + "\""
                    )
            );
        });

        linkStream.forEach(graphvizGenerator::addLink);

        return graphvizGenerator.generate();
    }

    private String keepImportsOfClass(Set<String> importsToShow, String classImports) {
        return importsToShow.stream()
                .filter(i -> classImports.startsWith(i))
                .findFirst()
                .orElse(null);
    }

    private Set<Class<?>> findDemoClasses() {
        return reflections.getTypesAnnotatedWith(ClassDemo.class, false);
    }

    private String formatDemoClass(Class<?> clazz, int titleLevel) {
        Optional<JavaClass> javaClass = getJavaClass(clazz);
        String comment = javaClass.map(j -> j.getComment().replaceAll("&#064;", "@"))
                .orElse("No description");

        String label = clazz.getDeclaredAnnotation(ClassDemo.class).label();

        return String.join("",
                formatter.title(titleLevel, label.isEmpty() ? clazz.getSimpleName() : label),
                formatter.paragraph("\n[.sourceFile]\nFrom: " + clazz.getCanonicalName()),
                formatter.paragraph(comment),
                formatter.source(classToJavaFile(clazz))
                        .withTag("example")
                        .withLanguage("java")
                        .withLegend("Code to extract information")
                        .toString(),
                "",
                generateGraphs(clazz),
                generatedDocs(clazz));

    }

    private String classToJavaFile(Class<?> clazz) {
        return clazz.getName().replaceAll("\\.", "/") + ".java";
    }

    private String generatedDocs(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(GenerateDoc.class))
                .map(m -> generatedDoc(clazz, m))
                .collect(Collectors.joining("\n"));
    }

    private String generatedDoc(Class<?> clazz, Method method) {
        try {
            String title = "." + method.getDeclaredAnnotation(GenerateDoc.class).name();

            Object o = clazz.getConstructor().newInstance();
            String doc = method.invoke(o).toString();
//            return title + "\n----\n" + doc + "\n----\n";
            return title + "\n====\n" + doc + "\n====\n";
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    private String generateGraphs(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(GenerateGraph.class))
                .map(m -> generateGraph(clazz, m))
                .collect(Collectors.joining("\n"));
    }

    private String generateGraph(Class<?> clazz, Method method) {
        try {
            String title = "." + method.getDeclaredAnnotation(GenerateGraph.class).name();

            Object o = clazz.getConstructor().newInstance();
            String graph = method.invoke(o).toString();
            return title + "\n" + graph;
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    private Optional<JavaMethod> getJavaMethod(Class<?> clazz, Method method) {
        return getJavaClass(clazz)
                .map(c -> c.getMethodBySignature(method.getName(), Collections.emptyList()));
    }

    private Optional<JavaClass> getJavaClass(Class<?> clazz) {
        return javaSources.stream()
                .map(JavaSource::getClasses)
                .flatMap(Collection::stream)
                .filter(c -> c.isA(clazz.getName()))
                .findFirst();
    }

    private GenericVisitorAdapter<Object, List<String>> importVisitor = new GenericVisitorAdapter<>() {
        @Override
        public Object visit(ImportDeclaration n, List<String> imports) {
            imports.add(n.getName().asString());
            return super.visit(n, imports);
        }
    };

}
