package com.puyixiaowo.eclipsembg.conf;

import java.util.ArrayList;
import java.util.List;

public class Constant {
	public static final int WIDTH = 620;
	public static final int HEIGHT = 320;
	//config root directory
	public static String CONF_DIR = System.getProperty("user.dir").replaceAll("\\\\", "/") + "/dropins/eclipse-mbg/";
	//default config file path
	public static final String DEFAULT_CONFIG_FILE = CONF_DIR + "generatorConfig.xml";
	
	
	/*
	 * generator config list
	 */
	public static List<GeneratorConfig> configList = new ArrayList<GeneratorConfig>();
	/*
	 * default generator config
	 */
	public static GeneratorConfig defaultConfig;
}
