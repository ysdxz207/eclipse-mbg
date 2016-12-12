package com.puyixiaowo.eclipsembg.model;

import java.util.Properties;

import org.mybatis.generator.config.ColumnOverride;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.IgnoredColumn;
import org.mybatis.generator.config.ModelType;
import org.mybatis.generator.config.PluginConfiguration;
import org.mybatis.generator.config.TableConfiguration;

import com.puyixiaowo.eclipsembg.enums.ClassPathEntryEnum;
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

	public Configuration toMybatisConfiguration() {
		Configuration conf = new Configuration();

		org.mybatis.generator.config.Context context = new org.mybatis.generator.config.Context(ModelType.FLAT);

		// plugin
		for (Plugin plugin : this.getContext().getPlugins()) {
			PluginConfiguration pluginConfiguration = new PluginConfiguration();
			Properties props = plugin.getProperties();
			for (Object key : props.keySet()) {
				pluginConfiguration.addProperty(key.toString(), props.getProperty(key.toString()));
			}
			context.addPluginConfiguration(pluginConfiguration);
		}

		// context
		Properties props = this.getContext().getProperties();
		for (Object key : props.keySet()) {
			context.addProperty(key.toString(), props.getProperty(key.toString()));
		}
		// table
		for (Table table : this.getContext().getTables()) {
			TableConfiguration tableConfiguration = new TableConfiguration(context);
			//column override
			for (com.puyixiaowo.eclipsembg.model.ColumnOverride co : table.getColumnOverrides()) {
				ColumnOverride columnOverride = new ColumnOverride(co.getProperty(TableEnum.COLUMN_NAME.name));
				for (Object key : co.getProperties().keySet()) {
					columnOverride.addProperty(key.toString(), co.getProperty(key.toString()));
				}
				tableConfiguration.addColumnOverride(columnOverride);
			}
			//ignored column
			for (com.puyixiaowo.eclipsembg.model.IgnoredColumn ic : table.getIgnoredColumns()) {
				
				IgnoredColumn ignoredColumn = new IgnoredColumn(ic.getProperty(TableEnum.IGNORE_COLUMN.name));
				
				for (Object key : ic.getProperties().keySet()) {
					ignoredColumn.addProperty(key.toString(), ic.getProperty(key.toString()));
				}
				tableConfiguration.addIgnoredColumn(ignoredColumn);
			}
			context.addTableConfiguration(tableConfiguration);
		}

		conf.addClasspathEntry(this.getClassPathEntry().getProperty(ClassPathEntryEnum.LOCATION.name));
		conf.addContext(context);

		return conf;
	}

}
