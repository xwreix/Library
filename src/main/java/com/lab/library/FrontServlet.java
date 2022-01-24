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
import javax.mail.MessagingException;
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
import java.sql.SQLException;
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
                try {
                    request.setAttribute("genres", bookService.selectGenres(dataSource));
                    url = "/addBook.jsp";
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "Selecting genres exception", e);
                    request.setAttribute("result", "Не удалось создать пользователя.");
                    url = "/result.jsp";
                }
                break;
            case ("/library/readerList"):
                try {
                    request.setAttribute("readers", readerService.selectReaders(dataSource));
                    url = "/readerList.jsp";
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "Select reader exception", e);
                    request.setAttribute("result", "Не удалось получить список читателей");
                    url = "/result.jsp";
                }
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
                try {
                    status = readerService.checkReader(request.getParameter("readerEmail"), dataSource);
                    if (status == Status.AVAIlABLE) {
                        valid = true;
                    } else if (status == Status.NON_EXISTENT) {
                        message = "Читатель не существует";
                    } else if (status == Status.LOCKED) {
                        message = "Читатель не вернул все книги";
                    }
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "Check reader exception", e);
                    message = "Не удаётся получить информацию о читателе";
                }
                sendJSON(valid, message, response);
                break;
            case ("/library/checkBook"):
                try {
                    status = bookService.checkBook(request.getParameter("bookName"), dataSource);
                    if (status == Status.AVAIlABLE) {
                        valid = true;
                    } else if (status == Status.NON_EXISTENT) {
                        message = "Такой книги не существует";
                    } else if (status == Status.LOCKED) {
                        message = "В наличии нет доступных экзмепляров";
                    }
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "Checking book exception ", e);
                    message = "Не удаётся получить информацию о книгах";
                }
                sendJSON(valid, message, response);
                break;
            case ("/library/getCost"):
                try {
                    message = String.valueOf(bookService.selectCost(request.getParameter("bookName"), dataSource));
                    valid = true;
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "Selecting cost exception ", e);
                    message = "Не удаётся получить информацию о стоимости";
                }
                sendJSON(valid, message, response);
                break;
            case ("/library/countGivenBooks"):
                try {
                    status = readerService.checkReader(request.getParameter("readerEmail"), dataSource);
                    if (status == Status.AVAIlABLE) {
                        message = "У читателя нет книг для возврата";
                    } else if (status == Status.NON_EXISTENT) {
                        message = "Читатель не существует";
                    } else if (status == Status.LOCKED) {
                        valid = true;
                    }
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "Counting given books exception ", e);
                    message = "Не удаётся получить информацию о книгах читателя";
                }
                sendJSON(valid, message, response);
                break;
            case ("/library/getGivenBooks"):
                try {
                    List<BookCopy> books = readerService.selectAllBooks(request.getParameter("readerEmail"), dataSource);
                    sendObjJson(books, response);
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "Selecting all books exception", e);
                    sendObjJson(null, response);
                }
                break;
            case ("/library/profitability"):
                url = "/profitability.jsp";
                break;
            case ("/library/calcProfitability"):
                try {
                    Profitability profitability = issueService.calcProfit(Date.valueOf(request.getParameter("start")),
                            Date.valueOf(request.getParameter("finish")), dataSource);
                    sendObjJson(profitability, response);
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "Calculating profitability exception ");
                    sendObjJson(null, response);
                }
                break;
            case ("/library/getCopyInfo"):
                try {
                    BookCopy bookCopy = bookService.selectCopyInfo(Integer.parseInt(request.getParameter("id")), dataSource);
                    sendObjJson(bookCopy, response);
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "Selecting copy info exception ", e);
                    sendObjJson(null, response);
                }
                break;
            case ("/library/send"):
                try {
                    readerService.sendMails(dataSource);
                    url = "/main.jsp";
                    try {
                        request.setAttribute("booksImg", bookService.selectPopular(dataSource));
                    } catch (SQLException | IOException e) {
                        logger.log(Level.SEVERE, "Selecting popular exception ", e);
                        request.setAttribute("message", "Не удаётся получить данные о популярных книгах книгах");
                    }
                    try {
                        request.setAttribute("books", bookService.selectBooks(dataSource));
                    } catch (SQLException e) {
                        logger.log(Level.SEVERE, "Selecting books exception ", e);
                        request.setAttribute("message", "Не удаётся получить данные о книгах книгах");
                    }
                } catch (SQLException | MessagingException e) {
                    logger.log(Level.SEVERE, "Sending message exception ", e);
                    request.setAttribute("result", "Не удалось отправить уведомление");
                    url = "/result.jsp";
                }
                break;
            case ("/library"):
                url = "/main.jsp";
                try {
                    request.setAttribute("booksImg", bookService.selectPopular(dataSource));
                } catch (SQLException | IOException e) {
                    logger.log(Level.SEVERE, "Selecting popular exception ", e);
                    request.setAttribute("message", "Не удаётся получить данные о популярных книгах книгах");
                }
                try {
                    request.setAttribute("books", bookService.selectBooks(dataSource));
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "Selecting books exception ", e);
                    request.setAttribute("message", "Не удаётся получить данные о книгах книгах");
                }
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
        String url = null;

        if (referer != null) {
            switch (referer) {
                case "/library/addReader":
                    Reader reader = requestHandler.getReaderFromReq(request);
                    try {
                        readerService.addReader(reader, dataSource);
                        request.setAttribute("result", "Пользователь создан успешно.");
                    } catch (SQLException e) {
                        logger.log(Level.WARNING, "Insert reader exception ", e);
                        request.setAttribute("result", "Не удалось создать пользователя.");
                    }
                    url = "/result.jsp";
                    break;
                case "/library/addBook":
                    try {
                        Book book = requestHandler.getBookFromReq(request);
                        bookService.addBook(book, dataSource);
                        request.setAttribute("result", "Книга добавлена успешно.");
                    } catch (SQLException | ServletException | IOException e) {
                        logger.log(Level.SEVERE, "Adding book exception ", e);
                        request.setAttribute("result", "Не удалось добавить книгу");
                    }
                    url = "/result.jsp";
                    break;
                case "/library/issueBook":
                    Issue issue = requestHandler.getNewIssueFromReq(request);
                    try {
                        List<BookCopy> givenBooks = issueService.addNewIssue(issue, dataSource);
                        request.setAttribute("givenBooks", givenBooks);
                        url = "/givenBooks.jsp";
                    } catch (SQLException e) {
                        logger.log(Level.WARNING, "Adding new issue exception ", e);
                        request.setAttribute("result", "Не удалось создать заказ");
                        url = "/result.jsp";
                    }
                    break;
                case "/library/returnBook":
                    try {
                        Issue returned = requestHandler.getReturnedFromReq(request);
                        issueService.finishIssue(returned, dataSource);
                        request.setAttribute("result", "Возврат книг проведён успешно");
                    } catch (SQLException | ServletException | IOException e) {
                        logger.log(Level.SEVERE, "Returning book exception ", e);
                        request.setAttribute("result", "Не удалось провести возврат книг");
                    }
                    url = "/result.jsp";
                    break;
                case "/library/writeOff":
                    try {
                        bookService.writeOff(Integer.parseInt(request.getParameter("copyId")), dataSource);
                        request.setAttribute("result", "Копия удалена");
                    } catch (SQLException e) {
                        logger.log(Level.SEVERE, "Writing off exception ", e);
                        request.setAttribute("result", "Не удалось удалить копию");
                    }
                    url = "/result.jsp";
                    break;
                default:
                    url = "/result.jsp";
            }
        }

        if (url != null) {
            try {
                getServletContext().getRequestDispatcher(url).forward(request, response);
            } catch (ServletException e) {
                logger.log(Level.SEVERE, "Forward exception ", e);
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

    private void sendJSONError(boolean flag, HttpServletResponse response) {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
                .add("flag", flag);
        JsonObject jsonObject = objectBuilder.build();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            response.getWriter().write(jsonObject.toString());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Writing JSON exception ", e);
        }
    }

    private void sendObjJson(Object object, HttpServletResponse response) {
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
