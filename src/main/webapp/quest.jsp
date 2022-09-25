<%@ page import="dev.webQuest.SQLiteHandler" %>
<%@ page import="dev.webQuest.Question" %>
<%@ page import="dev.webQuest.Answer" %>
<%@ page import="org.apache.logging.log4j.LogManager" %>
<%@ page import="org.apache.logging.log4j.Logger" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    Logger LOGGER = LogManager.getLogger("quest.jsp");
    LOGGER.info("quest.jsp downloading");
    SQLiteHandler sqLiteHandler = SQLiteHandler.getInstance();
    Question question;
    question = sqLiteHandler.getQuestion(String.valueOf(session.getAttribute("currentQuestion")));

%>
<html>
<head>
    <title>Title</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="css/bootstrap.css">
</head>
<body class="bg-danger bg-gradient">
<div class="container">
    <div class="row justify-content-end">
        <div class="row">
            <div class="col-3"></div>
            <div class="col-6">
                <div class="shadow p-4 mt-5 mb-5 rounded bg-secondary bg-gradient">
                    <h2></h2><br>
                    <p>
                        <%=question.text%>
                    </p>
                </div>
                <%
                    if (!question.isFinish) {

                        out.println("<div class=\"shadow p-3 mb-3 rounded bg-secondary bg-gradient\">");
                        for (Answer answer : question.answers) {
                            out.println("<div class=\"list-group\">");
                            out.println("<a href=\"questEngine?answerID=" + answer.id
                                    + "\" class=\"list-group-item list-group-item-action bg-secondary bg-gradient bg-opacity-10 mb-1\">"
                                    + answer.text
                                    + "</a>");
                            out.println("</div>");
                        }
                        out.println("</div>");
                    }else {
                        out.println("<div class=\"shadow p-3 mb-3 rounded\">");
                        out.println("<p>");
                        out.println("Игра окончена! ");
                        out.println("</p>");
                        out.println("</div>");
                    }
                %>
            </div>

            <jsp:include page="information.jsp" />
        </div>
    </div>
</div>

</body>
</html>
