package com.lab.library;

import com.lab.library.dao.*;

import java.io.*;
import java.sql.SQLException;
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
        String referer = request.getRequestURI();
        String url = "/main.jsp";

        switch (referer){
            case ("/library/addReader"):
                url = "/addReader.jsp";
                break;

        }

        try {
            getServletContext().getRequestDispatcher(url).forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest reqest, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        reqest.setCharacterEncoding("UTF-8");

//        String referer = null;
//        try {
//            referer = new URI(reqest.getHeader("referer")).getPath();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//
//        switch (referer) {
//            case "/addReader.jsp":
//                if(!InsertIntoDb.addReader(reqest, connectionPool.getConnection())){
//                    getServletContext().getRequestDispatcher("/addReader.jsp").forward(reqest, response);
//                };
//        }

    }

    public void destroy() {
    }

}