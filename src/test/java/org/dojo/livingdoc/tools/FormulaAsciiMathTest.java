package org.dojo.livingdoc.tools;

import org.dojo.livingdoc.tools.FormulaAsciiMath;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FormulaAsciiMathTest {

    @Test
    public void should_parse_number() {
        assertEquals("5", javaToAsciiMath("5;"));
        assertEquals("1.5", javaToAsciiMath("1.5;"));
    }

    @Test
    public void should_parse_addition() {
        assertEquals("5+3", javaToAsciiMath("5 + 3;"));
    }

    @Test
    public void should_parse_sqrt() {
        assertEquals("sqrt(5)", javaToAsciiMath("Math.sqrt(5);"));
    }

    @Test
    public void should_parse_pow() {
        assertEquals("(5^2)", javaToAsciiMath("Math.pow(5, 2);"));
    }

    @Test
    public void should_parse_variable() {
        assertEquals("\"value\"", javaToAsciiMath("value;"));
    }

    @Test
    public void should_parse_complex_formula() {
        assertEquals("(1.55^\"level\")+sqrt(12*\"level\")+50", javaToAsciiMath("Math.pow(1.55, level) + Math.sqrt(12*level) + 50;"));
    }

    private String javaToAsciiMath(String javaFormula) {
        return FormulaAsciiMath.fromJava(javaFormula);
    }


}
