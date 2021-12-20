package com.lab.library;

import com.lab.library.dao.*;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "frontServlet", value = "/front-servlet")
public class FrontServlet extends HttpServlet {
    private ConnectionPool connectionPool;

    public void init() {
        try {
            connectionPool = ConnectionPoolRealiz.create
                    ("jdbc:postgresql://localhost:5432/library?characterEncoding=UTF8&useUnicode=true",
                            "postgres", "mnr1209");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Not successful connection");
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String surname = req.getParameter("surname");
        String name = req.getParameter("name");
        String email = req.getParameter("email");

        Connection connection = connectionPool.getConnection();
        DBAction.insertIntoDB(surname, name, email, connection);
        ResultSet resultSet = DBAction.select(connection);
        PrintWriter printWriter = resp.getWriter();
        write(printWriter, resultSet);
        printWriter.println("Привет");
    }

    public void destroy() {
    }

    private void write(PrintWriter printWriter, ResultSet resultSet) {
        try {
            while (resultSet.next()) {
                String name = resultSet.getString(2);
                String surname = resultSet.getString(3);

                printWriter.println(name + " " + surname);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}