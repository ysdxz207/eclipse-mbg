package com.puyixiaowo.eclipsembg.enums;

public enum JavaModelGeneratorEnum {
	TARGET_PACKAGE("targetPackage"),
	TARGET_PROJECT("targetProject");
	

	private JavaModelGeneratorEnum(String name) {
		this.name = name;
	}
	
	public String name;
}
