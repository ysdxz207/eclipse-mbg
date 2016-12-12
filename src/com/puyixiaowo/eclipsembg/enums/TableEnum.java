package com.puyixiaowo.eclipsembg.enums;

public enum TableEnum {
	TAG_NAME("table"),
	COLUMN_NAME("column"),
	IGNORE_COLUMN("ignoreColumn");
	

	private TableEnum(String name) {
		this.name = name;
	}
	
	public String name;
}
