package org.dojo.livingdoc.demo;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import com.github.javaparser.utils.SourceRoot;
import org.dojo.livingdoc.annotation.ClassDemo;
import org.dojo.livingdoc.annotation.GenerateDoc;
import org.reflections.Reflections;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Parse code to extract informations.
 *
 * We can retrieve import, conditions, attributes, ...
 *
 * JavaParser: https://github.com/javaparser/javaparser
 */
@ClassDemo(group = "Static analysis", label = "Extract imports parsing code")
public class ParseDoc {
    public static void main(String[] args) {
        System.out.println(new ParseDoc().execute());
    }

    @GenerateDoc(name = "Parse code to extract information")
    // tag::example[]
    public String execute() throws Error {

        final Reflections reflections = new Reflections("org.dojo.livingdoc");
        Set<Class<?>> typesAnnotatedWith =
                reflections.getTypesAnnotatedWith(ClassDemo.class, false);

        SourceRoot sourceRoot = new SourceRoot(Paths.get("src/main/java"));

        return getImports(typesAnnotatedWith, sourceRoot)
                .limit(3)
                .collect(Collectors.joining("\n", "", "\n* ..."));

    }

    private Stream<String> getImports(Set<Class<?>> typesAnnotatedWith, SourceRoot sourceRoot) {

        return typesAnnotatedWith.stream().map(aClass -> {

            CompilationUnit cu = sourceRoot.parse(
                    aClass.getPackage().getName(),
                    aClass.getSimpleName() + ".java");

            List<String> imports = new ArrayList();
            cu.accept(new RecordImportsVisitor(), imports);

            return (String.format("* %s\n%s",
                    aClass.getSimpleName(),
                    imports.stream()
                            .distinct()
                            .filter(importName -> !importName.startsWith("java"))
                            .map(s -> "** " + s)
                            .collect(Collectors.joining("\n"))));
        });
    }

    /// Visitor that record imports
    class RecordImportsVisitor extends GenericVisitorAdapter<Object, List<String>> {
        @Override
        public Object visit(ImportDeclaration declaration, List<String> imports) {

            imports.add(extractPackageFromImport(declaration, imports));
            return super.visit(declaration, imports);
        }
    }
    // end::example[]

    private String extractPackageFromImport(ImportDeclaration declaration, List<String> imports) {
        try {
            Class<?> aClass = Class.forName(declaration.getNameAsString());
            return aClass.getPackageName();
        } catch (ClassNotFoundException e) {
            return declaration.getNameAsString();
        }
    }
}
