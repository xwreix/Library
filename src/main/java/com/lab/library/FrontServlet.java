package com.lab.library;

import com.lab.library.dao.ConnectionPool;
import com.lab.library.dao.ConnectionPoolRealiz;
import com.lab.library.dao.GetFromDB;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "frontServlet", value = "/library/*")
@MultipartConfig
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

        switch (referer) {
            case ("/library/addReader"):
                url = "/addReader.jsp";
                break;
            case ("/library/addBook"):
                url = "/addBook.jsp";
                request.setAttribute("genres", GetFromDB.getGenres(connectionPool.getConnection()));
                break;

        }

        try {
            getServletContext().getRequestDispatcher(url).forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        Part filePart = request.getPart("file");
        InputStream fileContent = filePart.getInputStream();
        try {
            PreparedStatement ps = connectionPool.getConnection().prepareStatement("INSERT INTO images(img, id) VALUES (?, ?)");
            ps.setBinaryStream(1, fileContent);
            ps.setInt(2, 1);
            ps.executeUpdate();
            System.out.println("Done");

            ps = connectionPool.getConnection().prepareStatement("SELECT img FROM images WHERE id = ?");
            ps.setInt(1, 1);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                byte[] imgBytes = rs.getBytes(1);
                response.setContentType("image/jpg");
                response.getOutputStream().write(imgBytes);
                response.getOutputStream().flush();
                response.getOutputStream().close();
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        String referer = null;
//        try {
//            referer = new URI(request.getHeader("referer")).getPath();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//
//        switch (referer) {
//            case "/library/addReader":
//                if (InsertIntoDb.addReader(request, connectionPool.getConnection())) {
//                    request.setAttribute("result", "Пользователь создан успешно.");
//                } else {
//                    request.setAttribute("result", "Не удалось создать пользователя.");
//                }
//                getServletContext().getRequestDispatcher("/result.jsp").forward(request, response);
//                break;
//            case "/library/addBook":
//
//
//                break;
//
//                        //InsertIntoDb.insertImage(request);
//
//                    }
//
    }

    public void destroy() {
    }

}