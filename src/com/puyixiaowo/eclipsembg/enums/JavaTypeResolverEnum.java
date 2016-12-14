package com.puyixiaowo.eclipsembg.enums;

public enum JavaTypeResolverEnum {
	TYPE("type"),
	FORCE_BIG_DECIMALS("forceBigDecimals");
	

	private JavaTypeResolverEnum(String name) {
		this.name = name;
	}
	
	public String name;
}
