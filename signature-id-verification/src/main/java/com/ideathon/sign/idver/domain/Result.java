package com.ideathon.sign.idver.domain;

public class Result {

	private String value;
	private String viewValue;

	public Result(String value, String viewValue) {
		this.value = value;
		this.viewValue = viewValue;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getViewValue() {
		return viewValue;
	}

	public void setViewValue(String viewValue) {
		this.viewValue = viewValue;
	}
}
