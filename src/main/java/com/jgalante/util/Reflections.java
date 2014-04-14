package com.jgalante.util;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Reflections {
	
	/**
	 * Return the parametized type used with a concrete implementation of
	 * a class that accepts generics.
	 * 
	 * Ex: If you declare
	 * <pre><code>
	 * public class SpecializedCollection implements Collection<SpecializedType> {
	 *   // ...
	 * }
	 * </code></pre>
	 * 
	 * then the code <code>getGenericTypeArgument(SpecializedCollection.class , 0);</code> will
	 * return the type <code>SpecializedType</code>.
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getGenericTypeArgument(final Class<?> clazz, final int idx) {
		final Type type = clazz.getGenericSuperclass();

		ParameterizedType paramType;
		try {
			paramType = (ParameterizedType) type;
		} catch (ClassCastException cause) {
			return getGenericTypeArgument((Class<T>) type, idx);
		}

		return (Class<T>) paramType.getActualTypeArguments()[idx];
	}

	@SuppressWarnings("unchecked")
	public static <T> Class<T> getGenericTypeArgument(final Field field, final int idx) {
		final Type type = field.getGenericType();
		final ParameterizedType paramType = (ParameterizedType) type;
		
		return (Class<T>) paramType.getActualTypeArguments()[idx];
	}

	/**
	 * <p>Return the parametized type passed to members (fields or methods) that accepts Generics.</p>
	 * 
	 * @see #getGenericTypeArgument(Field field, int idx)
	 * 
	 */
	public static <T> Class<T> getGenericTypeArgument(final Member member, final int idx) {
		Class<T> result = null;

		if (member instanceof Field) {
			result = getGenericTypeArgument((Field) member, idx);
		} else if (member instanceof Method) {
			result = getGenericTypeArgument((Method) member, idx);
		}

		return result;
	}

	/**
	 * <p>Return the parametized type passed to methods that accepts Generics.</p>
	 * 
	 * @see #getGenericTypeArgument(Field field, int idx)
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getGenericTypeArgument(final Method method, final int pos) {
		return (Class<T>) method.getGenericParameterTypes()[pos];
	}
}
