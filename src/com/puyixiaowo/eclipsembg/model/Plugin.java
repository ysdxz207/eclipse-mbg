package com.puyixiaowo.eclipsembg.model;

import java.util.List;

public class Plugin extends BaseBean{
	private String type;
	private List<Property> propertys;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Property> getPropertys() {
		return propertys;
	}

	public void setPropertys(List<Property> propertys) {
		this.propertys = propertys;
	}

}
