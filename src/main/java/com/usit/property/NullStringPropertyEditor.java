package com.usit.property;

import java.beans.PropertyEditorSupport;

public class NullStringPropertyEditor extends PropertyEditorSupport {

	@Override
	public String getAsText() {
		return String.valueOf((String)this.getValue());
	}
	
	@Override
	public void setAsText(String text) {
		if (text.equals("")) return;
		this.setValue(text);
	}
	
	@Override
	public void setValue(Object value) {
		super.setValue(value);
	}
	
	@Override
	public Object getValue() {
		return super.getValue();
	}
}
