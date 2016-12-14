package com.puyixiaowo.eclipsembg.model;

import java.util.Properties;

import org.mybatis.generator.api.IntrospectedTable.TargetRuntime;
import org.mybatis.generator.config.ColumnOverride;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.IgnoredColumn;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.JavaTypeResolverConfiguration;
import org.mybatis.generator.config.ModelType;
import org.mybatis.generator.config.PluginConfiguration;
import org.mybatis.generator.config.SqlMapGeneratorConfiguration;
import org.mybatis.generator.config.TableConfiguration;

import com.puyixiaowo.eclipsembg.enums.ClassPathEntryEnum;
import com.puyixiaowo.eclipsembg.enums.JavaClientGeneratorEnum;
import com.puyixiaowo.eclipsembg.enums.JavaModelGeneratorEnum;
import com.puyixiaowo.eclipsembg.enums.JavaTypeResolverEnum;
import com.puyixiaowo.eclipsembg.enums.JdbcConnectionEnum;
import com.puyixiaowo.eclipsembg.enums.SqlMapGeneratorEnum;
import com.puyixiaowo.eclipsembg.enums.TableEnum;

public class GeneratorConfig {
	private String fileName;
	private ClassPathEntry classPathEntry;
	private Context context;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public ClassPathEntry getClassPathEntry() {
		return classPathEntry;
	}

	public void setClassPathEntry(ClassPathEntry classPathEntry) {
		this.classPathEntry = classPathEntry;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	@Override
	public String toString() {

		return this.fileName;
	}
	///////////////////////////////

	/**
	 * convert to mybatis config
	 * 
	 * @return
	 */
	public Configuration toMybatisConfiguration() {
		Configuration conf = new Configuration();

		org.mybatis.generator.config.Context context = new org.mybatis.generator.config.Context(ModelType.FLAT);

		// plugin
		if (this.getContext().getPlugins() != null) {
			for (Plugin plugin : this.getContext().getPlugins()) {
				PluginConfiguration pluginConfiguration = new PluginConfiguration();
				Properties props = plugin.getProperties();
				for (Object key : props.keySet()) {
					pluginConfiguration.addProperty(key.toString(), props.getProperty(key.toString()));
				}
				context.addPluginConfiguration(pluginConfiguration);
			}
		}

		// context
		if (!this.getContext().getProperties().isEmpty()) {
			Properties props = this.getContext().getProperties();
			for (Object key : props.keySet()) {
				context.addProperty(key.toString(), props.getProperty(key.toString()));
			}
		}

		// table
		if (this.getContext().getTables() != null) {
			for (Table table : this.getContext().getTables()) {
				TableConfiguration tableConfiguration = new TableConfiguration(context);
				// column override
				if (table.getColumnOverrides() == null) {
					continue;
				}
				for (com.puyixiaowo.eclipsembg.model.ColumnOverride co : table.getColumnOverrides()) {
					ColumnOverride columnOverride = new ColumnOverride(co.getProperty(TableEnum.COLUMN_NAME.name));
					for (Object key : co.getProperties().keySet()) {
						columnOverride.addProperty(key.toString(), co.getProperty(key.toString()));
					}
					tableConfiguration.addColumnOverride(columnOverride);
				}
				// ignored column
				if (table.getIgnoredColumns() == null) {
					continue;
				}
				for (com.puyixiaowo.eclipsembg.model.IgnoredColumn ic : table.getIgnoredColumns()) {

					IgnoredColumn ignoredColumn = new IgnoredColumn(ic.getProperty(TableEnum.IGNORE_COLUMN.name));

					ignoredColumn.setColumnNameDelimited(
							Boolean.valueOf(ic.getProperty(TableEnum.DELIMITED_COLUMN_NAME.name)));
					tableConfiguration.addIgnoredColumn(ignoredColumn);
				}
				// properties
				if (table.getProperties().isEmpty()) {
					continue;
				}
				for (Object key : table.getProperties().keySet()) {
					tableConfiguration.addProperty(key.toString(), table.getProperty(key.toString()));
				}
				context.addTableConfiguration(tableConfiguration);
			}
		}
		// java client
		if (this.context.getJavaClientGenerator() != null) {
			JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();
			javaClientGeneratorConfiguration.setConfigurationType(
					this.context.getJavaClientGenerator().getProperty(JavaClientGeneratorEnum.TYPE.name));
			javaClientGeneratorConfiguration.setImplementationPackage(
					this.context.getJavaClientGenerator().getProperty(JavaClientGeneratorEnum.ROOT_INTERFACE.name));
			javaClientGeneratorConfiguration.setTargetPackage(
					this.context.getJavaClientGenerator().getProperty(JavaClientGeneratorEnum.TARGET_PACKAGE.name));
			javaClientGeneratorConfiguration.setTargetProject(
					this.context.getJavaClientGenerator().getProperty(JavaClientGeneratorEnum.TARGET_PROJECT.name));
			if (!this.context.getJavaClientGenerator().getProperties().isEmpty()) {
				for (Object key : this.context.getJavaClientGenerator().getProperties().keySet()) {
					javaClientGeneratorConfiguration.addProperty(key.toString(),
							this.context.getJavaClientGenerator().getProperty(key.toString()));
				}
			}
			context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);
		}
		// java model
		if (this.context.getJavaModelGenerator() != null) {
			JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration();
			javaModelGeneratorConfiguration.setTargetPackage(
					this.context.getJavaModelGenerator().getProperty(JavaModelGeneratorEnum.TARGET_PACKAGE.name));
			javaModelGeneratorConfiguration.setTargetProject(
					this.context.getJavaModelGenerator().getProperty(JavaModelGeneratorEnum.TARGET_PROJECT.name));
			if (!this.context.getJavaModelGenerator().getProperties().isEmpty()) {
				for (Object key : this.context.getJavaModelGenerator().getProperties().keySet()) {
					javaModelGeneratorConfiguration.addProperty(key.toString(),
							this.context.getJavaModelGenerator().getProperty(key.toString()));
				}
			}
			context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);
		}
		// java type resolver
		if (this.context.getJavaTypeResolver() != null) {
			JavaTypeResolverConfiguration javaTypeResolverConfiguration = new JavaTypeResolverConfiguration();
			javaTypeResolverConfiguration.setConfigurationType(
					this.context.getJavaTypeResolver().getProperty(JavaTypeResolverEnum.TYPE.name));
			if (!this.context.getJavaTypeResolver().getProperties().keySet().isEmpty()) {
				for (Object key : this.context.getJavaTypeResolver().getProperties().keySet()) {
					javaTypeResolverConfiguration.addProperty(key.toString(),
							this.context.getJavaTypeResolver().getProperty(key.toString()));
				}
			}
			context.setJavaTypeResolverConfiguration(javaTypeResolverConfiguration);
		}
		// jdbc connection
		if (this.context.getJdbcConnection() != null) {
			JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
			jdbcConnectionConfiguration.setConnectionURL(this.context.getJdbcConnection().getProperty(JdbcConnectionEnum.CONNECTION_URL.name));
			jdbcConnectionConfiguration.setDriverClass(this.context.getJdbcConnection().getProperty(JdbcConnectionEnum.DRIVER_CLASS.name));
			jdbcConnectionConfiguration.setPassword(this.context.getJdbcConnection().getProperty(JdbcConnectionEnum.PASSWORD.name));
			jdbcConnectionConfiguration.setUserId(this.context.getJdbcConnection().getProperty(JdbcConnectionEnum.USER_ID.name));
			if (!this.context.getJdbcConnection().getProperties().isEmpty()) {
				for (Object key : this.context.getJdbcConnection().getProperties().keySet()) {
					jdbcConnectionConfiguration.addProperty(key.toString(),
							this.context.getJdbcConnection().getProperty(key.toString()));
				}
			}
			context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);
		}
		// sql map
		if (this.context.getSqlMapGenerator() != null) {
			SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();
			sqlMapGeneratorConfiguration.setTargetPackage(
					this.getContext().getSqlMapGenerator().getProperty(SqlMapGeneratorEnum.TARGET_PACKAGE.name));
			sqlMapGeneratorConfiguration.setTargetProject(
					this.getContext().getSqlMapGenerator().getProperty(SqlMapGeneratorEnum.TARGET_PROJECT.name));
			if (!this.context.getSqlMapGenerator().getProperties().isEmpty()) {
				for (Object key : this.context.getSqlMapGenerator().getProperties().keySet()) {
					sqlMapGeneratorConfiguration.addProperty(key.toString(),
							this.context.getSqlMapGenerator().getProperty(key.toString()));
				}
			}
			context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);
		}
		// target runtime
		context.setTargetRuntime(TargetRuntime.MYBATIS3.name());
		context.setId(this.context.getProperty("id"));
		conf.addClasspathEntry(this.getClassPathEntry().getProperty(ClassPathEntryEnum.LOCATION.name));
		conf.addContext(context);

		return conf;
	}

}
