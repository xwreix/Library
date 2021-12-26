package com.lab.library.dao;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class InsertIntoDb {

    public static boolean addReader(HttpServletRequest request, Connection connection){
        Statement statement = null;

        try {
            statement = connection.createStatement();

            statement.executeUpdate
                    ("INSERT INTO reader (surname, name, patronymic, passportNumber, dateofBirth, city, street, building, flat, email) values" +
                            " ('"+ request.getParameter("surname") + "', '"+
                            request.getParameter("name") + "', '"+
                            request.getParameter("patronymic") + "', '"+
                            request.getParameter("passport") + "', '"+
                            request.getParameter("dateOfBirth") + "', '"+
                            request.getParameter("city") + "', '"+
                            request.getParameter("street") + "', '"+
                            request.getParameter("building") + "', '"+
                            request.getParameter("flat") + "', '"+
                            request.getParameter("email") + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

}
