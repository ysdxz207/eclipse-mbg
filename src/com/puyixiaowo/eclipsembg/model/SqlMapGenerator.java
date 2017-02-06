package com.puyixiaowo.eclipsembg.model;

import java.util.Properties;

import org.mybatis.generator.config.PropertyHolder;

public class SqlMapGenerator extends PropertyHolder{
	
	public SqlMapGenerator() {
	}
	
	public SqlMapGenerator(Properties properties) {
		for (Object key : properties.keySet()) {
			this.addProperty(key.toString(), properties.getProperty(key.toString()));
		}
	}


}
