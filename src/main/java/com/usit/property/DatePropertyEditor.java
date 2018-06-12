package com.usit.property;

import java.beans.PropertyEditorSupport;
import java.sql.Date;

public class DatePropertyEditor extends PropertyEditorSupport {

	@Override
	public String getAsText() {
		return String.valueOf((Date)this.getValue());
	}
	
	@Override
	public void setAsText(String text) {
		if (text.equals("")) {
			this.setValue(null);
			return;
		}
		this.setValue(Date.valueOf(text));
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
