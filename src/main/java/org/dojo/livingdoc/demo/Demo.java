package org.dojo.livingdoc.demo;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import com.github.javaparser.utils.SourceRoot;
import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaSource;
import org.asciidoctor.Asciidoctor;
import org.dojo.livingdoc.annotation.ClassDemo;
import org.reflections.Reflections;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;
import static org.asciidoctor.jruby.AsciidoctorJRuby.Factory.create;

public class Demo {

    private final JavaSource[] javaSources;
    private final Formatter formatter = new Formatter.AsciidoctorFormatter();
    private final Reflections reflections = new Reflections("org.dojo.livingdoc");

    public static void main(String... args) throws FileNotFoundException {
        new Demo().execute();

    }

    public Demo() {
        JavaDocBuilder builder = new JavaDocBuilder();
        builder.addSourceTree(new File("src/main/java"));
        javaSources = builder.getSources();

    }

    private void execute() throws FileNotFoundException {


        String doc =
                formatter.title(1, "Living documentation") +
                        formatter.tableOfContent() +
                        ":sourcedir: ..\n" +
                        ":source-highlighter: pygments\n" +
                        formatter.title(2, "Available demo") +
                        findDemoClasses().stream()
                                .map(this::formatDemoClass)
                                .collect(joining("\n")) +
                        formatter.title(2, "Specifics behavior") +
                        includeCodeFragment() +
                        formatter.title(2, "Library dependencies") +
                        includeGraph();

        generateReport(doc);


    }

    private void generateReport(String doc) throws FileNotFoundException {
        System.out.println(doc);

        File adocFile = new File("./target/demo.adoc");
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(adocFile))) {
            writer.append(doc);
        }

        Asciidoctor asciidoctor = create();
        String html = asciidoctor.convertFile(
                adocFile,
                new HashMap<String, Object>());
    }

    private String includeCodeFragment() {
        return "\n[source,java,indent=0]\n" +
                ".Some interesting code to show\n" +
                "----\n" +
                "include::{sourcedir}/org/dojo/livingdoc/TechnicalStuff.java[tags=InterestingCode]\n" +
                "----\n";
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

        return "\n[graphviz]\n" +
                "----\n" +
                "digraph g {\n" +
                demoClasses.entrySet().stream()
                        .map(this::formatDependency)
                        .collect(Collectors.joining("\n")) +
                "\n}\n" +
                "----\n"
                ;
    }

    private String formatDependency(Map.Entry<Class<?>, List<String>> classListEntry) {
        Set<String> importsToShow = Set.of(
                "org.eclipse.jgit",
                "com.github.javaparser",
                "org.reflections",
                "com.thoughtworks.qdox"
        );
        return classListEntry.getValue().stream()
                .map(e -> importsToShow.stream().filter(i -> e.startsWith(i)).findFirst().orElse(null))
                .distinct()
                .filter(Objects::nonNull)
                .map(e -> classListEntry.getKey().getSimpleName() + " -> "
                        + "\"" + e + "\"")
                .collect(Collectors.joining("\n"));
    }

    private Set<Class<?>> findDemoClasses() {
        return reflections.getTypesAnnotatedWith(ClassDemo.class, false);
    }

    private String formatDemoClass(Class<?> clazz) {

        Optional<JavaClass> javaClass = getJavaClass(clazz);
        String comment = javaClass.map(j -> j.getComment()).orElse("No description");


        return formatter.title(3, clazz.getSimpleName())
                + formatter.paragraph(comment)
                + formatter.sourceCode("include::{sourcedir}/" + clazz.getName().replaceAll("\\.", "/") + ".java[tags=example]\n");
    }

    private Optional<JavaClass> getJavaClass(Class<?> clazz) {
        return Arrays.stream(javaSources)
                .map(JavaSource::getClasses)
                .flatMap(Arrays::stream)
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
