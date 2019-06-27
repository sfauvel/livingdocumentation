package org.dojo.livingdoc;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClassToDocumentTest {
	private JavaSource[] sources;
	private Class<?> CLASS_TO_SEARCH = ClassToDocument.class;

	@BeforeEach
	public void getSources() {
		JavaDocBuilder builder = new JavaDocBuilder();
		builder.addSourceTree(new File("src/main/java"));
		sources = builder.getSources();
	}

	/**
	 * This test show that source files into src/main/java are present into sources object.
	 * @throws Exception
	 */
	@Test
	public void sources_contains_source_files_which_are_in_src_main_java() throws Exception {

		Optional<String> sourceFile = Arrays.stream(sources)
		        .map(source -> source.getURL().getPath())
		        .filter(path -> path.endsWith(CLASS_TO_SEARCH.getSimpleName() + ".java"))
		        .findFirst();

		assertTrue(sourceFile.isPresent());
	}

	@Test
	public void sources_contains_javaclass_from_files_which_are_in_src_main_java() throws Exception {

		Optional<JavaSource> javaSource = getJavaSourceOf(CLASS_TO_SEARCH);

		Optional<JavaClass> findJavaClass = Arrays.stream(javaSource.get().getClasses())
		        .filter(javaClass -> CLASS_TO_SEARCH.getName().equals(javaClass.getFullyQualifiedName()))
		        .findFirst();

		assertTrue(findJavaClass.isPresent());
	}

	@Test
	public void sources_contains_source_line_from_files_into_src_main_java() throws Exception {

		Optional<JavaSource> javaSource = getJavaSourceOf(CLASS_TO_SEARCH);

		String source = javaSource.get().toString();
		assertTrue(source.contains("package org.dojo.livingdoc;"));
		assertTrue(source.contains("public class ClassToDocument {"));
	}

	@Test
	public void get_comment_from_javasource() throws Exception {

		Optional<JavaSource> javaSource = getJavaSourceOf(CLASS_TO_SEARCH);
		JavaClass javaClass = javaSource.get().getClasses()[0];

		assertEquals("Class to show QDox usage.", javaClass.getComment());
	}

	@Test
	public void get_class_declaration_line_from_javasource() throws Exception {

		Optional<JavaSource> javaSource = getJavaSourceOf(CLASS_TO_SEARCH);
		JavaClass javaClass = javaSource.get().getClasses()[0];

		String[] lines = javaSource.get().toString().split("\n");
		assertEquals("public class ClassToDocument {", lines[javaClass.getLineNumber() - 1]);
	}

	@Test
	public void get_method_comment_from_javasource() throws Exception {

		Optional<JavaSource> javaSource = getJavaSourceOf(CLASS_TO_SEARCH);
		JavaClass javaClass = javaSource.get().getClasses()[0];

		JavaMethod method = javaClass.getMethodBySignature("simpleMethod", null);
		assertEquals("Simple method documented.", method.getComment());
	}

	private Optional<JavaSource> getJavaSourceOf(Class<?> classToSearch) {
		return Arrays.stream(sources)
		        .filter(source -> source.getURL().getPath().endsWith(classToSearch.getSimpleName() + ".java"))
		        .findFirst();
	}

}
