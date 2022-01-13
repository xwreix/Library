package com.lab.library.dao.DBService;

import com.lab.library.dao.beans.Status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckElement {
    public static Status checkReader(String email, Connection connection) {
        Status result = null;

        try {
            connection.setAutoCommit(false);

            String SQL = "SELECT id FROM reader WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            int idReader = 0;
            if (!resultSet.next()) {
                return Status.NON_EXISTENT;
            } else {
                idReader = resultSet.getInt("id");
            }

            SQL = "SELECT COUNT(*) FROM issue WHERE readerId = ? and returnDate IS NULL";
            statement = connection.prepareStatement(SQL);
            statement.setInt(1, idReader);
            resultSet = statement.executeQuery();

            int count = 0;

            while (resultSet.next()) {
                count = resultSet.getInt(1);
            }

            if (count == 0) {
                result = Status.AVAIlABLE;
            } else {
                result = Status.LOCKED;
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
                //TODO log
            }
            e.printStackTrace();
            //TODO log
        }

        return result;
    }

    public static Status checkBook(String bookName, Connection connection) {
        Status status = null;

        try {
            connection.setAutoCommit(false);

            String SQL = "SELECT COUNT(*) FROM bookCopy\n" +
                    "JOIN book ON book.id = bookcopy.bookid\n" +
                    "WHERE book.nameInrus = ?;";
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setString(1, bookName);
            ResultSet resultSet = statement.executeQuery();

            int totalAmount = 0;
            while (resultSet.next()) {
                totalAmount = resultSet.getInt(1);
            }

            if (totalAmount == 0) {
                status = Status.NON_EXISTENT;
            } else {
                SQL = "SELECT COUNT (*) FROM bookcopy\n" +
                        "JOIN book ON book.id = bookcopy.bookid\n" +
                        "JOIN issue ON issue.bookcopyid = bookcopy.id\n" +
                        "WHERE book.nameInrus = ? AND returnDate is NULL";
                statement = connection.prepareStatement(SQL);
                statement.setString(1, bookName);
                resultSet = statement.executeQuery();

                int availableAmount = totalAmount;
                while (resultSet.next()) {
                    availableAmount -= resultSet.getInt(1);
                }

                if (availableAmount > 0) {
                    status = Status.AVAIlABLE;
                } else {
                    status = Status.LOCKED;
                }
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
                //TODO log
            }
            e.printStackTrace();
            //TODO log
        }

        return status;
    }

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
