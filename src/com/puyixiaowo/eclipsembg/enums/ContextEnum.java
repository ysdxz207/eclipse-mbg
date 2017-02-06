package com.puyixiaowo.eclipsembg.enums;

public enum ContextEnum {
	ID("id"),
	TARGET_RUNTIME("targetRuntime");
	

	private ContextEnum(String name) {
		this.name = name;
	}
	
	public String name;
}
