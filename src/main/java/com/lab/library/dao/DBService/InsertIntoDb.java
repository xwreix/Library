package com.lab.library.dao.DBService;

import com.lab.library.dao.beans.BookCopy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class InsertIntoDb {

    public static boolean addReader(HttpServletRequest request, Connection connection) {
        String SQL = "INSERT INTO reader" +
                " (surname, name, patronymic, passportNumber, dateOfBirth, address, email)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {

            statement.setString(1, request.getParameter("surname"));
            statement.setString(2, request.getParameter("name"));

            String patronymic = setNull(request.getParameter("patronymic"));
            statement.setString(3, patronymic);

            String passNumb = setNull(request.getParameter("passportNumber"));
            statement.setString(4, passNumb);

            statement.setDate(5, Date.valueOf(request.getParameter("dateOfBirth")));

            String address = setNull(request.getParameter("address"));
            statement.setString(6, address);

            statement.setString(7, request.getParameter("email"));

            statement.executeUpdate();

            connection.close();
        } catch (Exception e) {
            //TODO log
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean addBook(HttpServletRequest request, Connection connection) {
        try {
            connection.setAutoCommit(false);
            AddBookService addBookService = new AddBookService(request, connection);

            int id = addBookService.insertIntoBook();

            addBookService.insertIntoCover(id);
            addBookService.insertIntoGenre(id);
            addBookService.insertAuthor(id);


            addBookService.insertIntoBookCopy(id);

            connection.commit();
        } catch (SQLException | ServletException | IOException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
                //TODO log
            }
            //TODO log
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static List<BookCopy> addIssue(HttpServletRequest request, Connection connection) {
        Enumeration<String> params = request.getParameterNames();
        List<String> books = new ArrayList<>();
        while (params.hasMoreElements()) {
            String el = params.nextElement();
            if (Pattern.matches("book[0-9]*", el)) {
                books.add(request.getParameter(el));
            }
        }


        List<BookCopy> givenBooks = new ArrayList<>();

        String SQL;

        try {
            connection.setAutoCommit(false);

            SQL = "SELECT id FROM reader WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setString(1, request.getParameter("email"));
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int idReader = resultSet.getInt(1);
            int discount = Integer.parseInt(request.getParameter("discount"));

            SQL = "INSERT INTO issue(readerID, bookCopyId, preliminaryDate, discount) VALUES (?, ?, ?, ?) ";
            statement = connection.prepareStatement(SQL);
            statement.setInt(1, idReader);
            statement.setDate(3, Date.valueOf(request.getParameter("preliminaryDate")));
            statement.setInt(4, discount);

            String getIdBook = "SELECT bookcopy.id, damage  FROM bookCopy " +
                    "JOIN book on book.id = bookCopy.bookId" +
                    " LEFT JOIN issue on bookCopy.id = issue.bookcopyid" +
                    " WHERE (nameInRus = ?) AND (issue.id IS NULL OR returnDate IS NOT NULl)" +
                    "LIMIT 1";
            PreparedStatement idStatement;

            for (String book : books) {
                idStatement = connection.prepareStatement(getIdBook);
                idStatement.setString(1, book);
                resultSet = idStatement.executeQuery();
                resultSet.next();
                int copyID = resultSet.getInt(1);
                String damage = resultSet.getString(2);

                SQL = "INSERT INTO issue(readerID, bookCopyId, preliminaryDate, discount) VALUES (?, ?, ?, ?) ";
                statement = connection.prepareStatement(SQL);
                statement.setInt(1, idReader);
                statement.setInt(2, copyID);
                statement.setDate(3, Date.valueOf(request.getParameter("preliminaryDate")));
                statement.setInt(4, discount);
                statement.executeUpdate();

                givenBooks.add(new BookCopy(copyID, book, damage, discount));
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

        return givenBooks;
    }

    public static boolean returnBook(HttpServletRequest request, Connection connection) {
        String SQL;
        try {
            connection.setAutoCommit(false);

            SQL = "SELECT id FROM reader WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setString(1, request.getParameter("email"));
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int readerId = resultSet.getInt(1);

            Enumeration<String> params = request.getParameterNames();
            List<String> booksParams = new ArrayList<>();
            while (params.hasMoreElements()) {
                String el = params.nextElement();
                if (Pattern.matches("book[0-9]*", el)) {
                    booksParams.add(el);
                }
            }
            ReturnBookService returnBookService = new ReturnBookService(request, connection);

            for (String book : booksParams) {
                String paramId = book.substring(4);
                String bookName = request.getParameter(book);
                int copyId = returnBookService.getCopyId(readerId, bookName);
                returnBookService.insertDamage(copyId, paramId);
                returnBookService.insertDamagePhotos(copyId, paramId);
                returnBookService.updateIssue(copyId, readerId, paramId);
            }


            connection.commit();
        } catch (SQLException | ServletException | IOException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
                //TODO log
            }
            e.printStackTrace();
            //TODO log
            return false;
        }

        return true;
    }

    private static String setNull(String param) {
        if (Objects.equals(param, "")) {
            param = null;
        }
        return param;
    }

}

