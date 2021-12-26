package com.lab.library;

import com.lab.library.dao.*;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "frontServlet", value = "/library/*")
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
        resp.setContentType("text/html;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");


        System.out.println(req.getParameter("patronymic"));
        String referer = null;
        try {
            referer = new URI(req.getHeader("referer")).getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        switch (referer) {
            case "/addReader.jsp":
//                if (InsertIntoDb.addReader(req, connectionPool.getConnection())) {
//                    System.out.println(req.getParameter("patronymic"));
//                    getServletContext().setAttribute("added", "Новый читатель");
//                    getServletContext().getRequestDispatcher("/successfully.jsp").forward(req, resp);
//                }
                System.out.println("RED");

        }

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