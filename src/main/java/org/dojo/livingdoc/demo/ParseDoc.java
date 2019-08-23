package org.dojo.livingdoc.demo;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import com.github.javaparser.utils.SourceRoot;
import org.dojo.livingdoc.annotation.ClassDemo;
import org.reflections.Reflections;

import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;


/**
 * Parse code to extract informations.
 *
 * We can retrieve import, conditions, attributes, ...
 *
 * JavaParser: https://github.com/javaparser/javaparser
 */
@ClassDemo
public class ParseDoc {
    public static void main(String[] args) {
        new ParseDoc().execute();
    }

    private void execute() throws Error {

        final Reflections reflections = new Reflections("org.dojo.livingdoc");
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(ClassDemo.class, false);

        SourceRoot sourceRoot = new SourceRoot(Paths.get("src/main/java"));

        getImports(typesAnnotatedWith, sourceRoot);

    }

    GenericVisitorAdapter<Object, List<String>> genericVisitorAdapter = new GenericVisitorAdapter<>() {
        @Override
        public Object visit(ImportDeclaration n, List<String> imports) {
            imports.add(n.getName().asString());
            return super.visit(n, imports);
        }
    };

    private void getImports(Set<Class<?>> typesAnnotatedWith, SourceRoot sourceRoot) {

        for (Class<?> aClass : typesAnnotatedWith) {

            CompilationUnit cu = sourceRoot.parse(aClass.getPackage().getName(), aClass.getSimpleName() + ".java");

            List<String> imports = new ArrayList();
            cu.accept(genericVisitorAdapter, imports);

            System.out.println(aClass.getSimpleName() +
                imports.stream().map(s -> "  import " + s).collect(Collectors.joining("\n", "\n", "")));

        }
    }


}
