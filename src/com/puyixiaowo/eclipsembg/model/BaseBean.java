package com.puyixiaowo.eclipsembg.model;

import java.util.List;

public class BaseBean {
	private List<Attribute> attributes;
	private int pos;//position index
	

	public BaseBean() {
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}
	
	//////////////////////
	public String getAttr(String name) {
		for (Attribute attribute : attributes) {
			if (attribute.getName().equals(name)) {
				return attribute.getValue();
			}
		}
		return null;
	}
	
}
