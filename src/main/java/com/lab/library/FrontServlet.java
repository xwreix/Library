package com.lab.library;

import com.lab.library.dao.DBManager;
import com.lab.library.dao.beans.Status;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@WebServlet(name = "frontServlet", value = "/library/*")
@MultipartConfig
public class FrontServlet extends HttpServlet {
    DBManager dbManager = new DBManager();

    public void init() {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String referer = request.getRequestURI();
        String url = null;

        switch (referer) {
            case ("/library/addReader"):
                url = "/addReader.jsp";
                break;
            case ("/library/addBook"):
                url = "/addBook.jsp";
                request.setAttribute("genres", dbManager.selectFromGenres());
                break;
            case ("/library/readerList"):
                url = "/readerList.jsp";
                request.setAttribute("readers", dbManager.selectFromReader());
                break;
            case ("/library/issueBook"):
                url = "/issueBook.jsp";
                break;
            case ("/library/checkReader"):
                Status status = dbManager.checkReader(request.getParameter("readerEmail"));
                boolean valid = false;
                String message = "";
                if (status == Status.AVAIlABLE) {
                    valid = true;
                } else if (status == Status.NON_EXISTENT){
                    message = "Читатель не существует";
                } else if(status == Status.LOCKED){
                    message = "Читатель не вернул все книги";
                }

                System.out.println(message);

                JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
                        .add("valid", valid)
                        .add("mess", message);
                JsonObject jsonObject = objectBuilder.build();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(jsonObject.toString());
                break;
            case ("/library"):
                url = "/main.jsp";
                request.setAttribute("books", dbManager.selectFromBook());
                break;

        }

        if (url != null) {

            try {
                getServletContext().getRequestDispatcher(url).forward(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        String referer = null;
        try {
            referer = new URI(request.getHeader("referer")).getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if (referer != null) {
            switch (referer) {
                case "/library/addReader":
                    if (dbManager.addReader(request)) {
                        request.setAttribute("result", "Пользователь создан успешно.");
                    } else {
                        request.setAttribute("result", "Не удалось создать пользователя.");
                    }
                    getServletContext().getRequestDispatcher("/result.jsp").forward(request, response);
                    break;
                case "/library/addBook":
                    if (dbManager.addBook(request)) {
                        request.setAttribute("result", "Книга добавлена успешно.");
                    } else {
                        request.setAttribute("result", "Не удалось добавить книгу");
                    }
                    getServletContext().getRequestDispatcher("/result.jsp").forward(request, response);
                    break;
                default:
                    getServletContext().getRequestDispatcher("/result.jsp").forward(request, response);
            }
        }

    }

    public void destroy() {
    }
}
