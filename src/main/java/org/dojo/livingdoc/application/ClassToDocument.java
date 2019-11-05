package org.dojo.livingdoc.application;

import org.dojo.livingdoc.annotation.Functionnality;

/**
 * Class to show QDox usage.
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

	@Functionnality(name="Living Documentation")
	public void functionnalityToDocument() {

	}
}
