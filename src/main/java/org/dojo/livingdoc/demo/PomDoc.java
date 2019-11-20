package org.dojo.livingdoc.demo;

import org.dojo.livingdoc.annotation.ClassDemo;
import org.dojo.livingdoc.annotation.GenerateDoc;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Extract information from pom.xml (or a xml file).
 *
 * You may have some information stored in a XML file like the project description into the pom.xml.
 *
 * In this demo, we parse the file and display the content of the 'description' tag.
 */
@ClassDemo(group = "Static analysis", label = "Extract information from pom.xml")
public class PomDoc {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        System.out.println(new PomDoc().generatePomDescription());
    }

    @GenerateDoc(name = "Content of tag 'description' from pom.xml")
    // tag::example[]
    public String generatePomDescription()
            throws ParserConfigurationException, IOException, SAXException {

        Element root = parsePom().getDocumentElement();

        return root.getElementsByTagName("description").item(0).getTextContent();

    }

    private static Document parsePom()
            throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        return builder.parse(new File("pom.xml"));
    }
    // end::example[]
}
