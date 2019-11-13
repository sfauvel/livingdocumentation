package org.dojo.livingdoc.demo;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import com.github.javaparser.javadoc.description.JavadocDescription;
import com.github.javaparser.utils.SourceRoot;
import org.dojo.livingdoc.application.ClassToDocument;
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

    @GenerateDoc(name = "Javadoc extracted from class with a parser")
    // tag::example[]
    public String generateDoc() {
        Class<?> classToDocument = ClassToDocument.class;

        // Parse class source code.
        SourceRoot sourceRoot = new SourceRoot(Paths.get("src/main/java"));
        CompilationUnit cu = sourceRoot.parse(
                classToDocument.getPackage().getName(),
                classToDocument.getSimpleName() + ".java");

        // Visit code tree to retrieve javadoc.
        JavadocVisitorAdapter javadocVisitor = new JavadocVisitorAdapter();
        cu.accept(javadocVisitor, null);

        // Format result to create documentation.
        return String.join("\n",
                formatClass(javadocVisitor.javaDocOfClasses),
                "",
                javadocVisitor.javaDocOfMethods.stream()
                        .map(this::formatMethod)
                        .collect(Collectors.joining()));
    }

    /// Visitor to store class and methods javadoc.
    public static class JavadocVisitorAdapter extends GenericVisitorAdapter<Object, Void> {

        JavaDocOfElement javaDocOfClasses;
        List<JavaDocOfElement> javaDocOfMethods = new ArrayList<>();

        @Override
        public Object visit(ClassOrInterfaceDeclaration declaration, Void arg) {
            String className = declaration.getFullyQualifiedName().orElse(null);
            javaDocOfClasses =
                    new JavaDocOfElement(className, declaration.getComment());
            return super.visit(declaration, arg);
        }

        @Override
        public Object visit(MethodDeclaration declaration, Void arg) {
            javaDocOfMethods.add(
                    new JavaDocOfElement(declaration.getNameAsString(), declaration.getComment())
            );
            return super.visit(declaration, arg);
        }
    }
    // end::example[]

    private String formatMethod(JavaDocOfElement j) {
        return String.format("- %s: %s\n",
                j.getNameOr("No name."),
                j.getDescriptionOr("No description.")
        );
    }

    private String formatClass(JavaDocOfElement javaDocOfClasses) {
        return String.format("%s:\n%s",
                javaDocOfClasses.getNameOr("No name."),
                javaDocOfClasses.getDescriptionOr("No description.")
        );
    }

    /// Class to store information about an element and his javadoc.
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
            return elementName.orElse(defaultName);
        }

    }

}
