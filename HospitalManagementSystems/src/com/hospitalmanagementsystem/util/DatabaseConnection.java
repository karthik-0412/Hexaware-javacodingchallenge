package com.hospitalmanagementsystem.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection;

    private DatabaseConnection() {}

    public static Connection getConnection() {
        if (connection == null) {
            try {
                String[] config = PropertyUtil.getPropertyString().split("\\|");
                String url = config[0];
                String user = config[1];
                String password = config[2];

                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Connection Established");
            } catch (SQLException e) {
                System.err.println("Database Connection failed: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return connection;
    }
    
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;  
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Failed to close database connection: " + e.getMessage());
            }
        }
    }
}
