//package com.lab.library.dao.DBService;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.Part;
//import java.io.IOException;
//import java.io.InputStream;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.Enumeration;
//import java.util.List;
//import java.util.Objects;
//import java.util.regex.Pattern;
//
//public class AddBookService {
//    private HttpServletRequest request;
//    private Connection connection;
//
//    public AddBookService(HttpServletRequest request, Connection connection) {
//        this.request = request;
//        this.connection = connection;
//    }
//
//    public int insertIntoBook() throws SQLException {
//        String SQL = "INSERT INTO book(nameInRus, originalName, cost, priceForDay, publYear, pageAmount) " +
//                "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
//
//        PreparedStatement statement = connection.prepareStatement(SQL);
//        statement.setString(1, request.getParameter("nameInRus"));
//
//        String originalName = request.getParameter("originalName");
//        if (Objects.equals(originalName, "")) {
//            originalName = null;
//        }
//        statement.setString(2, originalName);
//
//        statement.setDouble(3, Double.parseDouble(request.getParameter("cost")));
//        statement.setDouble(4, Double.parseDouble(request.getParameter("priceForDay")));
//
//        String publYear = request.getParameter("publYear");
//        if (Objects.equals(publYear, "")) {
//            statement.setNull(5, Types.INTEGER);
//        } else {
//            statement.setInt(5, Integer.parseInt(publYear));
//        }
//
//        String pageAmount = request.getParameter("pageAmount");
//        if (Objects.equals(pageAmount, "")) {
//            statement.setNull(6, Types.INTEGER);
//        } else {
//            statement.setInt(6, Integer.parseInt(pageAmount));
//        }
//
//        ResultSet resultSet = statement.executeQuery();
//        int id = 0;
//        while (resultSet.next()) {
//            id = resultSet.getInt(1);
//        }
//
//        return id;
//    }
//
//    public void insertIntoCover(int id) throws ServletException, IOException, SQLException {
//        String SQL = "INSERT INTO cover (bookId, img) VALUES (?, ?)";
//        PreparedStatement statement;
//
//        List<Part> parts = (List<Part>) request.getParts();
//        for (Part part : parts) {
//            if (part.getName().equalsIgnoreCase("covers[]")) {
//                InputStream inputStream = part.getInputStream();
//                statement = connection.prepareStatement(SQL);
//                statement.setInt(1, id);
//                statement.setBinaryStream(2, inputStream);
//                statement.executeUpdate();
//            }
//        }
//    }
//
//    public void insertIntoGenre(int id) throws SQLException {
//        String SQL = "INSERT INTO BooksGenres (genreId, bookId) VALUES (?, ?)";
//        PreparedStatement statement;
//
//        String[] genresId = request.getParameterValues("genres");
//        for (String genre : genresId) {
//            statement = connection.prepareStatement(SQL);
//            statement.setInt(1, Integer.parseInt(genre));
//            statement.setInt(2, id);
//            statement.executeUpdate();
//        }
//    }
//
//    public void insertIntoBookCopy(int id) throws SQLException {
//        String SQL = "INSERT INTO bookCopy(bookId, registrDate) VALUES (?, ?)";
//        PreparedStatement statement;
//
//        Date date = Date.valueOf(request.getParameter("registrDate"));
//        int amount = Integer.parseInt(request.getParameter("amount"));
//        for (int i = 0; i < amount; i++) {
//            statement = connection.prepareStatement(SQL);
//            statement.setInt(1, id);
//            statement.setDate(2, date);
//            statement.executeUpdate();
//        }
//    }
//
//    public void insertAuthor(int id) throws SQLException, ServletException, IOException {
//        Enumeration<String> params = request.getParameterNames();
//        List<String> authors = new ArrayList<>();
//        while (params.hasMoreElements()) {
//            String el = params.nextElement();
//            if (Pattern.matches("author[0-9]*", el)) {
//                authors.add(el);
//            }
//        }
//
//        String SQL;
//
//        for (String param : authors) {
//            SQL = "SELECT id, name FROM author WHERE name = ?";
//            PreparedStatement statement;
//
//            statement = connection.prepareStatement(SQL);
//            statement.setString(1, request.getParameter(param));
//            ResultSet resultSet = statement.executeQuery();
//
//            int idAuthor = 0;
//            if (!resultSet.next()) {
//                SQL = "INSERT INTO author(name) VALUES (?) RETURNING id";
//                statement = connection.prepareStatement(SQL);
//                statement.setString(1, request.getParameter(param));
//                resultSet = statement.executeQuery();
//
//                while (resultSet.next()) {
//                    idAuthor = resultSet.getInt("id");
//                }
//            } else {
//                idAuthor = resultSet.getInt("id");
//            }
//
//
//            SQL = "INSERT INTO authorsBooks(authorId, bookId) VALUES (?, ?)";
//            statement = connection.prepareStatement(SQL);
//            statement.setInt(1, idAuthor);
//            statement.setInt(2, id);
//            statement.executeUpdate();
//
//            SQL = "INSERT INTO authorPhoto (authorId, img) VALUES (?, ?)";
//
//            String photosParam = "authorPhotos" + param.substring(6) + "[]";
//            List<Part> parts = (List<Part>) request.getParts();
//            for (Part part : parts) {
//                if (part.getName().equalsIgnoreCase(photosParam)) {
//                    InputStream inputStream = part.getInputStream();
//                    byte[] bytes = new byte[inputStream.available()];
//                    if (bytes.length != 0) {
//                        statement = connection.prepareStatement(SQL);
//                        statement.setInt(1, idAuthor);
//                        statement.setBinaryStream(2, inputStream);
//                        statement.executeUpdate();
//                    }
//                }
//            }
//        }
//    }
//
//}
