package com.puyixiaowo.eclipsembg.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.DocumentException;
import org.yong.util.file.xml.XMLObject;
import org.yong.util.file.xml.XMLParser;

import com.puyixiaowo.eclipsembg.constants.Constants;
import com.puyixiaowo.eclipsembg.enums.TableEnum;
import com.puyixiaowo.eclipsembg.model.ClassPathEntry;
import com.puyixiaowo.eclipsembg.model.Context;
import com.puyixiaowo.eclipsembg.model.GeneratorConfig;
import com.puyixiaowo.eclipsembg.model.JavaClientGenerator;
import com.puyixiaowo.eclipsembg.model.JavaModelGenerator;
import com.puyixiaowo.eclipsembg.model.JavaTypeResolver;
import com.puyixiaowo.eclipsembg.model.JdbcConnection;
import com.puyixiaowo.eclipsembg.model.SqlMapGenerator;
import com.puyixiaowo.eclipsembg.model.Table;

public class GeneratorConfigUtil {
	/**
	 * generate default config file to dropin/eclipse-mbg dir
	 * 
	 * @return
	 */
	public static File generateDefaultConfFile() {

		try {
			InputStream input = GeneratorConfigUtil.class.getResourceAsStream("/resources/generatorConfig.xml");
			File file = new File(Constants.DEFAULT_CONFIG_FILE);
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				FileUtil.generateFileByInputStream(input, file);
			}
			return file;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * get generator config list
	 * 
	 * @return
	 */
	public static List<GeneratorConfig> getGeneratorConfigs() {
		if (Constants.configList == null) {
			List<File> files = getListFiles(Constants.CONF_DIR);
			Constants.configList = getConfigsByFiles(files);
		}
		return Constants.configList;
	}

	/**
	 * get config list by all files in eclipse-mbg dir
	 * 
	 * @param files
	 * @return
	 */
	private static List<GeneratorConfig> getConfigsByFiles(List<File> files) {
		List<GeneratorConfig> list = new ArrayList<GeneratorConfig>();

		for (File file : files) {
			list.add(parseConfig(file));
		}

		return list;
	}
	
	/**
	 * parse config file
	 * 
	 * @param file
	 * @return
	 */
	private static GeneratorConfig parseConfig(File file) {
		GeneratorConfig config = new GeneratorConfig();
		String fileName = file.getName();

		try {
			XMLParser parser = new XMLParser(file.getAbsolutePath());
			XMLObject root = parser.parse();
			// classPathEntry
			XMLObject classPathEntryObj = root.getChildTag("classPathEntry", 0);
			ClassPathEntry classPathEntry = new ClassPathEntry(parseProperties(classPathEntryObj));
			// context
			XMLObject contextObj = root.getChildTag("context", 0);
			Context context = new Context(parseProperties(contextObj));

			// jdbcConnection
			XMLObject jdbcConnectionObj = contextObj.getChildTag("jdbcConnection", 0);
			JdbcConnection jdbcConnection = new JdbcConnection(parseProperties(jdbcConnectionObj));
			context.setJdbcConnection(jdbcConnection);

			// javaTypeResolver
			XMLObject javaTypeResolverObj = contextObj.getChildTag("javaTypeResolver", 0);
			JavaTypeResolver javaTypeResolver = new JavaTypeResolver(parseProperties(javaTypeResolverObj));
			context.setJavaTypeResolver(javaTypeResolver);

			// javaModelGenerator
			XMLObject javaModelGeneratorObj = contextObj.getChildTag("javaModelGenerator", 0);
			JavaModelGenerator javaModelGenerator = new JavaModelGenerator(parseProperties(javaModelGeneratorObj));
			context.setJavaModelGenerator(javaModelGenerator);

			// sqlMapGenerator
			XMLObject sqlMapGeneratorObj = contextObj.getChildTag("sqlMapGenerator", 0);
			SqlMapGenerator sqlMapGenerator = new SqlMapGenerator(parseProperties(sqlMapGeneratorObj));
			context.setSqlMapGenerator(sqlMapGenerator);

			// javaClientGenerator
			XMLObject javaClientGeneratorObj = contextObj.getChildTag("javaClientGenerator", 0);
			JavaClientGenerator javaClientGenerator = new JavaClientGenerator(parseProperties(javaClientGeneratorObj));
			context.setJavaClientGenerator(javaClientGenerator);

			// table
			List<Table> tables = new ArrayList<Table>();
			List<XMLObject> tableObjList = contextObj.getAllChildTags("table");
			for (int i = 0; i < tableObjList.size(); i++) {
				XMLObject tableObj = tableObjList.get(i);
				Table table = new Table(parseProperties(tableObj), i);
				tables.add(table);
			}
			context.setTables(tables);

			config.setClassPathEntry(classPathEntry);
			config.setContext(context);
			config.setFileName(fileName);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return config;
	}

	public static GeneratorConfig getDefaultConfig(){
		
		return getConfigByFilename(Constants.DEFAULT_CONFIG_FILE_NAME);
	}

	/**
	 * parse xml object to properties
	 * @return
	 */
	private static Properties parseProperties(XMLObject obj) {
		Properties props = new Properties();
		Map<String, String> attrMap = obj.getAttrs();
		if (attrMap == null) {
			return props;
		}
		for (String key : attrMap.keySet()) {
			props.setProperty(key, attrMap.get(key));
		}
		if (obj.getChildTags("property").isEmpty()) {
			return props;
		}
		//containts property tag
		for (XMLObject xmlObj : obj.getChildTags("property")) {
			props.setProperty(xmlObj.getAttr("name"), xmlObj.getAttr("value"));
		}
		return props;
	}

	/***
	 * 获取指定目录下的所有的文件（不包括文件夹），采用了递归
	 * 
	 * @param obj
	 * @return
	 */
	private static ArrayList<File> getListFiles(Object obj) {
		File directory = null;
		if (obj instanceof File) {
			directory = (File) obj;
		} else {
			directory = new File(obj.toString());
		}
		ArrayList<File> files = new ArrayList<File>();
		if (directory.isFile()) {
			files.add(directory);
			return files;
		} else if (directory.isDirectory()) {
			File[] fileArr = directory.listFiles();
			for (int i = 0; i < fileArr.length; i++) {
				File fileOne = fileArr[i];
				files.addAll(getListFiles(fileOne));
			}
		}
		return files;
	}

	
	/**
	 * update config file in dropin/eclipse-mbg dir
	 */
	public static void updateConfigFile(GeneratorConfig config) {
		if (config == null) {
			return;
		}
		String filepath = Constants.CONF_DIR + config.getFileName();
		XMLParser parser = new XMLParser(filepath);
		try {

			XMLObject root = parser.parse();

			// classPathEntry
			XMLObject classPathEntryObj = root.getChildTag("classPathEntry", 0);
			if (config.getClassPathEntry() != null) {
				addProperties(config.getClassPathEntry().getProperties(), classPathEntryObj);
			}

			// context
			XMLObject contextObj = root.getChildTag("context", 0);
			if (config.getContext() == null) {
				parser.transferRoot(root, new File(filepath), false);//save file
				return;
			}
			addProperties(config.getContext().getProperties(), contextObj);

			// jdbcConnection
			XMLObject jdbcConnectionObj = contextObj.getChildTag("jdbcConnection", 0);
			if (config.getContext().getJdbcConnection() != null) {
				addProperties(config.getContext().getJdbcConnection().getProperties(), jdbcConnectionObj);
			}

			// javaTypeResolver
			XMLObject javaTypeResolverObj = contextObj.getChildTag("javaTypeResolver", 0);
			if (config.getContext().getJavaTypeResolver() != null) {
				addProperties(config.getContext().getJavaTypeResolver().getProperties(), javaTypeResolverObj);
			}

			// javaModelGenerator
			XMLObject javaModelGeneratorObj = contextObj.getChildTag("javaModelGenerator", 0);
			if (config.getContext().getJavaModelGenerator() != null) {
				addProperties(config.getContext().getJavaModelGenerator().getProperties(),
						javaModelGeneratorObj);
			}
			// sqlMapGenerator
			XMLObject sqlMapGeneratorObj = contextObj.getChildTag("sqlMapGenerator", 0);
			if (config.getContext().getSqlMapGenerator() != null) {
				addProperties(config.getContext().getSqlMapGenerator().getProperties(), sqlMapGeneratorObj);
			}
			// javaClientGenerator
			XMLObject javaClientGeneratorObj = contextObj.getChildTag("javaClientGenerator", 0);
			if (config.getContext().getJavaClientGenerator() != null) {
				addProperties(config.getContext().getJavaClientGenerator().getProperties(),
						javaClientGeneratorObj);
			}

			// table
			
			if (config.getContext().getTables() != null) {
				//delete tables
				List<XMLObject> tableObjList = contextObj.getAllChildTags("table");
				
				if (tableObjList != null) {
					for (XMLObject xmlObject : tableObjList) {
						xmlObject.remove();
					}
				}
				//add tables
				for (int i = 0; i < config.getContext().getTables().size(); i++) {
					Table table = config.getContext().getTables().get(i);
					
					XMLObject tableObject = XMLParser.createNode(TableEnum.TAG_NAME.name, "",
							attributesToMap(table.getProperties()));
					addProperties(table.getProperties(), tableObject);
					tableObject.insertAfter(contextObj);
				}
			}

			parser.transferRoot(root, new File(filepath), false);//save file
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * attributes list to Map<String, String>
	 * 
	 * @param attributes
	 * @return
	 */
	private static Map<String, String> attributesToMap(Properties properties) {
		Map<String, String> map = new HashMap<String, String>();

		for (Object key : properties.keySet()) {
			map.put(key.toString(), properties.getProperty(key.toString()));
		}
		return map;
	}

	/**
	 * add attributes to XMLObject
	 * 
	 * @param attributes
	 * @param xmlObj
	 */
	private static void addProperties(Properties properties, XMLObject xmlObj) {
		if (properties == null || properties.size() == 0) {
			return;
		}
		for (Object key : properties.keySet()) {
			xmlObj.addAttr(key.toString(), properties.getProperty(key.toString()));
		}
	}
	
	public static File addGeneratorConfig(GeneratorConfig config){
		try {
			String configFileName = config.getFileName();
			File file = new File(Constants.CONF_DIR + configFileName);
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				InputStream input = GeneratorConfigUtil.class.getResourceAsStream("/resources/generatorConfig.xml");
				FileUtil.generateFileByInputStream(input, file);
				
			}
			
			updateConfigFile(config);
			return file;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static GeneratorConfig getConfigByFilename(String filename){
		List<GeneratorConfig> configList = getGeneratorConfigs();
		GeneratorConfig config = null;
		for (GeneratorConfig generatorConfig :configList) {
			if (filename.equals(generatorConfig.getFileName())) {
				config = generatorConfig;
				break;
			}
		}
		return config;
	}
	
	public static void deleteGeneratorConfig(String filename){
		File file = new File(Constants.CONF_DIR + filename);
		if (file.exists()) {
			file.delete();
		}
	}
	
	public static boolean isGeneratorConfigExists(String filename){
		return getConfigByFilename(filename) != null;
	}
	

	public static void refreshGeneratorConfig() {
		Constants.configList = null;
		getGeneratorConfigs();
		Constants.defaultConfig = getDefaultConfig();
	}
	
	/**
	 * 
	 * @param config
	 * @param text
	 * @return
	 */
	public static Table getTableByTableName(GeneratorConfig config, String text) {
		Table table = null;
		if (config != null) {
			List<Table> tables = config.getContext().getTables();
			for (Table t : tables) {
				if (text.equals(t.getProperty(TableEnum.tableName.name))) {
					table = t;
					break;
				}
			}
		}
		return table;
	}

	public static void main(String[] args) {
		File file = new File(
				"D:/javaDir/java/eclipse-committers-neon-1a-win32-x86_64/eclipse/dropins/eclipse-mbg/generatorConfig.xml");
		// GeneratorConfig config = parseConfig(file);
		// System.out.println(config.getContext().getTables().get(0).getProperties().get(0).getValue());

		/*
		 * XMLParser parser = new XMLParser(file.getAbsolutePath()); try {
		 * XMLObject root = parser.parse(); //classPathEntry XMLObject
		 * classPathEntryObj = root.getChildTag("classPathEntry", 0);
		 * classPathEntryObj.addAttr(ClassPathEntryEnum.LOCATION.name,
		 * "i am location");
		 * 
		 * parser.transferRoot(root, file, false); } catch (Exception e) {
		 * e.printStackTrace(); }
		 */

	}

	

}
