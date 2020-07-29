package org.dojo.livingdoc.tools;

import com.github.javaparser.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Transform Java code to asciimath text.
 */
public class FormulaAsciiMath {

    private boolean TRACE_ON = false;

    //tag::example[]
    public static String fromJava(String javaFormula) {
        return new FormulaAsciiMath().parse(javaFormula);
    }

    private String parse(String javaFormula) {
        final Provider codeProvider = Providers.provider("class X { String formula = " + javaFormula + " }");
        ParseResult<CompilationUnit> result = (new JavaParser()).parse(ParseStart.COMPILATION_UNIT, codeProvider);
        if (!result.isSuccessful()) {
            throw new RuntimeException(result.getProblems().stream()
                    .map(problem -> problem.getVerboseMessage())
                    .collect(Collectors.joining("\n")));
        }

        final CompilationUnit compilationUnit = result.getResult().get();

        //end::example[]
        traceParsingCalls(compilationUnit);

        //tag::example[]
        final FormulaVisitor formulaVisitor = new FormulaVisitor();
        Stack<String> stack = new Stack<>();
        compilationUnit.accept(formulaVisitor, stack);

        return  stack.peek();
    }

    //end::example[]
    /**
     * Trace calls to help debugging and find which `visit` method is called.
     * @param compilationUnit
     */
    private void traceParsingCalls(CompilationUnit compilationUnit) {
        if (!TRACE_ON) {
            return;
        }
        final VoidVisitor mock = Mockito.mock(VoidVisitorAdapter.class, Mockito.withSettings().defaultAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                final String collect = Arrays.stream(invocationOnMock.getArguments())
                        .map(arg -> (arg == null) ? "NULL" : arg.getClass().getSimpleName())
                        .collect(Collectors.joining(", "));
                System.out.println(invocationOnMock.getMethod().getName() + "(" + collect + ") " + invocationOnMock.getArgument(0).toString());
                return invocationOnMock.callRealMethod();
            }
        }));
        compilationUnit.accept(mock, null);
        System.out.println("*******************************");
    }

    //tag::example[]
    public static class FormulaVisitor extends VoidVisitorAdapter<Stack<String>> {

        @Override
        public void visit(IntegerLiteralExpr n, Stack<String> arg) {
            super.visit(n, arg);
            arg.push(n.getValue());
        }

        @Override
        public void visit(DoubleLiteralExpr n, Stack<String> arg) {
            super.visit(n, arg);
            arg.push(n.getValue());
        }

        @Override
        public void visit(BinaryExpr n, Stack<String> arg) {
            n.getLeft().accept(this, arg);
            String left = arg.pop();
            n.getRight().accept(this, arg);
            String right = arg.pop();
            arg.push(left + n.getOperator().asString() + right);
        }

        @Override
        public void visit(NameExpr n, Stack<String> arg) {
            super.visit(n, arg);
            arg.push("\"" + n.getNameAsString() + "\"");
        }

        @Override
        public void visit(MethodCallExpr n, Stack<String> arg) {

            for (Expression expression : n.getArguments()) {
                expression.accept(this, arg);
            }

            final String mathFunction = n.getName().asString();

            if ("pow".equals(mathFunction)) {
                String exponent = arg.pop();
                String base = arg.pop();
                arg.push("(" + base + "^" + exponent + ")");

            } else  if ("sqrt".equals(mathFunction)) {
                String value = arg.pop();
                arg.push(mathFunction + "(" + value + ")");

            } else {
                throw new RuntimeException("Function '"+mathFunction+"' not supported");
            }
        }
    }

    //end::example[]
}
