package com.puyixiaowo.eclipsembg.model;

import org.mybatis.generator.config.PropertyHolder;

public class Plugin extends PropertyHolder {
	private String type;
	private int pos;

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
