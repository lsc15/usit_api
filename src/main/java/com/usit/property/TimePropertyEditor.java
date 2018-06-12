package com.usit.property;

import java.beans.PropertyEditorSupport;
import java.sql.Time;
import java.time.LocalTime;

public class TimePropertyEditor extends PropertyEditorSupport {

	@Override
	public String getAsText() {
		return String.valueOf((Time)this.getValue());
	}
	
	@Override
	public void setAsText(String text) {
		if (text.equals("")) {
			this.setValue(null);
			return;
		}
		int hour = 0;
		int min = 0;
		String[] timeStringArr = text.split(" ");
		String amPm = timeStringArr[0];
		String hourMinString = timeStringArr[1];
		String[] hourMinStringArr = hourMinString.split(":");
		hour = Integer.parseInt(hourMinStringArr[0]);
		min = Integer.parseInt(hourMinStringArr[1]);
		if (amPm.equals("PM")) hour = hour + 12;
		this.setValue(Time.valueOf(LocalTime.of(hour, min)));
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
