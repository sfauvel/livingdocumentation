package org.sfvl.livingdoc.asciidoctor;

import org.dojo.livingdoc.annotation.GenerateDoc;
import org.jruby.javasupport.ext.JavaUtil;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TableGenerator implements Generator {

    public TableGenerator with(ColumnGenerator columnGenerator) {
        this.columnGenerator = columnGenerator;
        return this;
    }

    public TableGenerator withHeader(List<String> header) {
        this.header = header;
        return this;
    }

    public static class ColumnGenerator implements Generator {

        private boolean autowidth;

        @Override
        public String generate() {
            return autowidth ? "[%autowidth]" : "";
        }

        public ColumnGenerator autowidth() {
            this.autowidth = true;
            return this;
        }
    }

    private List<String> header;
    private ColumnGenerator columnGenerator = new ColumnGenerator();
    private List<List<String>> data;

    public TableGenerator withData(List<List<String>> data) {
        this.data = data;
        return this;
    }

    public String generate() {
        return joinLines(
                "|====",
                generateHeader(),
                columnGenerator.generate(),
                data.stream().map(this::generateLine).collect(Collectors.joining("\n")),
                "|====");
    }

    private String generateHeader() {
        if (header == null) {
            return "";
        }
        return generateLine(header) + "\n";
    }

    private String joinLines(String... lines) {
        return Arrays.stream(lines)
                .filter(Predicate.not(String::isEmpty))
                .collect(Collectors.joining("\n"));
    }

    private String generateLine(List<String> lineData) {
        return lineData.stream().collect(Collectors.joining(" | ", "| ", ""));
    }
}
