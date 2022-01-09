package com.lab.library.dao.DBService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

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
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            //TODO log
            e.printStackTrace();
            return false;
        } catch (ServletException | IOException e) {
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