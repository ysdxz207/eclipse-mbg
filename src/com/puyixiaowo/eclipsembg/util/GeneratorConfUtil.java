package com.puyixiaowo.eclipsembg.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.dom4j.DocumentException;
import org.yong.util.file.xml.XMLObject;
import org.yong.util.file.xml.XMLParser;

import com.puyixiaowo.eclipsembg.conf.Constant;
import com.puyixiaowo.eclipsembg.conf.GeneratorConfig;
import com.puyixiaowo.eclipsembg.enums.TableEnum;
import com.puyixiaowo.eclipsembg.model.Attribute;
import com.puyixiaowo.eclipsembg.model.ClassPathEntry;
import com.puyixiaowo.eclipsembg.model.Context;
import com.puyixiaowo.eclipsembg.model.JavaClientGenerator;
import com.puyixiaowo.eclipsembg.model.JavaModelGenerator;
import com.puyixiaowo.eclipsembg.model.JavaTypeResolver;
import com.puyixiaowo.eclipsembg.model.JdbcConnection;
import com.puyixiaowo.eclipsembg.model.SqlMapGenerator;
import com.puyixiaowo.eclipsembg.model.Table;
import com.puyixiaowo.eclipsembg.views.EclipsembgView;

public class GeneratorConfUtil {
	/**
	 * generate default config file to dropin/eclipse-mbg dir
	 * 
	 * @return
	 */
	public static File generateDefaultConfFile() {

		try {
			InputStream input = EclipsembgView.class.getResourceAsStream("/resources/generatorConfig.xml");
			File file = new File(Constant.DEFAULT_CONFIG_FILE);
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				FileUtil.inputstreamToFile(input, file);
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
		List<File> files = getListFiles(Constant.CONF_DIR);
		return getConfigsByFiles(files);
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
			ClassPathEntry classPathEntry = new ClassPathEntry(parseAttributes(classPathEntryObj));
			// context
			XMLObject contextObj = root.getChildTag("context", 0);
			Context context = new Context(parseAttributes(contextObj));

			// jdbcConnection
			XMLObject jdbcConnectionObj = contextObj.getChildTag("jdbcConnection", 0);
			JdbcConnection jdbcConnection = new JdbcConnection(parseAttributes(jdbcConnectionObj));
			context.setJdbcConnection(jdbcConnection);

			// javaTypeResolver
			XMLObject javaTypeResolverObj = contextObj.getChildTag("javaTypeResolver", 0);
			JavaTypeResolver javaTypeResolver = new JavaTypeResolver(parseAttributes(javaTypeResolverObj));
			context.setJavaTypeResolver(javaTypeResolver);

			// javaModelGenerator
			XMLObject javaModelGeneratorObj = contextObj.getChildTag("javaModelGenerator", 0);
			JavaModelGenerator javaModelGenerator = new JavaModelGenerator(parseAttributes(javaModelGeneratorObj));
			context.setJavaModelGenerator(javaModelGenerator);

			// sqlMapGenerator
			XMLObject sqlMapGeneratorObj = contextObj.getChildTag("sqlMapGenerator", 0);
			SqlMapGenerator sqlMapGenerator = new SqlMapGenerator(parseAttributes(sqlMapGeneratorObj));
			context.setSqlMapGenerator(sqlMapGenerator);

			// javaClientGenerator
			XMLObject javaClientGeneratorObj = contextObj.getChildTag("javaClientGenerator", 0);
			JavaClientGenerator javaClientGenerator = new JavaClientGenerator(parseAttributes(javaClientGeneratorObj));
			context.setJavaClientGenerator(javaClientGenerator);

			// table
			List<Table> tables = new ArrayList<Table>();
			List<XMLObject> tableObjList = contextObj.getAllChildTags("table");
			for (int i = 0; i < tableObjList.size(); i++) {
				XMLObject tableObj = tableObjList.get(i);
				Table table = new Table(parseAttributes(tableObj), i);
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

	/**
	 * refresh config list
	 * 
	 * @return
	 */
	public static List<GeneratorConfig> refreshConfigs() {
		Constant.configList = getGeneratorConfigs();
		for (GeneratorConfig generatorConfig : Constant.configList) {
			if (generatorConfig.getFileName().equals("generatorConfig.xml")) {
				Constant.defaultConfig = generatorConfig;
				break;
			}
		}
		return Constant.configList;
	}

	/**
	 * 
	 * @return
	 */
	private static List<Attribute> parseAttributes(XMLObject obj) {
		List<Attribute> attributes = new ArrayList<Attribute>();
		Map<String, String> attrMap = obj.getAttrs();
		for (String key : attrMap.keySet()) {
			Attribute attribute = new Attribute(key, attrMap.get(key));
			attributes.add(attribute);
		}
		return attributes;
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
	 * update default config file in dropin/eclipse-mbg dir
	 */
	public static void updateDefaultConfigFile(GeneratorConfig config) {
		if (config == null) {
			return;
		}

		XMLParser parser = new XMLParser(Constant.DEFAULT_CONFIG_FILE);
		try {
			GeneratorConfig defaultConfig = new GeneratorConfig();
			defaultConfig.setClassPathEntry(Constant.defaultConfig.getClassPathEntry());
			defaultConfig.setContext(Constant.defaultConfig.getContext());
			defaultConfig.setFileName(Constant.defaultConfig.getFileName());

			BeanUtils.copyProperties(defaultConfig, config);

			XMLObject root = parser.parse();

			// classPathEntry
			XMLObject classPathEntryObj = root.getChildTag("classPathEntry", 0);
			if (defaultConfig.getClassPathEntry() != null) {
				addAttributes(defaultConfig.getClassPathEntry().getAttributes(), classPathEntryObj);
			}

			// context
			XMLObject contextObj = root.getChildTag("context", 0);
			if (defaultConfig.getContext() == null) {
				saveParser(parser, root);//save file
				return;
			}
			addAttributes(defaultConfig.getContext().getAttributes(), contextObj);

			// jdbcConnection
			XMLObject jdbcConnectionObj = contextObj.getChildTag("jdbcConnection", 0);
			if (defaultConfig.getContext().getJdbcConnection() != null) {
				addAttributes(defaultConfig.getContext().getJdbcConnection().getAttributes(), jdbcConnectionObj);
			}

			// javaTypeResolver
			XMLObject javaTypeResolverObj = contextObj.getChildTag("javaTypeResolver", 0);
			if (defaultConfig.getContext().getJavaTypeResolver() != null) {
				addAttributes(defaultConfig.getContext().getJavaTypeResolver().getAttributes(), javaTypeResolverObj);
			}

			// javaModelGenerator
			XMLObject javaModelGeneratorObj = contextObj.getChildTag("javaModelGenerator", 0);
			if (defaultConfig.getContext().getJavaModelGenerator() != null) {
				addAttributes(defaultConfig.getContext().getJavaModelGenerator().getAttributes(),
						javaModelGeneratorObj);
			}
			// sqlMapGenerator
			XMLObject sqlMapGeneratorObj = contextObj.getChildTag("sqlMapGenerator", 0);
			if (defaultConfig.getContext().getSqlMapGenerator() != null) {
				addAttributes(defaultConfig.getContext().getSqlMapGenerator().getAttributes(), sqlMapGeneratorObj);
			}
			// javaClientGenerator
			XMLObject javaClientGeneratorObj = contextObj.getChildTag("javaClientGenerator", 0);
			if (defaultConfig.getContext().getJavaClientGenerator() != null) {
				addAttributes(defaultConfig.getContext().getJavaClientGenerator().getAttributes(),
						javaClientGeneratorObj);
			}

			// table
			List<XMLObject> tableObjList = contextObj.getAllChildTags("table");
			
			if (tableObjList != null) {
				for (XMLObject xmlObject : tableObjList) {
					xmlObject.remove();
				}
			}
			
			if (defaultConfig.getContext().getTables() != null) {
				
				for (int i = 0; i < defaultConfig.getContext().getTables().size(); i++) {
					Table table = defaultConfig.getContext().getTables().get(i);
					
					XMLObject tableObject = XMLParser.createNode(TableEnum.TAG_NAME.name, "",
							attributesToMap(table.getAttributes()));
					addAttributes(table.getAttributes(), tableObject);
					tableObject.insertAfter(contextObj);
				}
			}

			saveParser(parser, root);//save file
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void saveParser(XMLParser parser, XMLObject root) throws IOException{
		parser.transferRoot(root, new File(Constant.DEFAULT_CONFIG_FILE), false);
		refreshConfigs();//refresh configs
	}

	/**
	 * attributes list to Map<String, String>
	 * 
	 * @param attributes
	 * @return
	 */
	private static Map<String, String> attributesToMap(List<Attribute> attributes) {
		Map<String, String> map = new HashMap<String, String>();

		for (Attribute attribute : attributes) {
			map.put(attribute.getName(), attribute.getValue());
		}
		return map;
	}

	/**
	 * add attributes to XMLObject
	 * 
	 * @param attributes
	 * @param xmlObj
	 */
	private static void addAttributes(List<Attribute> attributes, XMLObject xmlObj) {
		if (attributes == null || attributes.size() == 0) {
			return;
		}
		for (Attribute attribute : attributes) {
			xmlObj.addAttr(attribute.getName(), attribute.getValue());
		}
	}

	public static void main(String[] args) {
		File file = new File(
				"D:/javaDir/java/eclipse-committers-neon-1a-win32-x86_64/eclipse/dropins/eclipse-mbg/generatorConfig.xml");
		// GeneratorConfig config = parseConfig(file);
		// System.out.println(config.getContext().getTables().get(0).getAttributes().get(0).getValue());

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
