package com.jgalante.util;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.convert.Converter;
import javax.servlet.http.HttpServletRequest;

public class Faces {
	
	public static Object convert(final String value, final Converter converter) {
		Object result = null;

		if (!isEmpty(value)) {
			if (converter != null) {
				result = converter.getAsObject(getFacesContext(), getFacesContext().getViewRoot(), value);
			} else {
				result = new String(value);
			}
		}

		return result;
	}
	
	public static Converter getConverter(Class<?> targetClass) {
		Converter result;

		try {
			Application application = getFacesContext().getApplication();
			result = application.createConverter(targetClass);

		} catch (Exception e) {
			result = null;
		}

		return result;
	}
	
	public static HttpServletRequest getRequest() {
		return (HttpServletRequest) getFacesContext().getExternalContext().getRequest();
	}
	
	public static Flash getFlash() {
		return getFacesContext().getExternalContext().getFlash();
	}
	
	public static void putFlashMap(String key, Object value) {
		Flash flash = Faces.getFlash();
		flash.put(key, value);
	}
	
	public static void putFlashMap(String key, String value, Converter converter) {
		Flash flash = Faces.getFlash();
		flash.put(key, Faces.convert(value, converter));
	}
	
	public static Object getFlashMap(String key) {
		Object value = null;
		
		Flash flash = Faces.getFlash();
		if (flash.containsKey(key)) {
			value = flash.get(key);
			flash.keep(key);
		}
		
		return value;
	}

	public static FacesContext getFacesContext() {
		return (FacesContext) FacesContext.getCurrentInstance();
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

	public static boolean isEmpty(final String value) {
		return value == null || value.trim().isEmpty();
	}
}
