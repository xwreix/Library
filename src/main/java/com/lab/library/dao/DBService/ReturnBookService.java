package com.lab.library.dao.DBService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.List;

public class ReturnBookService {
    HttpServletRequest request;
    Connection connection;

    public ReturnBookService(HttpServletRequest request, Connection connection) {
        this.request = request;
        this.connection = connection;
    }

    public void insertDamage(int bookCopyId, String paramId) throws SQLException {
        String SQL = null;

        SQL = "SELECT damage FROM bookCopy WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(SQL);
        statement.setInt(1, bookCopyId);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        String damageEx = resultSet.getString(1);
        if (damageEx == null) {
            damageEx = "";
        }

        SQL = "UPDATE  bookCopy\n" +
                "SET damage = ?\n" +
                "WHERE id = ?;";
        statement = connection.prepareStatement(SQL);
        String damage = request.getParameter("damage" + paramId);
        statement.setString(1, damageEx + " " + damage);
        statement.setInt(2, bookCopyId);
        statement.executeUpdate();
    }

    public int getCopyId(int readerId, String bookName) throws SQLException {
        String SQL = "SELECT bookcopyID FROM issue\n" +
                "JOIN bookCopy on bookCopy.id = issue.bookCopyId\n" +
                "JOIN book on book.id = bookCopy.bookId\n" +
                "WHERE readerId = ? AND book.nameinrus = ?;\n";
        int copyId = 0;

        PreparedStatement statement = connection.prepareStatement(SQL);
        statement.setInt(1, readerId);
        statement.setString(2, bookName);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        copyId = resultSet.getInt(1);

        return copyId;
    }

    public void insertDamagePhotos(int bookCopyId, String paramId) throws ServletException, IOException, SQLException {
        String param = "damagePhotos" + paramId + "[]";
        String SQL = "INSERT INTO damagePhoto(img, copyid) VALUES (?, ?)";
        PreparedStatement statement;

        List<Part> parts = (List<Part>) request.getParts();
        for (Part part : parts) {
            if (part.getName().equalsIgnoreCase(param)) {
                InputStream inputStream = part.getInputStream();
                byte[] bytes = new byte[inputStream.available()];
                if (bytes.length != 0) {
                    statement = connection.prepareStatement(SQL);
                    statement.setBinaryStream(1, inputStream);
                    statement.setInt(2, bookCopyId);
                    statement.executeUpdate();
                }
            }
        }
    }

    public void updateIssue(int copyId, int readerId, String param) throws SQLException {
        String SQL = "UPDATE issue\n" +
                "SET returnDate = ?, rating = ?\n" +
                "WHERE readerId=? AND bookcopyid=? AND returnDate IS NULL;";
        PreparedStatement statement = connection.prepareStatement(SQL);
        statement.setDate(1, Date.valueOf(request.getParameter("returnDate")));

        String rating = request.getParameter("rating" + param);
        if (rating == null || rating.equals("")) {
            statement.setNull(2, Types.INTEGER);
        } else {
            statement.setInt(2, Integer.parseInt(rating));
        }

        statement.setInt(3, readerId);
        statement.setInt(4, copyId);
        statement.executeUpdate();
    }

}
