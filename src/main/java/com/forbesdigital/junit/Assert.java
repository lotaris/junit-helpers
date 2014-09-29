package com.forbesdigital.junit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import org.codehaus.jackson.annotate.JsonProperty;

import static org.junit.Assert.*;

/**
 * Additional assertions for unit tests.
 *
 * @author Simon Oulevay <simon.oulevay@lotaris.com>
 */
public final class Assert {

	/**
	 * Asserts that an Exception of the class (or subclass) is thrown.
	 *
	 * @param exceptionClass the expected Exception class.
	 * @param call the Runnable code which should throw the exception.
	 */
	public static void assertExceptionThrown(Class<? extends Exception> exceptionClass, Runnable call) {
		try {
			call.run();
		} catch (Exception e) {
			assertTrue("Expected an exception of type " + exceptionClass.getName() + " (or a subclass) to have been thrown", exceptionClass.isAssignableFrom(e.getClass()));
		}
	}

	/**
	 * Asserts that the specified class is annotated with the specified annotation.
	 *
	 * <p>
	 * The annotation instance is returned so that its values can be checked.
	 *
	 * @param <T> the annotation type
	 * @param annotationClass the annotation class
	 * @param c the class to check
	 * @return the annotation instance
	 */
	public static void assertAnnotationPresentOnClass(Class<? extends Annotation> annotationClass, Class c) {
		assertAnnotationPresentOnClass("Expected " + c.getName() + " to have annotation " + annotationClass.getName(), annotationClass, c);
	}

	/**
	 * Asserts that the specified class is annotated with the specified annotation.
	 *
	 * <p>
	 * The annotation instance is returned so that its values can be checked.
	 *
	 * @param <T> the annotation type
	 * @param message the error message if the annotation is not found on the field
	 * @param annotationClass the annotation class
	 * @param c the class to check
	 * @return the annotation instance
	 */
	public static <T extends Annotation> T assertAnnotationPresentOnClass(String message, Class<T> annotationClass, Class c) {
		assertTrue(message, c.isAnnotationPresent(annotationClass));
		return (T) c.getAnnotation(annotationClass);
	}

	/**
	 * Asserts that the specified class has exactly the expected number of annotations.
	 *
	 * @param n the expected number of annotations
	 * @param c the class to check
	 */
	public static void assertNumberOfAnnotationsOnClassEquals(int n, Class c) {
		assertNumberOfAnnotationsOnClassEquals("Expected class " + c.getName() + " to have exactly " + n + " annotations", n, c);
	}

	/**
	 * Asserts that the specified class has exactly the expected number of annotations.
	 *
	 * @param message the error message if the number of annotations doesn't match
	 * @param n the expected number of annotations
	 * @param c the class to check
	 */
	public static void assertNumberOfAnnotationsOnClassEquals(String message, int n, Class c) {
		assertEquals(message, n, c.getAnnotations().length);
	}

	/**
	 * Asserts that the specified field on a class is annotated with the specified
	 * annotation. An assertion will also be made that the class has that field.
	 *
	 * <p>
	 * The annotation instance is returned so that its values can be checked.
	 *
	 * @param <T> the annotation type
	 * @param annotationClass the annotation class
	 * @param c the class
	 * @param field the name of the field to check
	 * @return the annotation instance
	 */
	public static <T extends Annotation> T assertAnnotationPresentOnField(Class<T> annotationClass, Class c, String field) {
		return assertAnnotationPresentOnField("Expected " + c.getName() + " to have annotation " + annotationClass.getName() + " on field " + field, annotationClass, c, field);
	}

	/**
	 * Asserts that the specified field on a class is annotated with the specified
	 * annotation. An assertion will also be made that the class has that field.
	 *
	 * <p>
	 * The annotation instance is returned so that its values can be checked.
	 *
	 * @param <T> the annotation type
	 * @param message the error message if the annotation is not found on the field
	 * @param annotationClass the annotation class
	 * @param c the class
	 * @param field the name of the field to check
	 * @return the annotation instance
	 */
	public static <T extends Annotation> T assertAnnotationPresentOnField(String message, Class<T> annotationClass, Class c, String field) {
		final Field f = ClassUtils.getDeclaredField(c, field);
		assertNotNull("Expected class " + c.getName() + " to have field " + field, f);
		assertTrue(message, f.isAnnotationPresent(annotationClass));
		return f.getAnnotation(annotationClass);
	}

	/**
	 * Asserts that the specified field on a class has exactly the expected number
	 * of annotations. An assertion will also be made that the class has that
	 * field.
	 *
	 * @param n the expected number of annotations
	 * @param c the class
	 * @param field the name of the field to check
	 */
	public static void assertNumberOfAnnotationsOnFieldEquals(int n, Class c, String field) {
		assertNumberOfAnnotationsOnFieldEquals("Expected class " + c.getName() + " to have exactly " + n + " annotations", n, c, field);
	}

	/**
	 * Asserts that the specified field on a class has exactly the expected number
	 * of annotations. An assertion will also be made that the class has that
	 * field.
	 *
	 * @param message the error message if the number of annotations doesn't match
	 * @param n the expected number of annotations
	 * @param c the class
	 * @param field the name of the field to check
	 */
	public static void assertNumberOfAnnotationsOnFieldEquals(String message, int n, Class c, String field) {
		final Field f = ClassUtils.getDeclaredField(c, field);
		assertNotNull("Expected class " + c.getName() + " to have field " + field, f);
		assertEquals(message, n, f.getAnnotations().length);
	}

	/**
	 * Asserts that the specified field on a class is annotated with the JsonProperty annotation.
	 * The annotation should have the field name as its value. If you need to test an annotation
	 * whose value is different than the field name, use
	 * {@link #assertJsonPropertyAnnotation(java.lang.Class, java.lang.String, java.lang.String)}.
	 *
	 * @param c the class
	 * @param field the name of the field to check
	 */
	public static void assertJsonPropertyAnnotation(Class c, String field) {
		JsonProperty jsonPropertAnnotation = assertAnnotationPresentOnField(JsonProperty.class, c, field);
		assertEquals(field, jsonPropertAnnotation.value());
	}

	/**
	 * Asserts that the specified field on a class is annotated with the JsonProperty annotation.
	 *
	 * @param c the class
	 * @param field the name of the field to check
	 * @param value the value the annotation should have
	 */
	public static void assertJsonPropertyAnnotation(Class c, String field, String value) {
		JsonProperty jsonPropertAnnotation = assertAnnotationPresentOnField(JsonProperty.class, c, field);
		assertEquals(field, jsonPropertAnnotation.value());
	}

	/**
	 * Asserts that the specified array of validation groups contains exactly the expected validation groups in the correct order.
	 *
	 * <p><pre>
	 * assertValidationGroups(annotation.groups(), Default.class, ValidationGroups.Create.class);
	 * </pre>
	 *
	 * @param actualGroups the actual groups (e.g. the "groups" property of the bean validation annotation)
	 * @param expectedGroups the expected groups (e.g. the expected validation group classes in the correct order
	 */
	public static void assertValidationGroups(Class[] actualGroups, Class... expectedGroups) {
		assertTrue(Arrays.equals(actualGroups, expectedGroups));
	}

	/**
	 * Asserts that the specified field on a class is annotated with the Column annotation, 
	 * with the specified parameters.
	 * 
	 * @param c the class
	 * @param field the name of the field to check
	 * @param nullable whether the database column is nullable
	 * @param name the name of the column
	 * @param length the column length
	 * @param unique whether the column is a unique key
	 */
	public static void assertColumnAnnotation(Class c, String field, boolean nullable, String name, int length, boolean unique) {
		Column annotation = assertAnnotationPresentOnField(Column.class, c, field);
		assertEquals(nullable, annotation.nullable());
		assertEquals(name, annotation.name());
		assertEquals(length, annotation.length());
		assertEquals(unique, annotation.unique());
		
		// the following parameters should not be used and keep their default values
		assertEquals("", annotation.columnDefinition());
		assertEquals(true, annotation.insertable());
		assertEquals(0, annotation.precision());
		assertEquals(0, annotation.scale());
		assertEquals("", annotation.table());
		assertEquals(true, annotation.updatable());
	}
	
	/**
	 * Asserts that the specified field on a class is annotated with the OneToOne annotation, 
	 * with the specified parameters.
	 * 
	 * @param c the class
	 * @param field the name of the field to check
	 * @param cascadeTypes the operations that must be cascaded to the target of the association
	 * @param fetchTypes whether the association should be lazily loaded or must be eagerly fetched
	 * @param mappedBy the field that owns the relationship
	 * @param optional whether the association is optional
	 */
	public static void assertOneToOneAnnotation(Class c, String field, CascadeType[] cascadeTypes, FetchType fetchTypes, String mappedBy, boolean optional) {
		OneToOne annotation = assertAnnotationPresentOnField(OneToOne.class, c, field);
		assertTrue(Arrays.equals(annotation.cascade(), cascadeTypes));
		assertEquals(fetchTypes, annotation.fetch());
		assertEquals(mappedBy, annotation.mappedBy());
		assertEquals(optional, annotation.optional());
		
		// the following parameters should not be used and keep their default values
		assertEquals(false, annotation.orphanRemoval());
		assertEquals(void.class, annotation.targetEntity());
	}
	
	/**
	 * Asserts that the specified field on a class is annotated with the Column annotation, 
	 * with the specified parameters.
	 * 
	 * @param c the class
	 * @param field the name of the field to check
	 * @param nullable whether the database column is nullable
	 * @param name the name of the column
	 * @param unique whether the column is a unique key
	 */
	public static void assertJoinColumnAnnotation(Class c, String field, boolean nullable, String name,  boolean unique) {
		JoinColumn annotation = assertAnnotationPresentOnField(JoinColumn.class, c, field);
		assertEquals(name, annotation.name());
		assertEquals(nullable, annotation.nullable());
		assertEquals(unique, annotation.unique());
		
		// the following parameters should not be used and keep their default values
		assertEquals("", annotation.columnDefinition());
		// TODO:
		// assertEquals(TODO, annotation.foreignKey());
		assertEquals(true, annotation.insertable());
		assertEquals("", annotation.referencedColumnName());
		assertEquals("", annotation.table());
		assertEquals(true, annotation.updatable());
	}
	
	/**
	 * Asserts that the id field on a class is annotated properly.
	 * 
	 * @param c the class
	 */
	public static void assertIdAnnotations(Class c) {
		assertNumberOfAnnotationsOnFieldEquals(2, c, "id");
		
		assertAnnotationPresentOnField(Id.class, c, "id");
		
		GeneratedValue annotation = assertAnnotationPresentOnField(GeneratedValue.class, c, "id");
		assertEquals(GenerationType.IDENTITY, annotation.strategy());
		assertEquals("", annotation.generator());
	}
	
	/**
	 * Asserts that the key field on a class is annotated properly.
	 * 
	 * @param c the class
	 */
	public static void assertKeyAnnotations(Class c) {
		assertNumberOfAnnotationsOnFieldEquals(1, c, "key");
		
		assertColumnAnnotation(c, "key", false, "KEE", 10, true);
	}

	//<editor-fold defaultstate="collapsed" desc="Hidden Constructor">
	private Assert() {
	}
	//</editor-fold>
}
