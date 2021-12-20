//package com.lab.library.dao;
//
//import java.sql.*;
//import java.util.Properties;
//
//public class ConnectionPooll {
//    private Connection connection;
//
//    public ConnectionPooll() {
//        String URL = "jdbc:postgresql://localhost/library";
//        final String USER = "postgres";
//        final String PASS = "mnr1209";
//
//        this.connection = null;
//
//        try {
//            Class.forName("org.postgresql.Driver");
//
//        } catch (ClassNotFoundException e) {
//            System.out.println("Driver not found");
//            e.printStackTrace();
//        }
//
//        Properties info = new Properties();
//        info.setProperty("user",USER);
//        info.setProperty("password",PASS);
//        info.setProperty("useUnicode","true");
//        info.setProperty("characterEncoding","utf8");
//
//        try {
//            connection = DriverManager.getConnection(URL, info);
//        } catch (SQLException e) {
//            System.out.println("Connection failed");
//            e.printStackTrace();
//        }
//
//        if (connection != null) {
//            System.out.println("Successfully");
//        }
//    }
//
//
//    public void selectFromDB(){
//
//    }
//
//    public void insertIntoDB(String surname, String name, String email){
//        Statement statement = null;
//
//        try {
//            statement = connection.createStatement();
//
//            statement.executeUpdate
//                    ("INSERT INTO reader (surname, name, email) values" +
//                            " ('"+ surname + "', '"+ name + "', '"+ email + "')");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}
