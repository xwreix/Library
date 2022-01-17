package com.lab.library.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConnectionPool {
    private String url;
    private String user;
    private String password;
    private static List<Connection> connectionPool;
    private static List<Connection> usedConnections = new ArrayList<>();
    private static int POOL_SIZE = 10;

    public ConnectionPool(String url, String user, String password, List<Connection> pool) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.connectionPool = pool;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public static ConnectionPool create()
            throws SQLException {
        List<Connection> pool = new ArrayList<>(POOL_SIZE);

        Properties props = new Properties();
        String fileName = "db.properties";

        InputStream inputStream = ConnectionPool.class.getClassLoader().getResourceAsStream(fileName);
        try {
            props.load(inputStream);
        } catch (IOException e) {
            //TODO log
        }

        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        for (int i = 0; i < POOL_SIZE; i++) {
            pool.add(createConnection(url, user, password));
        }

        return new ConnectionPool(url, user, password, pool);
    }

    public static Connection getConnection() {
        Connection connection = connectionPool.remove(connectionPool.size() - 1);
        usedConnections.add(connection);
        return connection;
    }

    public static boolean releaseConnection(Connection connection) {
        connectionPool.add(connection);
        return usedConnections.remove(connection);
    }


    private static Connection createConnection(String url, String user, String password)
            throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            //TODO log
        }
        return DriverManager.getConnection(url, user, password);
    }

    public int getSize() {
        return connectionPool.size() + usedConnections.size();
    }

}