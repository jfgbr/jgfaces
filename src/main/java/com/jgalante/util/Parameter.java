package com.jgalante.util;

import java.io.Serializable;

import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import com.jgalante.annotation.Param;

@Param
public class Parameter<T extends Object> implements Serializable {

	private static final long serialVersionUID = 1L;

	private T value;

	private  String key;

	private boolean flashScope = false;

	private Class<?> type;
	
	@Inject
	public Parameter(InjectionPoint ip) {
		String key = ip.getAnnotated().getAnnotation(Param.class).value();
		flashScope = "flash".equals(ip.getAnnotated().getAnnotation(Param.class).scope());
		
		type = Faces.getGenericTypeArgument(ip.getMember(), 0);
		
		if (key.isEmpty()) {
			this.key = ip.getMember().getName();
		} else {
			this.key = key;
		}
		
	}
	
	

	public String getKey() {
		return key;
	}

	@SuppressWarnings("unchecked")
	public T getValue() {
		String parameterValue = Faces.getRequest().getParameter(key);
		
		if (flashScope) {
//			Inicio Anterior
//			Flash flash = Faces.getFlash();
//			if (flash.containsKey(key)) {
//				value = (T)flash.get(key);
//				flash.keep(key);
//			}
//			Fim anterior
			if (parameterValue != null) {
				Faces.putFlashMap(key, parameterValue, Faces.getConverter(type));
			}
			value = (T) Faces.getFlashMap(key);
			
		} else {
			
//			Map<String, Object> viewMap = Faces.getViewMap();
//			if (parameterValue != null) {
//				viewMap.put(key, Faces.convert(parameterValue, getConverter()));
//			}
//
//			result = (T) viewMap.get(key);
		}
		
		return value;
	}

	public void setValue(T value) {
		this.value = value;
		if (flashScope) {
			Faces.putFlashMap(key, value);
		}
	}

	@Override
	public String toString() {
		if (value != null) {
			return value.toString();
		}
		return "";
	}

	public Class<?> getType() {
		return type;
	}
}
