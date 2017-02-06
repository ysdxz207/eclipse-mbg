package com.puyixiaowo.eclipsembg.constants;

import java.util.List;

import com.puyixiaowo.eclipsembg.model.GeneratorConfig;

public class Constants {
	public static final int WIDTH_CONNECTION = 620;
	public static final int HEIGHT_CONNECTION = 320;
	
	public static final int WIDTH_CONFIG= 800;
	public static final int HEIGHT_CONFIG = 600;
	
	public static final String PROJECT_NAME = "com.puyixiaowo.mgb";
	
	//default config file name
	public static String DEFAULT_CONFIG_FILE_NAME = "generatorConfig.xml";
	//config root directory
	public static String CONF_DIR = System.getProperty("user.dir").replaceAll("\\\\", "/") + "/dropins/eclipse-mbg/";
	//default config file path
	public static final String DEFAULT_CONFIG_FILE = CONF_DIR + DEFAULT_CONFIG_FILE_NAME;
	
	public static final String SQL_SELECT_DBS_PATH = "classpath:resources/sql/sql_select_dbs.sql";
	
	//config list
	public static List<GeneratorConfig> configList;
	//default config
	public static GeneratorConfig defaultConfig;
}
