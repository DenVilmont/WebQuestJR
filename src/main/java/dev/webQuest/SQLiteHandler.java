package dev.webQuest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sqlite.JDBC;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class SQLiteHandler {
    private static final Logger LOGGER = LogManager.getLogger(SQLiteHandler.class);
    private static String CON_STR = "jdbc:sqlite:"
            + Objects.requireNonNull(SQLiteHandler.class.getClassLoader().getResource("WebQuest.db")).getPath();

    private static SQLiteHandler instance = null;

    public static synchronized SQLiteHandler getInstance(){
        if (instance == null)
            instance = new SQLiteHandler();
        return instance;
    }
    private Connection connection;

    private SQLiteHandler() {
        openConnection();
    }
    private void openConnection(){
        try {
            DriverManager.registerDriver(new JDBC());
            this.connection = DriverManager.getConnection(CON_STR);
            LOGGER.info("DB connection was opened - {}", !connection.isClosed());
        } catch (SQLException e) {
            LOGGER.error(e);
        }
    }

    public void closeConnection(){
        try {
            this.connection.close();
            LOGGER.info("DB connection was closed - {}", connection.isClosed());
        } catch (SQLException e) {
            LOGGER.error(e);
        }
    }

    public Question getQuestion(String questionID){
        if (questionID == null || questionID.isEmpty() || questionID.isBlank() || questionID.length() != 36)
            return null;
        try (PreparedStatement statement = this.connection.prepareStatement(
                "SELECT id, text, isStart, isFinish FROM questions WHERE id = ?")) {
            statement.setObject(1, questionID);

            Question question = new Question();
            ResultSet resultSet = statement.executeQuery();
            question.id = resultSet.getString("id");
            question.text = resultSet.getString("text").replace(System.lineSeparator(),"</br>");
            question.isStart = resultSet.getBoolean("isStart");
            question.isFinish = resultSet.getBoolean("isFinish");

            if(!question.isFinish){
                question.answers = getAnswers(questionID);
            }

            return question;
        } catch (SQLException e) {
            LOGGER.error(e);
            return null;
        }
    }

    public ArrayList<Answer> getAnswers(String questionID){
        if (questionID == null || questionID.isEmpty() || questionID.isBlank() || questionID.length() != 36)
            return null;
        try (PreparedStatement statement = this.connection.prepareStatement(
                "SELECT id, question_id, text, next_question_id FROM answers WHERE question_id = ?")) {
            statement.setObject(1, questionID);

            ArrayList<Answer> list = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                Answer answer = new Answer();
                answer.id = resultSet.getString("id");
                answer.questionID = resultSet.getString("question_id");
                answer.text = resultSet.getString("text").replace(System.lineSeparator(),"</br>");
                answer.nextQuestionID = resultSet.getString("next_question_id");
                list.add(answer);
            }
            return list;
        } catch (SQLException e) {
            LOGGER.error(e);
            return null;
        }
    }

    public String getStartQuestionID(){
        try (Statement statement = this.connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT id FROM questions WHERE isStart = 1");

            return resultSet.getString("id");
        } catch (SQLException e) {
            LOGGER.error(e);
            return "";
        }
    }

    public String getNextQuestionID(String answerID) {
        if (answerID == null || answerID.isEmpty() || answerID.isBlank() || answerID.length() != 36)
            return null;
        try (PreparedStatement statement = this.connection.prepareStatement(
                "SELECT next_question_id FROM answers WHERE id = ?")) {
            statement.setObject(1, answerID);

            ResultSet resultSet = statement.executeQuery();

            return resultSet.getString("next_question_id");
        } catch (SQLException e) {
            LOGGER.error(e);
            return null;
        }
    }
}
