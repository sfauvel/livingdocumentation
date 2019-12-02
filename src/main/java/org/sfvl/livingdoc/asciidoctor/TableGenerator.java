package org.sfvl.livingdoc.asciidoctor;

import org.jruby.javasupport.ext.JavaUtil;

import java.util.List;
import java.util.stream.Collectors;

public class TableGenerator {
    private List<List<String>> data;

    public TableGenerator withData(List<List<String>> data) {
        this.data = data;
        return this;
    }

    public String generate() {
        return String.join("\n",
                "|====",
                generateLine(data.get(0)),
                "|====");
    }

    private String generateLine(List<String> lineData) {
        return lineData.stream().collect(Collectors.joining(" | ", "| ", ""));
    }
}
