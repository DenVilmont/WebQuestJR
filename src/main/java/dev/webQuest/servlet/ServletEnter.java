package dev.webQuest.servlet;

import dev.webQuest.SQLiteHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ServletEnter", value = "/enter")
public class ServletEnter extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(ServletEnter.class);
    private static final SQLiteHandler sqLiteHandler = SQLiteHandler.getInstance();

    @Override
    public void init() throws ServletException {
        LOGGER.info("ServletEnter was inited!");
        super.init();
    }

    @Override
    public void destroy() {
        LOGGER.info("ServletEnter was destroyed");
        super.destroy();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("ServletEnter do Post!");
        HttpSession session = req.getSession();

        LOGGER.info("User name in session: {}", session.getAttribute("Login"));

        if (validateUserName(session.getAttribute("Login"))){
            if (session.getAttribute("currentQuestion") == null){
                session.setAttribute("currentQuestion", sqLiteHandler.getStartQuestionID());
            }
            getServletContext().getRequestDispatcher("/quest.jsp").forward(req, resp);

        }else {
            String userName = req.getParameter("Login");
            LOGGER.info("User name in parameters: {}", userName);
            if (validateUserName(userName)){
                session.setAttribute("Login", userName);
                LOGGER.info("Inserted user name in session");
                LOGGER.info("User name in session: {}", userName);
                if (session.getAttribute("currentQuestion") == null){
                    session.setAttribute("currentQuestion", sqLiteHandler.getStartQuestionID());
                }
                getServletContext().getRequestDispatcher("/quest.jsp").forward(req, resp);

            }else {
                resp.sendRedirect("index.jsp");
            }
        }
    }


    private boolean validateUserName(Object obj){
        String name;
        if (obj instanceof String){
            name = String.valueOf(obj);
            if (name != null
                    && !name.isBlank()
                    && !name.isEmpty()){
                return true;
            }
        }
        return false;
    }
}