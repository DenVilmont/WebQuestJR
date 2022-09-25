package dev.webQuest.servlet;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServletEnterTest {
    private static Logger LOGGER = null;

    @Mock
    private ServletConfig servletConfig;
    @Mock
    private ServletContext servletContext;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Spy
    private ServletEnter servletEnter;

    @BeforeAll
    static void setLogger() {
        System.setProperty("log4j.configurationFile","log4j2-test.xml");
        LOGGER = LogManager.getLogger(ServletEnterTest.class);
    }

    @BeforeEach
    void init() throws ServletException {
        lenient().when(servletConfig.getServletContext()).thenReturn(servletContext);
        lenient().when(servletContext.getRequestDispatcher(any())).thenReturn(requestDispatcher);
        servletEnter.init(servletConfig);
    }

    @Test
    void testDoPost_when_login_in_session() throws ServletException, IOException {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(argThat("Login"::equals))).thenReturn("Den");
        servletEnter.doPost(request, response);

        verify(servletContext).getRequestDispatcher(argThat("/quest.jsp"::equals));
    }

    @Test
    void testDoPost_when_login_in_request() throws ServletException, IOException {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(argThat("Login"::equals))).thenReturn(null);
        when(request.getParameter(argThat("Login"::equals))).thenReturn("Den");
        servletEnter.doPost(request, response);

        verify(servletContext).getRequestDispatcher(argThat("/quest.jsp"::equals));
    }
    @Test
    void testDoPost_when_no_login_anywhere() throws ServletException, IOException {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(argThat("Login"::equals))).thenReturn("");
        when(request.getParameter(argThat("Login"::equals))).thenReturn(null);
        servletEnter.doPost(request, response);
        verify(response).sendRedirect(argThat("index.jsp"::equals));
    }
}