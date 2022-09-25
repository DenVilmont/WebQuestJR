package dev.webQuest.servlet;

import dev.webQuest.SQLiteHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "ServletQuestEngine", value = "/questEngine")
public class ServletQuestEngine extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(ServletQuestEngine.class);
    private static final SQLiteHandler sqLiteHandler = SQLiteHandler.getInstance();

//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        LOGGER.info("ServletQuestEngine do Post!");
//        String answerID = req.getParameter("answerID");
//        HttpSession session = req.getSession();
//        session.setAttribute("currentQuestion", sqLiteHandler.getNextQuestionID(answerID));
//        getServletContext().getRequestDispatcher("/quest.jsp").forward(req, resp);
//    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("ServletQuestEngine do Get!");
        String answerID = req.getParameter("answerID");
        HttpSession session = req.getSession();
        session.setAttribute("currentQuestion", sqLiteHandler.getNextQuestionID(answerID));
        getServletContext().getRequestDispatcher("/quest.jsp").forward(req, resp);
    }

    @Override
    public void init() throws ServletException {
        LOGGER.info("ServletQuestEngine was inited!");
        super.init();
    }

    @Override
    public void destroy() {
        LOGGER.info("ServletQuestEngine was destroyed");
        super.destroy();
    }
}