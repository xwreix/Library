package com.lab.library.dao;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class GetFromDB {
    public static Map<Integer, String> getGenres(Connection connection){
        String SQL = "SELECT id, name FROM genre";
        Map<Integer, String> genres = new HashMap<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);

            while(resultSet.next()){
                genres.put(resultSet.getInt(1), resultSet.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //TODO log
        }

        return  genres;
    }

}
