package com.puyixiaowo.eclipsembg.enums;

public enum SqlMapGeneratorEnum {
	TARGET_PACKAGE("targetPackage"),
	TARGET_PROJECT("targetProject");
	

	private SqlMapGeneratorEnum(String name) {
		this.name = name;
	}
	
	public String name;
}
