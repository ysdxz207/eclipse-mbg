package com.puyixiaowo.eclipsembg.enums;

public enum JdbcConnectionEnum {
	CONNECTION_URL("connectionURL"),
	DRIVER_CLASS("driverClass"),
	USER_ID("userId"),
	PASSWORD("password");
	

	private JdbcConnectionEnum(String name) {
		this.name = name;
	}
	
	public String name;
}
