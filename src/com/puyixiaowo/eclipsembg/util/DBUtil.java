package com.puyixiaowo.eclipsembg.util;

import com.puyixiaowo.eclipsembg.enums.JdbcConnectionEnum;
import com.puyixiaowo.eclipsembg.model.GeneratorConfig;

public class DBUtil {
	
	/**
	 * get db name by generator config
	 * @param config
	 * @return
	 */
	public static String getDbName(GeneratorConfig config) {
		
		String connectionURL = config.getContext().getJdbcConnection().getProperty(JdbcConnectionEnum.CONNECTION_URL.name);
		return connectionURL.substring(connectionURL.lastIndexOf("/") + 1);
	}
	
	public static void main(String[] args) {
		String connectionURL = "jdbc:mysql://localhost:3306/test_db";
		System.out.println(connectionURL.substring(connectionURL.lastIndexOf("/") + 1));
	}
}
