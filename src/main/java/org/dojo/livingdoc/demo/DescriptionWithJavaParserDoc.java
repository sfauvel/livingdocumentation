package org.dojo.livingdoc.demo;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import com.github.javaparser.javadoc.description.JavadocDescription;
import com.github.javaparser.utils.SourceRoot;
import org.dojo.livingdoc.ClassToDocument;
import org.dojo.livingdoc.annotation.ClassDemo;
import org.dojo.livingdoc.annotation.GenerateDoc;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Get description from javadoc comment using JavaParser.
 *
 * It's a simple example retrieve javadoc from class and methods.
 */
@ClassDemo(group="Extract javadoc", label = "JavaDoc with JavaParser")
public class DescriptionWithJavaParserDoc {

    public static void main(String... args) {
        System.out.println(new DescriptionWithJavaParserDoc().generateDoc());
    }

    @GenerateDoc(name = "Javadoc extract from class")
    // tag::example[]
    public String generateDoc() {
        Class<?> clazz = ClassToDocument.class;

        // Parse class source code.
        SourceRoot sourceRoot = new SourceRoot(Paths.get("src/main/java"));
        CompilationUnit cu = sourceRoot.parse(
                clazz.getPackage().getName(),
                clazz.getSimpleName() + ".java");

        // Visit code tree to retrieve javadoc.
        JavadocVisitorAdapter javadocVisitor = new JavadocVisitorAdapter();
        cu.accept(javadocVisitor, null);

        // Format result to create documentation.
        return String.join("\n",
                formatClass(javadocVisitor.javaDocOfClasses),
                javadocVisitor.javaDocOfMethods.stream()
                        .map(DescriptionWithJavaParserDoc::formatMethod)
                        .collect(Collectors.joining()));
    }

    public static class JavadocVisitorAdapter extends GenericVisitorAdapter<Object, Void> {

        JavaDocOfElement javaDocOfClasses;
        List<JavaDocOfElement> javaDocOfMethods = new ArrayList<>();

        @Override
        public Object visit(ClassOrInterfaceDeclaration n, Void arg) {
            javaDocOfClasses =
                    new JavaDocOfElement(n.getFullyQualifiedName().orElse(null), n.getComment());
            return super.visit(n, arg);
        }

        @Override
        public Object visit(MethodDeclaration n, Void arg) {
            javaDocOfMethods.add(
                    new JavaDocOfElement(n.getNameAsString(), n.getComment())
            );
            return super.visit(n, arg);
        }
    }
    // end::example[]

    private static String formatMethod(JavaDocOfElement j) {
        return String.format("\t- %s: %s\n",
                j.getNameOr("No name."),
                j.getDescriptionOr("No description.")
        );
    }

    private static String formatClass(JavaDocOfElement javaDocOfClasses) {
        return String.format("%s:\n%s",
                javaDocOfClasses.getNameOr("No name."),
                javaDocOfClasses.getDescriptionOr("No description.")
        );
    }

    public static class JavaDocOfElement {
        final Optional<String> elementName;
        final Optional<JavadocDescription> javadoc;

        public JavaDocOfElement(String elementName, Optional<Comment> comment) {
            this.elementName = Optional.ofNullable(elementName);
            this.javadoc = comment.map(c -> c.asJavadocComment().parse().getDescription());
        }

        public String getDescriptionOr(String defaultDescription) {
            return javadoc.map(JavadocDescription::toText).orElse(defaultDescription);
        }

        public String getNameOr(String defaultName) {
            return addTabulation(elementName.orElse(defaultName));
        }
    }

    private static String addTabulation(String comment) {
        return String.join("\n\t", comment.split("\n"));
    }
}
