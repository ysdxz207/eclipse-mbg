package com.puyixiaowo.eclipsembg.model;

import java.util.Properties;

import org.mybatis.generator.config.PropertyHolder;

public class JavaTypeResolver extends PropertyHolder{

	public JavaTypeResolver(Properties properties) {
		for (Object key : properties.keySet()) {
			this.addProperty(key.toString(), properties.getProperty(key.toString()));
		}
	}

}
