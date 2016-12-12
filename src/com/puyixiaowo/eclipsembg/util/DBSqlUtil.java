package com.puyixiaowo.eclipsembg.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBSqlUtil {
    private String driver;
    private String url;
    private String userid;
    private String password;

    public DBSqlUtil(String driver, String url,
            String userId, String password) throws Exception {
        
        
        if (driver == null || driver.length() == 0) {
            throw new Exception("JDBC Driver is required");
        }
        
        if (url == null || url.length() == 0) {
            throw new Exception("JDBC URL is required");
        }
        
        this.driver = driver;
        this.url = url;
        this.userid = userId;
        this.password = password;
    }
    /**
     * excute sql
     * @return
     * @throws Exception
     */
    public List<String> executeSql(String sql) throws Exception {

        Connection connection = null;
        Statement statement = null;

        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, userid, password);

            statement = connection.createStatement();

            ResultSet rs = null;

            rs = statement.executeQuery(sql);
            
            List<String> tableNames = new ArrayList<String>();
    		while (rs.next()) {
    			String tableName = rs.getString(1);
    			tableNames.add(tableName);
    		}
    		
            return tableNames;
        } finally {
        	closeStatement(statement);
            closeConnection(connection);
        }
    }

    

    private void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                // ignore
                ;
            }
        }
    }

    private void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                // ignore
                ;
            }
        }
    }
    
    ///////////////
    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}