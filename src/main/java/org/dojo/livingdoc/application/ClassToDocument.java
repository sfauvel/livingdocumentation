package org.dojo.livingdoc.application;

import org.dojo.livingdoc.annotation.Functionnality;

import java.lang.annotation.Annotation;

/**
 * Class to show a javadoc extraction.
 */
public class ClassToDocument {

	/**
	 * Starting point of the application.
	 */
	public static void main(String[] args) {
		// Nothing to do
	}
	
	/**
	 * Simple method documented.
	 */
	public void simpleMethod() {
		
	}

	@Functionnality(name="Find all method with a specific annotation.")
	public void findAnnotatedMethod(Class<? extends Annotation> annotation) {

	}
}
