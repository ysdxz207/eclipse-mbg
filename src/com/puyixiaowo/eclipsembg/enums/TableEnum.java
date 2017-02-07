package com.puyixiaowo.eclipsembg.enums;

public enum TableEnum {
	TAG_NAME("table"),
	COLUMN_NAME("column"),
	IGNORE_COLUMN("ignoreColumn"), 
	DELIMITED_COLUMN_NAME("delimitedColumnName"),
	tableName("tableName"),
	domainObjectName("domainObjectName"),
	enableCountByExample("enableCountByExample"),
	enableUpdateByExample("enableUpdateByExample"),
	enableDeleteByExample("enableDeleteByExample"),
	enableSelectByExample("enableSelectByExample"),
	selectByExampleQueryId("selectByExampleQueryId");
	

	private TableEnum(String name) {
		this.name = name;
	}
	
	public String name;
}
