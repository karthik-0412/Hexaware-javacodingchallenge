package com.hospitalmanagementsystem.util;

import java.io.IOException;
import java.util.Properties;

public class PropertyUtil {

    public static String getPropertyString() {
        Properties props = new Properties();
        try {
        	
        	props.load(PropertyUtil.class.getClassLoader().getResourceAsStream("com/hospitalmanagementsystem/util/DB.properties"));
            String hostname = props.getProperty("hostname");
            String port = props.getProperty("port");
            String dbname = props.getProperty("dbname");
            String username = props.getProperty("username");
            String password = props.getProperty("password");

            return String.format(
                "jdbc:mysql://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&logger=com.mysql.cj.log.StandardLogger|%s|%s",
                hostname, port, dbname, username, password
            );

        } catch (IOException e) {
            throw new RuntimeException("Failed to load database properties", e);
        }
    }
}
