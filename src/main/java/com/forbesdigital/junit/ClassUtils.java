package com.forbesdigital.junit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Class utilities for testing.
 *
 * @author Simon Oulevay <simon.oulevay@lotaris.com>
 */
public final class ClassUtils {

	/**
	 * Returns the specified field of the specified class.
	 *
	 * @param c the class to take the field from
	 * @param field the name of the field
	 * @return a field, or null if the class has no such field
	 * @throws NullPointerException if the class is null
	 */
	public static Field getDeclaredField(Class c, String field) {
		try {
			return c.getDeclaredField(field);
		} catch (NoSuchFieldException nsfe) {
			return null;
		}
	}

	/**
	 * Returns the specific annotation on the specified class.
	 *
	 * @param <T> the annotation type
	 * @param c the class to take the annotation from
	 * @param annotationClass the annotation class
	 * @return the annotation, or null if the class has no such annotation
	 */
	public static <T extends Annotation> T getAnnotation(Class c, Class<T> annotationClass) {
		return (T) c.getAnnotation(annotationClass);
	}

	/**
	 * Returns the specified annotation on the specified field of a class.
	 *
	 * @param <T> the annotation type
	 * @param c the class to take the annotation from
	 * @param field the name of the field
	 * @param annotationClass the annotation class
	 * @return the annotation, or null if the class has no such field or the field has no such annotation
	 */
	public static <T extends Annotation> T getAnnotation(Class c, String field, Class<T> annotationClass) {
		final Field f = getDeclaredField(c, field);
		return f != null ? f.getAnnotation(annotationClass) : null;
	}

	//<editor-fold defaultstate="collapsed" desc="Hidden Constructor">
	public ClassUtils() {
	}
	//</editor-fold>
}
