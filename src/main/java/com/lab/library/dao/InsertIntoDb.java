package com.lab.library.dao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
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

            String SQL = "INSERT INTO book(nameInRus, originalName, cost, priceForDay, publYear, pageAmount) " +
                    "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";

            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setString(1, request.getParameter("nameInRus"));

            String originalName = setNull(request.getParameter("originalName"));
            statement.setString(2, originalName);

            statement.setDouble(3, Double.parseDouble(request.getParameter("cost")));
            statement.setDouble(4, Double.parseDouble(request.getParameter("priceForDay")));

            String publYear = request.getParameter("publYear");
            if (Objects.equals(publYear, "")) {
                statement.setNull(5, Types.INTEGER);
            } else {
                statement.setString(5, publYear);
            }

            String pageAmount = request.getParameter("pageAmount");
            if (Objects.equals(pageAmount, "")) {
                statement.setNull(6, Types.INTEGER);
            } else {
                statement.setString(6, pageAmount);
            }

            ResultSet resultSet = statement.executeQuery();
            int id = 0;
            while (resultSet.next()) {
                id = resultSet.getInt(1);
            }

            SQL = "INSERT INTO cover (bookId, img) VALUES (?, ?)";
            List<Part> parts = (List<Part>) request.getParts();
            for (Part part : parts) {
                if (part.getName().equalsIgnoreCase("covers[]")) {
                    InputStream inputStream = part.getInputStream();
                    statement = connection.prepareStatement(SQL);
                    statement.setInt(1, id);
                    statement.setBinaryStream(2, inputStream);
                    statement.executeUpdate();
                }
            }

            SQL = "INSERT INTO BooksGenres (genreId, bookId) VALUES (?, ?)";
            String[] genresId = request.getParameterValues("genres");
            for (String genre : genresId) {
                statement = connection.prepareStatement(SQL);
                statement.setInt(1, Integer.parseInt(genre));
                statement.setInt(2, id);
                statement.executeUpdate();
            }

            SQL = "INSERT INTO bookCopy(bookId, registrDate) VALUES (?, ?)";
            Date date = Date.valueOf(request.getParameter("registrDate"));
            int amount = Integer.parseInt(request.getParameter("amount"));
            for (int i = 0; i < amount; i++) {
                statement = connection.prepareStatement(SQL);
                statement.setInt(1, id);
                statement.setDate(2, date);
                statement.executeUpdate();
            }

            Enumeration<String> params = request.getParameterNames();
            List<String> authors = new ArrayList<>();
            while(params.hasMoreElements()){
                String el = params.nextElement();
                if(Pattern.matches("author[0-9]*", el)){
                    authors.add(el);
                }
            }
            for(String param: authors){
                String photosParam = "authorPhotos" + param.substring(6) + "[]";
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            //TODO log
            e.printStackTrace();
            return false;
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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

//   Collection<Part> filePart = request.getParts();
//                try {
//                    PreparedStatement ps = connectionPool.getConnection().prepareStatement("INSERT INTO images(img, id) VALUES (?, ?)");
//                    ps.setBinaryStream(1, fileContent);
//                    ps.setInt(2, 1);
//                    ps.executeUpdate();
//
//                    ps = connectionPool.getConnection().prepareStatement("SELECT img FROM images WHERE id =?");
//                    ps.setInt(1, 12);
//                    ResultSet rs = ps.executeQuery();
//                    while (rs.next()) {
//                        byte[] imgBytes = rs.getBytes(12);
//                        response.setContentType("image/jpg");
//                        response.getOutputStream().write(imgBytes);
//                        response.getOutputStream().flush();
//                        response.getOutputStream().close();
//                    }
//                    rs.close();
//                    ps.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//               break;

//InsertIntoDb.insertImage(request);