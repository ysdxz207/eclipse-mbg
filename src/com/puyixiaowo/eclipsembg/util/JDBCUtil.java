package com.puyixiaowo.eclipsembg.util;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.puyixiaowo.eclipsembg.constants.Constants;
import com.puyixiaowo.eclipsembg.constants.Sql;
import com.puyixiaowo.eclipsembg.enums.JdbcConnectionEnum;
import com.puyixiaowo.eclipsembg.model.GeneratorConfig;

public class JDBCUtil {
	
	/**
	 * get db tables
	 * @param dbName
	 * @return
	 * @throws Exception
	 */
	public static List<String> getTableNames(String dbName) throws Exception{
		
		GeneratorConfig config = getConfigByDBName(dbName);
		if (config == null) {
			return null;
		}
		String driverClass = config.getContext().getJdbcConnection().getProperty(JdbcConnectionEnum.DRIVER_CLASS.name);
		String url = config.getContext().getJdbcConnection().getProperty(JdbcConnectionEnum.CONNECTION_URL.name);
		String userId = config.getContext().getJdbcConnection().getProperty(JdbcConnectionEnum.USER_ID.name);
		String password = config.getContext().getJdbcConnection().getProperty(JdbcConnectionEnum.PASSWORD.name);
		
		
		DBSqlUtil runner = new DBSqlUtil(driverClass, url, userId, password);
		return runner.executeSql(Sql.SELECT_DBS_SQL);
		
	}
	/**
	 * 
	 * @param dbName
	 * @return
	 */
	private static GeneratorConfig getConfigByDBName(String dbName) {
		List<GeneratorConfig> configList = GeneratorConfigUtil.getGeneratorConfigs();
		
		for (GeneratorConfig config : configList) {
			String name = DBUtil.getDbName(config);
			if (name.equals(dbName)) {
				return config;
			}
		}
		return null;
	}
}
