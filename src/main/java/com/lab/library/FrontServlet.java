package com.lab.library;

import com.google.gson.Gson;
import com.lab.library.beans.*;
import com.lab.library.service.BookService;
import com.lab.library.service.IssueService;
import com.lab.library.service.ReaderService;
import com.lab.library.service.RequestHandler;
import org.apache.commons.dbcp2.BasicDataSource;

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
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "frontServlet", value = "/library/*")
@MultipartConfig
public class FrontServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(FrontServlet.class.getName());
    private final ReaderService readerService = new ReaderService();
    private final BookService bookService = new BookService();
    private final IssueService issueService = new IssueService();
    private final RequestHandler requestHandler = new RequestHandler();
    private static BasicDataSource dataSource = null;

    public void init() {
        connectToDB();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String referer = request.getRequestURI();
        String url = null;
        Status status;
        boolean valid = false;
        String message = "";

        switch (referer) {
            case ("/library/addReader"):
                url = "/addReader.jsp";
                break;
            case ("/library/addBook"):
                url = "/addBook.jsp";
                request.setAttribute("genres", bookService.selectGenres(dataSource));
                break;
            case ("/library/readerList"):
                url = "/readerList.jsp";
                request.setAttribute("readers", readerService.selectReaders(dataSource));
                break;
            case ("/library/issueBook"):
                url = "/issueBook.jsp";
                break;
            case ("/library/returnBook"):
                url = "/returnBook.jsp";
                break;
            case ("/library/writeOff"):
                url = "/writeOff.jsp";
                break;
            case ("/library/checkReaderExistence"):
                status = readerService.checkReader(request.getParameter("readerEmail"), dataSource);
                if (status == Status.AVAIlABLE) {
                    valid = true;
                } else if (status == Status.NON_EXISTENT) {
                    message = "Читатель не существует";
                } else if (status == Status.LOCKED) {
                    message = "Читатель не вернул все книги";
                }
                sendJSON(valid, message, response);
                break;
            case ("/library/checkBook"):
                    status = bookService.checkBook(request.getParameter("bookName"), dataSource);
                    if (status == Status.AVAIlABLE) {
                        valid = true;
                    } else if (status == Status.NON_EXISTENT) {
                        message = "Такой книги не существует";
                    } else if (status == Status.LOCKED) {
                        message = "В наличии нет доступных экзмепляров";
                    }
                sendJSON(valid, message, response);
                break;
            case ("/library/getCost"):
                double cost = bookService.selectCost(request.getParameter("bookName"), dataSource);
                sendJSON(true, String.valueOf(cost), response);
                break;
            case ("/library/countGivenBooks"):
                status = readerService.checkReader(request.getParameter("readerEmail"), dataSource);
                if (status == Status.AVAIlABLE) {
                    message = "У читателя нет книг для возврата";
                } else if (status == Status.NON_EXISTENT) {
                    message = "Читатель не существует";
                } else if (status == Status.LOCKED) {
                    valid = true;
                }
                sendJSON(valid, message, response);
                break;
            case ("/library/getGivenBooks"):
                List<BookCopy> books = readerService.selectAllBooks(request.getParameter("readerEmail"), dataSource);
                sendObjJson(books, response);
                break;
            case("/library/profitability"):
                url = "/profitability.jsp";
                break;
            case ("/library/calcProfitability"):
                Profitability profitability = issueService.calcProfit(Date.valueOf(request.getParameter("start")),
                        Date.valueOf(request.getParameter("finish")), dataSource);
                sendObjJson(profitability, response);
                break;
            case ("/library/getCopyInfo"):
                BookCopy bookCopy = bookService.selectCopyInfo(Integer.parseInt(request.getParameter("id")), dataSource);
                sendObjJson(bookCopy, response);
                break;
            case ("/library/send"):
                readerService.sendMails(dataSource);
                url = "/main.jsp";
                request.setAttribute("booksImg", bookService.selectPopular(dataSource));
                request.setAttribute("books", bookService.selectBooks(dataSource));
                break;
            case ("/library"):
                url = "/main.jsp";
                request.setAttribute("booksImg", bookService.selectPopular(dataSource));
                request.setAttribute("books", bookService.selectBooks(dataSource));
                break;
        }

        if (url != null) {
            try {
                getServletContext().getRequestDispatcher(url).forward(request, response);
            } catch (ServletException e) {
                logger.log(Level.SEVERE, "Forward exception ", e);
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
            logger.log(Level.SEVERE, "Referer exception ", e);
        }

        if (referer != null) {
            switch (referer) {
                case "/library/addReader":
                    Reader reader = requestHandler.getReaderFromReq(request);
                    if (readerService.addReader(reader, dataSource)) {
                        request.setAttribute("result", "Пользователь создан успешно.");
                    } else {
                        request.setAttribute("result", "Не удалось создать пользователя.");
                    }
                    getServletContext().getRequestDispatcher("/result.jsp").forward(request, response);
                    break;
                case "/library/addBook":
                    Book book = requestHandler.getBookFromReq(request);
                    if (bookService.addBook(book, dataSource)) {
                        request.setAttribute("result", "Книга добавлена успешно.");
                    } else {
                        request.setAttribute("result", "Не удалось добавить книгу");
                    }
                    getServletContext().getRequestDispatcher("/result.jsp").forward(request, response);
                    break;
                case "/library/issueBook":
                    Issue issue = requestHandler.getNewIssueFromReq(request);
                    List<BookCopy> givenBooks = issueService.addNewIssue(issue, dataSource);
                    request.setAttribute("givenBooks", givenBooks);
                    getServletContext().getRequestDispatcher("/givenBooks.jsp").forward(request, response);
                    break;
                case "/library/returnBook":
                    Issue returned = requestHandler.getReturnedFromReq(request);
                    if (issueService.finishIssue(returned, dataSource)) {
                        request.setAttribute("result", "Возврат книг проведён успешно");
                    } else {
                        request.setAttribute("result", "Не удалось провести возврат книг");
                    }
                    getServletContext().getRequestDispatcher("/result.jsp").forward(request, response);
                    break;
                case "/library/writeOff":
                    if(bookService.writeOff(Integer.parseInt(request.getParameter("copyId")), dataSource)){
                        request.setAttribute("result", "Копия удалена");
                    } else {
                        request.setAttribute("result", "Не удалось удалить копию");
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

    private void sendJSON(boolean valid, String message, HttpServletResponse response) {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
                .add("valid", valid)
                .add("value", message);
        JsonObject jsonObject = objectBuilder.build();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            response.getWriter().write(jsonObject.toString());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Writing JSON exception ", e);
        }
    }

    private void sendObjJson(Object object, HttpServletResponse response){
        String json = new Gson().toJson(object);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            response.getWriter().write(json);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Writing JSON exception ", e);
        }
    }

    private void connectToDB() {
        dataSource = new BasicDataSource();

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Driver exception ", e);
        }

        Properties props = new Properties();
        String fileName = "db.properties";
        InputStream inputStream = FrontServlet.class.getClassLoader().getResourceAsStream(fileName);
        try {
            props.load(inputStream);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Loading properties exception ", e);
        }

        dataSource.setUrl(props.getProperty("db.url"));
        dataSource.setUsername(props.getProperty("db.user"));
        dataSource.setPassword(props.getProperty("db.password"));

        dataSource.setMinIdle(5);
        dataSource.setMaxIdle(10);
        dataSource.setMaxTotal(25);
    }
}
