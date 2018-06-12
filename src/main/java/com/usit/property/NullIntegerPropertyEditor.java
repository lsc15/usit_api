package com.usit.property;

import java.beans.PropertyEditorSupport;

public class NullIntegerPropertyEditor extends PropertyEditorSupport {
	
	@Override
	public String getAsText() {
		return String.valueOf((Integer)this.getValue());
	}
	
	@Override
	public void setAsText(String text) {
		if (text.equals("")) return;
		this.setValue(Integer.valueOf(text));
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
