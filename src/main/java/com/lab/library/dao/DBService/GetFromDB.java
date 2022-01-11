package com.lab.library.dao.DBService;

import com.lab.library.dao.beans.Book;
import com.lab.library.dao.beans.Reader;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class GetFromDB {

    public static Map<Integer, String> setGenres(Connection connection) {
        String SQL = "SELECT id, name FROM genre";
        Map<Integer, String> genres = new HashMap<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                genres.put(resultSet.getInt(1), resultSet.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //TODO log
        }


        return genres;
    }

    public static List<Book> setBooks(Connection connection) {
        List<Book> books = new ArrayList<>();

        try {
            connection.setAutoCommit(false);

            String SQL = "SELECT id, nameInRus, publYear from book";
            PreparedStatement statement = connection.prepareStatement(SQL);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("nameInRus");
                int year = resultSet.getInt("publYear");

                String query = "SELECT COUNT(*) FROM bookcopy WHERE bookId = ?";
                statement = connection.prepareStatement(query);
                statement.setInt(1, id);
                ResultSet result = statement.executeQuery();
                int totalAmount = 0;
                while(result.next()){
                    totalAmount = result.getInt(1);
                }

                query = "select count(*) from issue\n" +
                        " join bookcopy on bookcopy.id = issue.bookcopyid\n" +
                        " where bookcopy.bookId = ? AND returnDate is NULL";
                statement = connection.prepareStatement(query);
                statement.setInt(1, id);
                result = statement.executeQuery();
                int availableAmount = totalAmount;
                while(result.next()){
                    availableAmount -= result.getInt(1);
                }

                List<String> genres = new ArrayList<>();
                query = "select name from genre\n" +
                        "join booksgenres on booksgenres.genreId = genre.id\n" +
                        "where bookId = ?";
                statement = connection.prepareStatement(query);
                statement.setInt(1, id);
                result = statement.executeQuery();
                while (result.next()){
                    genres.add(result.getString("name"));
                }

                books.add(new Book(id, name, genres, year, totalAmount, availableAmount));
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }

        Collections.sort(books);
        return books;
    }

    public static List<Reader> setReaders(Connection connection){
        List<Reader> readers = new ArrayList<>();

        String SQL = "SELECT id, surname, name, dateOfBirth, address, email FROM reader ORDER BY surname";

        try {
            PreparedStatement statement = connection.prepareStatement(SQL);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String surname = resultSet.getString("surname");
                String name = resultSet.getString("name");
                Date dateOfBirth = resultSet.getDate("dateOfBirth");
                String address = resultSet.getString("address");
                String email = resultSet.getString("email");

                readers.add(new Reader(id, surname, name, dateOfBirth, address, email));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return readers;
    }

    public static double getCost(String bookName, Connection connection){
        double result = 0;
        String SQL = "SELECT priceForDay FROM book WHERE nameInRus = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setString(1, bookName);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            result = resultSet.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
