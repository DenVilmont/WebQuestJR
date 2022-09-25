package dev.webQuest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;


class SQLiteHandlerTest {
    private static Logger LOGGER = null;
    private SQLiteHandler sqLiteHandler = null;
    private String start_question = null;
    private final static Pattern UUID_REGEX_PATTERN =
            Pattern.compile("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$");

    public static boolean isValidUUID(String str) {
        if (str == null) {
            return false;
        }
        return UUID_REGEX_PATTERN.matcher(str).matches();
    }

    @BeforeAll
    static void setLogger() {
        System.setProperty("log4j.configurationFile","log4j2-test.xml");
        LOGGER = LogManager.getLogger(SQLiteHandlerTest.class);
    }

    @BeforeEach
    void setSQLiteHandler(){
        LOGGER.info("BeforeEach");
        sqLiteHandler = SQLiteHandler.getInstance();
        try {
            Field field = sqLiteHandler.getClass().getDeclaredField("CON_STR");
            field.setAccessible(true);
            field.set(sqLiteHandler, "jdbc:sqlite:"
                    + Objects.requireNonNull(SQLiteHandlerTest.class.getClassLoader().getResource("WebQuest.db")).getPath());

            Method method = sqLiteHandler.getClass().getDeclaredMethod("openConnection");
            method.setAccessible(true);
            method.invoke(sqLiteHandler);
        } catch (NoSuchFieldException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            LOGGER.error(e);
        }
        start_question = sqLiteHandler.getStartQuestionID();
    }
    @AfterEach
    void deleteSQLiteHandler(){
        LOGGER.info("AfterEach");
        sqLiteHandler.closeConnection();
    }

    @Test
    void getInstance() {
        LOGGER.info("getInstanceTest");
        SQLiteHandler sqLiteHandler1 = SQLiteHandler.getInstance();
        SQLiteHandler sqLiteHandler2 = SQLiteHandler.getInstance();
        Assertions.assertSame(sqLiteHandler1, sqLiteHandler2);
    }

    @Test
    void closeConnection() {
        LOGGER.info("closeConnectionTest");
        Connection connection;
        sqLiteHandler.closeConnection();
        try {
            Field field = sqLiteHandler.getClass().getDeclaredField("connection");
            field.setAccessible(true);
            connection = (Connection) field.get(sqLiteHandler);
            Assertions.assertTrue(connection.isClosed());
            Assertions.assertDoesNotThrow(()->sqLiteHandler.closeConnection());
        } catch (NoSuchFieldException | IllegalAccessException | SQLException e) {
            LOGGER.error(e);
        }
    }

    @Test
    void openConnection(){
        LOGGER.info("openConnectionTest");
        Connection connection;
        try {
            Field field = sqLiteHandler.getClass().getDeclaredField("connection");
            field.setAccessible(true);
            connection = (Connection) field.get(sqLiteHandler);

            Assertions.assertNotNull(connection);
            Assertions.assertFalse(connection.isClosed());

            sqLiteHandler.closeConnection();
            Method method = sqLiteHandler.getClass().getDeclaredMethod("openConnection");
            method.setAccessible(true);
            Assertions.assertDoesNotThrow(()-> method.invoke(sqLiteHandler));

        } catch (NoSuchFieldException | IllegalAccessException | SQLException | NoSuchMethodException e) {
            LOGGER.error(e);
        }
    }

    @Test
    void getStartQuestionID() {
        LOGGER.info("getStartQuestionIDTest");
        Assertions.assertTrue(isValidUUID(sqLiteHandler.getStartQuestionID()));
        Question question = sqLiteHandler.getQuestion(start_question);
        Assertions.assertTrue(question.isStart);
        sqLiteHandler.closeConnection();
        Assertions.assertFalse(isValidUUID(sqLiteHandler.getStartQuestionID()));
    }

    @Test
    void getQuestion() {
        LOGGER.info("getQuestionTest");
        Question question = sqLiteHandler.getQuestion(start_question);
        Assertions.assertNotNull(question);
        Assertions.assertTrue(isValidUUID(question.id));
        Assertions.assertNotNull(question.answers);

        Assertions.assertNull(sqLiteHandler.getQuestion(""));
        Assertions.assertNull(sqLiteHandler.getQuestion(null));

        sqLiteHandler.closeConnection();
        question = sqLiteHandler.getQuestion(start_question);
        Assertions.assertNull(question);
    }

    @Test
    void getAnswers() {
        LOGGER.info("getAnswersTest");
        ArrayList<Answer> answers = sqLiteHandler.getAnswers(start_question);
        Assertions.assertTrue(answers.size()>1);
        Assertions.assertNull(sqLiteHandler.getAnswers(""));
        Assertions.assertNull(sqLiteHandler.getAnswers(null));


        sqLiteHandler.closeConnection();
        answers = sqLiteHandler.getAnswers(start_question);
        Assertions.assertNull(answers);
    }

    @Test
    void getNextQuestionID() {
        LOGGER.info("getNextQuestionIDTest");

        Question question = sqLiteHandler.getQuestion(start_question);
        String nextQuestionId = sqLiteHandler.getNextQuestionID(question.answers.get(0).id);

        Assertions.assertTrue(isValidUUID(nextQuestionId));
        Assertions.assertNull(sqLiteHandler.getNextQuestionID(""));
        Assertions.assertNull(sqLiteHandler.getNextQuestionID(null));
        Question nextQuestion = sqLiteHandler.getQuestion(nextQuestionId);
        Assertions.assertNotNull(nextQuestion);
        Assertions.assertTrue(isValidUUID(nextQuestion.id));

        sqLiteHandler.closeConnection();
        nextQuestionId = sqLiteHandler.getNextQuestionID(question.answers.get(0).id);
        Assertions.assertNull(nextQuestionId);
    }
}