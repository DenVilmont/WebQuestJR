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

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServletRenovarTest {
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
    private ServletRenovar servletRenovar;

    @BeforeAll
    static void setLogger() {
        System.setProperty("log4j.configurationFile","log4j2-test.xml");
        LOGGER = LogManager.getLogger(ServletEnterTest.class);
    }

    @BeforeEach
    void init() throws ServletException {
        lenient().when(servletConfig.getServletContext()).thenReturn(servletContext);
        lenient().when(servletContext.getRequestDispatcher(any())).thenReturn(requestDispatcher);
        servletRenovar.init(servletConfig);
    }

    @Test
    void doGet() throws IOException {
        when(request.getSession()).thenReturn(session);

        servletRenovar.doGet(request, response);

        verify(session, times(1)).invalidate();
        verify(response).sendRedirect(argThat("index.jsp"::equals));
    }
}