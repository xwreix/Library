package com.lab.library.dao;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;

public class InsertIntoDb {

    public static boolean addReader(HttpServletRequest request, Connection connection){
        String SQL = "INSERT INTO reader" +
                " (surname, name, patronymic, passportNumber, dateOfBirth, address, email)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {

            statement.setString(1, request.getParameter("surname"));
            statement.setString(2, request.getParameter("name"));
            statement.setString(3, request.getParameter(("patronymic")));
            statement.setString(4, request.getParameter("passportNumber"));
            statement.setDate(5, Date.valueOf(request.getParameter("dateOfBirth")));
            statement.setString(6, request.getParameter("address"));
            statement.setString(7, request.getParameter("email"));

            statement.executeUpdate();
        } catch (Exception e) {
            //TODO log
            return false;
        }
        return true;

    }

}
