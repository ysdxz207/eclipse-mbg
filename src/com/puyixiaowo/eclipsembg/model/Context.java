package com.puyixiaowo.eclipsembg.model;

import java.util.List;
import java.util.Properties;

import org.mybatis.generator.config.PropertyHolder;

public class Context extends PropertyHolder {
	private List<Plugin> plugins;
	private JdbcConnection jdbcConnection;
	private JavaTypeResolver javaTypeResolver;
	private JavaModelGenerator javaModelGenerator;
	private SqlMapGenerator sqlMapGenerator;
	private JavaClientGenerator javaClientGenerator;
	private List<Table> tables;

	public Context() {
	}


	public Context(Properties properties) {
		for (Object key : properties.keySet()) {
			this.addProperty(key.toString(), properties.getProperty(key.toString()));
		}
	}


	public List<Plugin> getPlugins() {
		return plugins;
	}

	public void setPlugins(List<Plugin> plugins) {
		this.plugins = plugins;
	}

	public JdbcConnection getJdbcConnection() {
		return jdbcConnection;
	}

	public void setJdbcConnection(JdbcConnection jdbcConnection) {
		this.jdbcConnection = jdbcConnection;
	}

	public JavaTypeResolver getJavaTypeResolver() {
		return javaTypeResolver;
	}

	public void setJavaTypeResolver(JavaTypeResolver javaTypeResolver) {
		this.javaTypeResolver = javaTypeResolver;
	}

	public JavaModelGenerator getJavaModelGenerator() {
		return javaModelGenerator;
	}

	public void setJavaModelGenerator(JavaModelGenerator javaModelGenerator) {
		this.javaModelGenerator = javaModelGenerator;
	}

	public SqlMapGenerator getSqlMapGenerator() {
		return sqlMapGenerator;
	}

	public void setSqlMapGenerator(SqlMapGenerator sqlMapGenerator) {
		this.sqlMapGenerator = sqlMapGenerator;
	}

	public JavaClientGenerator getJavaClientGenerator() {
		return javaClientGenerator;
	}

	public void setJavaClientGenerator(JavaClientGenerator javaClientGenerator) {
		this.javaClientGenerator = javaClientGenerator;
	}

	public List<Table> getTables() {
		return tables;
	}

	public void setTables(List<Table> tables) {
		this.tables = tables;
	}

}
