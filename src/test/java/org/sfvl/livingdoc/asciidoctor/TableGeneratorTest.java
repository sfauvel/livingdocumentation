package org.sfvl.livingdoc.asciidoctor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TableGeneratorTest {


    @Test
    public void should_create_a_table_with_one_line() {
        final String asciidoc = new TableGenerator()
                .withData(List.of(
                        List.of("A", "B", "C")
                ))
                .generate();

        assertEquals(String.join("\n",
                "|====",
                "| A | B | C",
                "|===="
                )
                ,asciidoc);
    }

    @Test
    public void should_create_a_table_with_multi_lines() {
        final String asciidoc = new TableGenerator()
                .withData(List.of(
                        List.of("A", "B", "C"),
                        List.of("a", "b", "c"),
                        List.of("x", "y", "z")
                ))
                .generate();

        assertEquals(String.join("\n",
                "|====",
                "| A | B | C",
                "| a | b | c",
                "| x | y | z",
                "|===="
                )
                ,asciidoc);
    }

    @Test
    public void should_create_a_table_with_header() {
        final String asciidoc = new TableGenerator()
                .withHeader(List.of("A", "B", "C"))
                .withData(List.of(
                        List.of("a", "b", "c")
                ))
                .generate();

        assertEquals(String.join("\n",
                "|====",
                "| A | B | C",
                "",
                "| a | b | c",
                "|===="
                )
                ,asciidoc);
    }

    @Test
    public void should_specify_auto_column_size() {
        final String asciidoc = new TableGenerator()
                .with(new TableGenerator.ColumnGenerator().autowidth())
                .withData(List.of(
                        List.of("A", "B", "C")
                ))
                .generate();

        assertEquals(String.join("\n",
                "|====",
                "[%autowidth]",
                "| A | B | C",
                "|===="
                )
                ,asciidoc);
    }
}