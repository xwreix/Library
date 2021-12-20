package com.lab.library.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBAction {
    public static ResultSet select(Connection connection) {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * from reader");
        } catch (
                SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    public static void insertIntoDB(String surname, String name, String email, Connection connection){
        Statement statement = null;

        try {
            statement = connection.createStatement();

            statement.executeUpdate
                    ("INSERT INTO reader (surname, name, email) values" +
                            " ('"+ surname + "', '"+ name + "', '"+ email + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
