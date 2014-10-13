package com.forbesdigital.junit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import org.codehaus.jackson.annotate.JsonProperty;

import static org.junit.Assert.*;

/**
 * Additional assertions for unit tests.
 *
 * @author Simon Oulevay <simon.oulevay@lotaris.com>
 */
public final class Assert {

	//<editor-fold defaultstate="collapsed" desc="Assertions on class">
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
	public static <T extends Annotation> T assertAnnotationPresentOnClass(Class<T> annotationClass, Class c) {
		return assertAnnotationPresentOnClass("Expected " + c.getName() + " to have annotation " + annotationClass.getName(), annotationClass, c);
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
	 * Asserts that the specified class has exactly the expected number of fields. 
	 * Non-static fields and inherited fields are not taken into account.
	 *
	 * @param n the expected number of fields
	 * @param c the class
	 */
	public static void assertNumberOfNonStaticFields(int n, Class c) {
		assertNumberOfNonStaticFields("Expected class " + c.getName() + " to have exactly " + n + " non-static fields", n, c);
	}

	/**
	 * Asserts that the specified class has exactly the expected number of fields. 
	 * Non-static fields and inherited fields are not taken into account.
	 *
	 * @param message the error message if the number of fields doesn't match
	 * @param n the expected number of fields
	 * @param c the class
	 */
	public static void assertNumberOfNonStaticFields(String message, int n, Class c) {
		Field[] fields = c.getDeclaredFields();
		List<Field> nonStaticFields = new ArrayList<>();
		for(Field field : fields){
			if (!Modifier.isStatic(field.getModifiers())) {
				nonStaticFields.add(field);
			}
		}
		assertEquals(message, n, nonStaticFields.size());
	}
	
	/**
	 * Asserts that the specified class has exactly the expected number of unique constraints.
	 * An array of the UniqueConstraints of the class is returned so that they can be checked individually.
	 *
	 * @param n the expected number of unique constraints
	 * @param c the class
	 * @return an array of UniqueConstraint
	 */
	public static UniqueConstraint[] assertNumberOfUniqueConstraintsOnClass(int n, Class c) {
		Table tableAnnotation = assertAnnotationPresentOnClass(Table.class, c);
		UniqueConstraint[] constraints = tableAnnotation.uniqueConstraints();
		assertEquals("Expected class " + c.getName() + " to have exactly " + n + " unique constraints.", n, constraints.length);
		return constraints;
	}
	
	/**
	 * Asserts that the specified unique constraint has exactly the expected column names in the correct order.
	 *
	 * @param uniqueConstraint the UniqueConstraint
	 * @param expectedGroups the expected column names in the correct order
	 */
	public static void assertUniqueConstraintColumnNames(UniqueConstraint uniqueConstraint, String... expectedNames) {
		assertTrue(Arrays.equals(uniqueConstraint.columnNames(), expectedNames));
	}
	//</editor-fold>

	//<editor-fold defaultstate="collapsed" desc="Assertions on fields">
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
	 * @param nullable whether the database column is nullable, by default true
	 * @param name the name of the column, by default ""
	 * @param length the column length, by default 255
	 * @param unique whether the column is a unique key, by default false
	 */
	public static void assertColumnAnnotation(Class c, String field, Boolean nullable, String name, Integer length, Boolean unique) {
		assertColumnAnnotation(c, field, nullable, name, length, unique, null);
	}
	
	/**
	 * Asserts that the specified field on a class is annotated with the Column annotation, 
	 * with the specified parameters.
	 * 
	 * @param c the class
	 * @param field the name of the field to check
	 * @param nullable whether the database column is nullable, by default true
	 * @param name the name of the column, by default ""
	 * @param length the column length, by default 255
	 * @param unique whether the column is a unique key, by default false
	 * @param columnDefinition the columnDefinition of the column, by default ""
	 */
	public static void assertColumnAnnotation(Class c, String field, Boolean nullable, String name, Integer length, Boolean unique, String columnDefinition) {
		Column annotation = assertAnnotationPresentOnField(Column.class, c, field);
		assertEquals(nullable != null ? nullable : true, annotation.nullable());
		assertEquals(name != null ? name : "", annotation.name());
		assertEquals(length != null ? length : 255, annotation.length());
		assertEquals(unique != null ? unique : false, annotation.unique());
		assertEquals(columnDefinition != null ? columnDefinition : "", annotation.columnDefinition());
		
		// the following parameters should not be used and keep their default values
		assertEquals(true, annotation.insertable());
		assertEquals(0, annotation.precision());
		assertEquals(0, annotation.scale());
		assertEquals("", annotation.table());
		assertEquals(true, annotation.updatable());
	}

	/**
	 * Asserts that the specified field on a class is annotated with the Enumerated annotation, 
	 * with the specified parameters.
	 * 
	 * @param c the class
	 * @param field the name of the field to check
	 * @param enumType the enumeration type, by default EnumType.ORDINAL
	 */
	public static void assertEnumeratedAnnotation(Class c, String field, EnumType enumType) {
		Enumerated annotation = assertAnnotationPresentOnField(Enumerated.class, c, field);
		assertEquals(enumType != null ? enumType : EnumType.ORDINAL, annotation.value());
	}

	/**
	 * Asserts that the specified field on a class is annotated with the Temporal annotation, 
	 * with the specified parameters.
	 * 
	 * @param c the class
	 * @param field the name of the field to check
	 * @param temporalType the temporal type
	 */
	public static void assertTemporalAnnotation(Class c, String field, TemporalType temporalType) {
		Temporal annotation = assertAnnotationPresentOnField(Temporal.class, c, field);
		assertEquals(temporalType, annotation.value());
	}
	
	/**
	 * Asserts that the specified field on a class is annotated with the ElementCollection annotation, 
	 * with the specified parameters.
	 * 
	 * @param c the class
	 * @param field the name of the field to check
	 * @param targetClass the targetClass, by default void.class 
	 * @param fetchType the fetch type, by default EnumType.LAZY
	 */
	public static void assertElementCollectionAnnotation(Class c, String field, Class targetClass, FetchType fetchType) {
		ElementCollection annotation = assertAnnotationPresentOnField(ElementCollection.class, c, field);
		assertEquals(targetClass != null ? targetClass : void.class, annotation.targetClass());
		assertEquals(fetchType != null ? fetchType : FetchType.LAZY, annotation.fetch());
	}
	
	/**
	 * Asserts that the specified field on a class is annotated with the OneToOne annotation, 
	 * with the specified parameters.
	 * 
	 * @param c the class
	 * @param field the name of the field to check
	 * @param cascadeTypes the operations that must be cascaded to the target of the association, by default {}
	 * @param fetchType whether the association should be lazily loaded or must be eagerly fetched, by default FetchType.EAGER
	 * @param mappedBy the field that owns the relationship, by default ""
	 * @param optional whether the association is optional, by default true
	 */
	public static void assertOneToOneAnnotation(Class c, String field, CascadeType[] cascadeTypes, FetchType fetchType, String mappedBy, Boolean optional) {
		OneToOne annotation = assertAnnotationPresentOnField(OneToOne.class, c, field);
		assertTrue(Arrays.equals(annotation.cascade(), cascadeTypes != null ? cascadeTypes : new CascadeType[]{}));
		assertEquals(fetchType != null ? fetchType : FetchType.EAGER, annotation.fetch());
		assertEquals(mappedBy != null ? mappedBy : "", annotation.mappedBy());
		assertEquals(optional != null ? optional : true, annotation.optional());
		
		// the following parameters should not be used and keep their default values
		assertEquals(false, annotation.orphanRemoval());
		assertEquals(void.class, annotation.targetEntity());
	}
	
	/**
	 * Asserts that the specified field on a class is annotated with the OneToMany annotation, 
	 * with the specified parameters.
	 * 
	 * @param c the class
	 * @param field the name of the field to check
	 * @param cascadeTypes the operations that must be cascaded to the target of the association
	 * @param fetchTypes whether the association should be lazily loaded or must be eagerly fetched
	 * @param mappedBy the field that owns the relationship
	 */
	public static void assertOneToManyAnnotation(Class c, String field, CascadeType[] cascadeTypes, FetchType fetchTypes, String mappedBy) {
		OneToMany annotation = assertAnnotationPresentOnField(OneToMany.class, c, field);
		assertTrue(Arrays.equals(annotation.cascade(), cascadeTypes));
		assertEquals(fetchTypes, annotation.fetch());
		assertEquals(mappedBy, annotation.mappedBy());

		// the following parameters should not be used and keep their default values
		assertEquals(false, annotation.orphanRemoval());
		assertEquals(void.class, annotation.targetEntity());
	}
	
	/**
	 * Asserts that the specified field on a class is annotated with the ManyToOne annotation, 
	 * with the specified parameters.
	 * 
	 * @param c the class
	 * @param field the name of the field to check
	 * @param cascadeTypes the operations that must be cascaded to the target of the association
	 * @param fetchTypes whether the association should be lazily loaded or must be eagerly fetched
	 * @param optional whether the association is optional
	 */
	public static void assertManyToOneAnnotation(Class c, String field, CascadeType[] cascadeTypes, FetchType fetchTypes, boolean optional) {
		ManyToOne annotation = assertAnnotationPresentOnField(ManyToOne.class, c, field);
		assertTrue(Arrays.equals(annotation.cascade(), cascadeTypes));
		assertEquals(fetchTypes, annotation.fetch());
		assertEquals(optional, annotation.optional());
		
		// the following parameters should not be used and keep their default values
		assertEquals(void.class, annotation.targetEntity());
	}
	
	/**
	 * Asserts that the specified field on a class is annotated with the CollectionTable annotation, 
	 * with the specified parameters.
	 * 
	 * @param c the class
	 * @param field the name of the field to check
	 * @param name the name of the collection table
	 * @param joinColumnNames array of name for the join columns
	 * @param constraintColumnNames array of constraint names
	 */
	public static void assertCollectionTableAnnotation(Class c, String field, String name, String[] joinColumnNames, String[] constraintColumnNames) {
		CollectionTable annotation = assertAnnotationPresentOnField(CollectionTable.class, c, field);
		
		assertEquals(annotation.joinColumns().length, joinColumnNames.length);
		
		for (int i = 0 ; i < joinColumnNames.length ; i++) {
			assertEquals(joinColumnNames[i], annotation.joinColumns()[i].name());
			
			// Join column should never be null for a collection table
			assertEquals("JoinColumn should not be nullable for a CollectionTable", false, annotation.joinColumns()[i].nullable());
		}
		
		// There should be only one constraint for a collection table
		assertEquals("Only one UniqueConstraint should exists for a CollectionTalble", 1, annotation.uniqueConstraints().length);
		
		// Constraints name size should match
		assertEquals(constraintColumnNames.length, annotation.uniqueConstraints()[0].columnNames().length);
		
		// Assert all constraints name
		for (int i = 0 ; i < constraintColumnNames.length ; i++) {
			assertEquals(constraintColumnNames[i], annotation.uniqueConstraints()[0].columnNames()[i]);
			
			// use default value for the constraint name
			assertEquals("UniqueConstraint should use the default name", "", annotation.uniqueConstraints()[0].name());
			
		}
		
		assertEquals(name, annotation.name());
		
		// the following parameters should not be used and keep their default values
		assertEquals("", annotation.schema());
		assertEquals("", annotation.catalog());
		assertEquals(0, annotation.indexes().length);
		
	}
	
	/**
	 * Asserts that the specified field on a class is annotated with the Column annotation, 
	 * with the specified parameters.
	 * 
	 * @param c the class
	 * @param field the name of the field to check
	 * @param nullable whether the database column is nullable, by default true
	 * @param name the name of the column, by default ""
	 * @param unique whether the column is a unique key, by default false
	 */
	public static void assertJoinColumnAnnotation(Class c, String field, Boolean nullable, String name,  Boolean unique) {
		JoinColumn annotation = assertAnnotationPresentOnField(JoinColumn.class, c, field);
		assertEquals(name != null ? name : "", annotation.name());
		assertEquals(nullable != null ? nullable : true, annotation.nullable());
		assertEquals(unique != null ? unique : false, annotation.unique());

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
	//</editor-fold>
	
	//<editor-fold defaultstate="collapsed" desc="Hidden Constructor">
	private Assert() {
	}
	//</editor-fold>
}
