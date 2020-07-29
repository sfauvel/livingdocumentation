package org.dojo.livingdoc.demo;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.SourceRoot;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import org.dojo.livingdoc.annotation.ClassDemo;
import org.dojo.livingdoc.annotation.GenerateDoc;
import org.dojo.livingdoc.application.SpecificRule;
import org.dojo.livingdoc.tools.FormulaAsciiMath;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Display formula like stem:[sum_(i=1)^n i^3] using default formula syntax http://asciimath.org/[asciimath].
 *
 * It needs to add `:stem:` option in document (see link:https://asciidoctor.org/docs/user-manual/#activating-stem-support[])
 *
 * [source,java,indent=0]
 * .Method to document
 * ----
 * include::{sourcedir}/org/dojo/livingdoc/application/SpecificRule.java[tags=example]
 * ----
 *
 * We can extract formula from Javadoc.
 * It's easy (see generateFormulaFromJavaDoc method) but it's not the real formula used in code.
 *
 * Another way of doing is to parse code and format it using asciimath syntax.
 * Below, we show a naive implementation used to parse example.
 *
 * [source,java,indent=0]
 * .Formula parser
 * ----
 * include::{sourcedir}/org/dojo/livingdoc/tools/FormulaAsciiMath.java[tags=example]
 * ----
 **
 */
@ClassDemo(group = "Static analysis", label = "Document formula with stem")
public class FormulaDoc {

    private final JavaProjectBuilder builder;

    public static void main(String[] args) {
        System.out.println(new FormulaDoc().generateFormulaFromJavaDoc());
    }

    // tag::example[]
    public FormulaDoc() {
        builder = new JavaProjectBuilder();
        builder.addSourceTree(new File("src/main/java"));
    }

    // end::example[]

    @GenerateDoc(name = "Formula from javadoc")
    // tag::example[]
    public String generateFormulaFromJavaDoc() {
        JavaProjectBuilder builder = new JavaProjectBuilder();
        builder.addSourceTree(new File("src/main/java"));

        JavaClass javaClass = builder.getClassByName(SpecificRule.class.getName());
        final List<JavaMethod> methods = javaClass.getMethods();

        return methods.stream()
                .map(method -> method.getName() + ": " + method.getComment())
                .collect(Collectors.joining("\n"));

    }

    // end::example[]

    @GenerateDoc(name = "Formula from java code using parsing")
    // tag::example[]
    public String generateFormulaParsingCode() {
        final Class classWithFormula = SpecificRule.class;
        final String methodWithFormula = "xpNeedsToNextLevel";

        String javaCode = extractMethodBody(classWithFormula, methodWithFormula);
        String formulaCode = javaCode.replaceAll("^\\{\\s*return (.*)\\s*\\}$", "$1");

        return methodWithFormula + ": stem:[" + FormulaAsciiMath.fromJava(formulaCode) + "]";

    }

    private String extractMethodBody(Class classWithFormula, String methodWithFormula) {
        SourceRoot sourceRoot = new SourceRoot(Paths.get("src/main/java"));

        CompilationUnit cu = sourceRoot.parse(
                classWithFormula.getPackage().getName(),
                classWithFormula.getSimpleName() + ".java");

        StringBuffer javaCode = new StringBuffer();
        cu.accept(new VoidVisitorAdapter<StringBuffer>() {
            @Override
            public void visit(MethodDeclaration n, StringBuffer arg) {
                if (methodWithFormula.equals(n.getNameAsString())) {
                    final String str = n.getBody()
                            .map(body -> body.toString())
                            .orElse("");
                    System.out.println("BODY:" + str);
                    javaCode.append(str);

                }
            }
        }, null);
        return javaCode.toString();
    }
    // end::example[]

}
