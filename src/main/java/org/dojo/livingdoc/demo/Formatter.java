package org.dojo.livingdoc.demo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface Formatter {

    String title(int index, String title);

    String description(String description);

    String paragraph(String content);

    String tableOfContent();

    String addDefinition(String key, String description);

    String listItem(String text);

    String listItems(String... texts);

    String sourceCode(String source);

    String startDocument(String title);

    String include(String filename, String options);

    String warning(String message);

    String section(String name, String message);

    String link(String id);

    String anchorLink(String id, String visibleText);

    String table(List<List<? extends Object>> data);

    String image(String filename);

    public static class AsciidoctorFormatter implements Formatter {

        @Override
        public String title(int index, String title) {
            return String.format(
                    "\n%s %s\n",
                    new String(new char[index]).replace('\0', '='),
                    title);
        }

        @Override
        public String description(String description) {
            return description;
        }

        @Override
        public String paragraph(String content) {
            return content;
        }

        @Override
        public String tableOfContent() {
            return ":toc:\n:toclevels: 4\n";
        }

        @Override
        public String addDefinition(String key, String description) {
            return String.format("\n%s:: %s\n", key, (description.isEmpty()?"\n+":description));
        }


        @Override
        public String listItem(String text) {
            return "\n* " + text;
        }

        @Override public String listItems(String... texts) {

            return texts.length==0
                    ? ""
                    : Arrays.stream(texts).collect(Collectors.joining("\n"));
        }

        @Override public String sourceCode(String source) {
            return block("----", ".highlight,indent=0", source);
        }
        @Override public String startDocument(String title) {
            return String.format("= %s\n:toc: left\n:toclevels: 3\n:sectlinks:\n:source-highlighter: coderay", title);
        }
        @Override public String include(String filename, String options) {
            return String.format("\ninclude::%s[%s]\n", filename, options);
        }

        @Override public String warning(String message) {
            return block("====", "WARNING", message);
        }

        @Override public String section(String name, String message) {
            return block("--", name, message);
        }

        @Override public String link(String id) {
            return "[[" + formatLink(id) + "]]";
        }

        @Override public String anchorLink(String id, String visibleText) {
            return "<<" +
                    formatLink(id) +
                    ((visibleText.isEmpty())?"":","+visibleText)+
                    ">>";
        }

        @Override public String table(List<List<? extends Object>> data) {
            return "\n|====\n" +
                    data.stream().map(line -> {
                return line.stream().map(Object::toString).collect(Collectors.joining("|","|", "\n"));
            }).collect(Collectors.joining()) +
                    "|====\n";
        }

        @Override public String image(String filename) {
            return String.format("\nimage::%s[]\n", filename);
        }

        private String block(String delimiter, String name, String message) {
            return String.format("\n[%s]\n%s\n%s\n%s\n", name, delimiter, message, delimiter);
        }

        private String formatLink(String id) {
            return id.replaceAll("[\\.$\\: #]", "_").toLowerCase();
        }
    }

}
