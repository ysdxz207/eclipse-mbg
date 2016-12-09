package com.puyixiaowo.eclipsembg.model;

import java.util.List;

public class Table extends BaseBean {

	public Table(List<Attribute> attributes, int pos) {
		this.setAttributes(attributes);
		this.setPos(pos);
	}

}
