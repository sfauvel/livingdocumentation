package org.dojo.livingdoc.demo;

import org.dojo.livingdoc.annotation.ClassDemo;
import org.dojo.livingdoc.annotation.Glossary;
import org.reflections.Reflections;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static java.util.stream.Collectors.joining;

/**
 * Extract information from pom.xml (file xml)
 */
@ClassDemo(label = "Pom.xml demo")
public class PomDoc {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        System.out.println(generatePomDescription());
    }

    // tag::example[]
    private static String generatePomDescription() throws ParserConfigurationException, IOException, SAXException {

        Element root = parsePom().getDocumentElement();

        return root.getElementsByTagName("description").item(0).getTextContent();

    }

    private static Document parsePom() throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        return builder.parse(new File("pom.xml"));
    }
    // end::example[]
}
