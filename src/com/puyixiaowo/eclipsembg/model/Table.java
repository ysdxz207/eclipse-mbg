package com.puyixiaowo.eclipsembg.model;

import java.util.List;
import java.util.Properties;

import org.mybatis.generator.config.PropertyHolder;

public class Table extends PropertyHolder {

	private List<ColumnOverride> columnOverrides;
	private List<IgnoredColumn> ignoredColumns;
	private int pos;

	public Table(Properties properties, int i) {
		for (Object key : properties.keySet()) {
			this.addProperty(key.toString(), properties.getProperty(key.toString()));
		}
		this.pos = i;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public List<ColumnOverride> getColumnOverrides() {
		return columnOverrides;
	}

	public void setColumnOverrides(List<ColumnOverride> columnOverrides) {
		this.columnOverrides = columnOverrides;
	}

	public List<IgnoredColumn> getIgnoredColumns() {
		return ignoredColumns;
	}

	public void setIgnoredColumns(List<IgnoredColumn> ignoredColumns) {
		this.ignoredColumns = ignoredColumns;
	}
	
	

}
