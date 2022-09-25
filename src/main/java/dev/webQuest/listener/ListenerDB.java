package dev.webQuest.listener;

import dev.webQuest.SQLiteHandler;

import javax.servlet.*;
import javax.servlet.annotation.*;

@WebListener
public class ListenerDB implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        SQLiteHandler sqLiteHandler = SQLiteHandler.getInstance();
        sqLiteHandler.closeConnection();
    }

}
