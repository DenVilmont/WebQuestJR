package dev.webQuest.servlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ServletRenovar", value = "/renovar")
public class ServletRenovar extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(ServletRenovar.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        LOGGER.info("ServletRenovar do Get!");
        LOGGER.info("Request: {}", req);
        HttpSession session = req.getSession();
        session.invalidate();
        resp.sendRedirect("index.jsp");
    }

    @Override
    public void init() throws ServletException {
        LOGGER.info("ServletRenovar was inited!");
        super.init();
    }

    @Override
    public void destroy() {
        LOGGER.info("ServletRenovar was destroyed");
        super.destroy();
    }
}
