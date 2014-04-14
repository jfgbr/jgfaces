package com.jgalante.converter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.jgalante.util.Faces;
import com.jgalante.util.Parameter;

@FacesConverter(forClass=Parameter.class)
public class ParameterConverter implements Converter{

	@SuppressWarnings("unchecked")
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		Object obj = value;
		if (component instanceof UIInput) {
			Parameter<Object> parameter = (Parameter<Object>)((UIInput)component).getValue();
			parameter.setValue(Faces.convert(value, Faces.getConverter(parameter.getType())));
			obj = parameter;			
		}		
		
		return obj;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		return value.toString();
	}

}
