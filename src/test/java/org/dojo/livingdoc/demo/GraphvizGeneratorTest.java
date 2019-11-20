package org.dojo.livingdoc.demo;

import org.dojo.livingdoc.tools.GraphvizGenerator;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GraphvizGeneratorTest {

    GraphvizGenerator generator = new GraphvizGenerator();

    @Test
    public void should_generate_an_empty_graph() {

        assertTextEquals(Arrays.asList(
                "",
                "[graphviz]",
                "----",
                "digraph g {",
                "}",
                "----",
                ""
        ), generator.generate());
    }


    @Test
    public void should_generate_an_simple_link() {

        generator.addLink("A", "B")
            .addLink("C", "D");


        assertTextContains(Arrays.asList(
                "{",
                "A -> B",
                "C -> D",
                "}"
        ), generator.generate());
    }

    private void assertTextEquals(List<String> expected, String actual) {
        assertEquals(
                expected.stream().collect(Collectors.joining("\n")),
                actual);

    }

    private void assertTextContains(List<String> expected, String actual) {
        String expectedFormatted = expected.stream().collect(Collectors.joining("\n"));
        assertTrue(
                actual.contains(expectedFormatted),
                actual + "\ndoes not contains\n" + expectedFormatted
                );

    }
}
