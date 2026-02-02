package com.revplay.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class JDBCUtil {

    private static String url;
    private static String user;
    private static String pass;

    static {
        try {
            Properties props = new Properties();

            InputStream is = JDBCUtil.class
                    .getClassLoader()
                    .getResourceAsStream("db.properties");

            if (is == null) {
                throw new RuntimeException("db.properties not found in src folder");
            }

            props.load(is);

            url = props.getProperty("db.url");
            user = props.getProperty("db.username");
            pass = props.getProperty("db.password");
            String driver = props.getProperty("db.driver");

            Class.forName(driver);
            LoggerUtil.logInfo("JDBC Driver Loaded");

        } catch (Exception e) {
            LoggerUtil.logError("Failed to load database config", e);
            throw new com.revplay.exception.RevPlayException("Failed to load database config", e);
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            LoggerUtil.logError("Error creating connection", e);
            throw new com.revplay.exception.RevPlayException("Database Connection Failed", e);
        }
    }
}
