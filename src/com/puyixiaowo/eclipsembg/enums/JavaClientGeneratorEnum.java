package com.puyixiaowo.eclipsembg.enums;

public enum JavaClientGeneratorEnum {
	TYPE("type"),
	TARGET_PACKAGE("targetPackage"),
	TARGET_PROJECT("targetProject"),
	ROOT_INTERFACE("rootInterface");
	

	private JavaClientGeneratorEnum(String name) {
		this.name = name;
	}
	
	public String name;
}
