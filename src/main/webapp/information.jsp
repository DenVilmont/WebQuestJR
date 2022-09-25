<%@ page import="org.apache.logging.log4j.LogManager" %>
<%@ page import="org.apache.logging.log4j.Logger" %>
<%
    Logger LOGGER = LogManager.getLogger("information.jsp");
    String login = String.valueOf(session.getAttribute("Login"));

%>
<%@ page contentType="text/html;charset=UTF-8"  %>
<div class="col-3 ">
    <div class="shadow p-4 mt-5 mb-5 rounded bg-secondary bg-gradient">
        <p>Information</p>
        <%
            if (!login.equalsIgnoreCase("null")){
                out.println("<p>");
                out.println("Login: " + login);
                out.println("</p>");
                out.println("");
                out.println("");
            }
        %>

        <form action="renovar" method="get">
            <br>
            <button type="submit" class="btn btn-success">Начать заново</button>
            <br>
        </form>
    </div>
</div>
