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
}