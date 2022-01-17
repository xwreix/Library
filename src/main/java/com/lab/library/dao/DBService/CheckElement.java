package com.lab.library.dao.DBService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckElement {

    public static int countGivenBooks(String email, Connection connection) {
        int result = 0;
        try {
            String SQL = "SELECT COUNT(*) FROM issue\n" +
                    "JOIN reader ON reader.id = issue.readerid\n" +
                    "JOIN bookcopy ON bookcopy.id = issue.bookcopyid\n" +
                    "JOIN book ON book.id = bookcopy.bookid\n" +
                    "WHERE reader.email = ? AND returndate IS NULL";
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            result = resultSet.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
            //TODO log
        }

        return result;
    }
}
