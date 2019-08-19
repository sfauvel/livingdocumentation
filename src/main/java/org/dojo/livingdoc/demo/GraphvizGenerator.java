package org.dojo.livingdoc.demo;

import org.dojo.livingdoc.annotation.GenerateGraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GraphvizGenerator {
    static private class Link {

        private final String from;
        private final String to;

        public Link(String from, String to) {
            this.from = from;
            this.to = to;
        }
    }
    List<Link> links = new ArrayList<>();

    public String generate() {
        return "\n[graphviz]\n" +
                "----\n" +
                "digraph g {\n" +
                links.stream()
                        .map(link -> link.from + " -> " + link.to + "\n")
                        .collect(Collectors.joining())+
                "}\n" +
                "----\n"
        ;
    }

    public GraphvizGenerator addLink(String from, String to) {
        return addLink(new Link(from, to));
    }

    private GraphvizGenerator addLink(Link link) {
        links.add(link);
        return this;
    }
}
