import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * The DataBaseConnector class gets data from and writes to the database.
 *
 * @author Aleksei_Semenov 20/08/16.
 */
class DataBaseConnector {

    private static Connection conn;

    static void createConnection() {

        if (conn == null) {
            String connectionUrl = String.format(
                "jdbc:sqlserver://%s;databaseName=%s;user=%s;password=%s;",
                LoginClient.getSettings().getServer(),
                LoginClient.getSettings().getDatabase(),
                LoginClient.getSettings().getLogin(),
                new String(LoginClient.getSettings().getPassword()));
            try {
                conn = DriverManager.getConnection(connectionUrl);
                prepareDatabase();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The connectionStatus method checks the connection status.
     *
     * @return the connection status
     */
    static boolean connectionStatus() {
        return conn != null;
    }

    /**
     * The closeConnection method closes the connection.
     */
    static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The prepareDatabase method calls the list of methods
     * to create new tables in the database.
     */
    private static void prepareDatabase() throws SQLException {

        try (Statement stmt = conn.createStatement()) {
            DatabaseMetaData dbmd = conn.getMetaData();

            createUsersTable(dbmd, stmt);
            createQuizzesTable(dbmd, stmt);
            createQuestionsTable(dbmd, stmt);
            createAnswersTable(dbmd, stmt);
            createAssignedQuizzesTable(dbmd, stmt);
            createQuizResponsesTable(dbmd, stmt);
        }
    }

    /**
     * The createUsersTable method creates a table to store records about users.
     */
    private static void createUsersTable(
        DatabaseMetaData dbmd, Statement stmt) throws SQLException {
        ResultSet tables = dbmd.getTables(null, null, "Users", null);
        if (!tables.next()) {

            String sql = "CREATE TABLE Users " +
                "(login     NVARCHAR(25)  NOT NULL PRIMARY KEY, " +
                " firstName NVARCHAR(50) NOT NULL, " +
                " lastName  NVARCHAR(50) NOT NULL, " +
                " password  NVARCHAR(8))";

            stmt.executeUpdate(sql);
        }
    }

    /**
     * The createQuizzesTable method creates a table to store records about quizzes.
     */
    private static void createQuizzesTable(
        DatabaseMetaData dbmd, Statement stmt) throws SQLException {
        ResultSet tables = dbmd.getTables(null, null, "Quizzes", null);
        if (!tables.next()) {
            String sql = "CREATE TABLE Quizzes " +
                "(id        INTEGER IDENTITY(1,1) NOT NULL PRIMARY KEY, " +
                " name      NVARCHAR(255) NOT NULL ," +
                " type      NVARCHAR(25) NOT NULL ," +
                " ownerId   NVARCHAR(25) NOT NULL FOREIGN KEY REFERENCES Users(login))";

            stmt.executeUpdate(sql);
        }
    }

    /**
     * The createQuestionsTable method creates a table to store records about questions.
     */
    private static void createQuestionsTable(
        DatabaseMetaData dbmd, Statement stmt) throws SQLException {
        ResultSet tables = dbmd.getTables(null, null, "Questions", null);
        if (!tables.next()) {
            String sql = "CREATE TABLE Questions " +
                "(id             INTEGER IDENTITY(1,1) NOT NULL PRIMARY KEY, " +
                " name           NVARCHAR(255) NOT NULL ," +
                " text           NVARCHAR(2000) NOT NULL ," +
                " multipleChoice BIT NOT NULL," +
                " quizId         INTEGER NOT NULL FOREIGN KEY REFERENCES Quizzes(id))";

            stmt.executeUpdate(sql);
        }
    }

    /**
     * The createAnswersTable method creates a table to store records about answers.
     */
    private static void createAnswersTable(
        DatabaseMetaData dbmd, Statement stmt) throws SQLException {
        ResultSet tables = dbmd.getTables(null, null, "Answers", null);
        if (!tables.next()) {
            String sql = "CREATE TABLE Answers " +
                "(id          INTEGER IDENTITY(1,1) NOT NULL PRIMARY KEY, " +
                " text        NVARCHAR(2000) NOT NULL ," +
                " correctness BIT NOT NULL," +
                " questionId  INTEGER NOT NULL FOREIGN KEY REFERENCES Questions(id))";

            stmt.executeUpdate(sql);
        }
    }

    /**
     * The createAssignedQuizzesTable method creates a table to store records about
     * assigned to users quizzes and completed by users quizzes.
     */
    private static void createAssignedQuizzesTable(
        DatabaseMetaData dbmd, Statement stmt) throws SQLException {
        ResultSet tables = dbmd.getTables(null, null, "AssignedQuizzes", null);
        if (!tables.next()) {
            String sql = "CREATE TABLE AssignedQuizzes " +
                "(userId        NVARCHAR(25) NOT NULL FOREIGN KEY REFERENCES Users(login)," +
                " quizId        INTEGER NOT NULL FOREIGN KEY REFERENCES Quizzes(id)," +
                " assignDate    DATETIME NOT NULL ," +
                " quizCompleted BIT NOT NULL," +
                " completeDate  DATETIME," +
                " resultViewed  BIT NOT NULL)";

            stmt.executeUpdate(sql);
        }
    }

    /**
     * The createQuizResponsesTable method creates a table to store records about
     * the users' answers.
     */
    private static void createQuizResponsesTable(
        DatabaseMetaData dbmd, Statement stmt) throws SQLException {
        ResultSet tables = dbmd.getTables(null, null, "QuizResponses", null);
        if (!tables.next()) {
            String sql = "CREATE TABLE QuizResponses " +
                "(respondentId NVARCHAR(25) NOT NULL FOREIGN KEY REFERENCES Users(login)," +
                " quizId       INTEGER NOT NULL FOREIGN KEY REFERENCES Quizzes(id)," +
                " questionId   INTEGER NOT NULL FOREIGN KEY REFERENCES Questions(id)," +
                " answerId     INTEGER NOT NULL FOREIGN KEY REFERENCES Answers(id)," +
                " isSelected   BIT NOT NULL)";

            stmt.executeUpdate(sql);
        }
    }

    /**
     * The getUserRecord gets a record about the user.
     */
    static ArrayList<String> getUserRecord(String[] prm) {

        ArrayList<String> userData = new ArrayList<>();

        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT " +
                "firstName, " +
                "lastName " +
                "FROM Users " +
                "WHERE login='" + prm[0] + "' AND password= '" + prm[1] + "'");
            if (rs.next()) {
                userData.add(rs.getString(1));
                userData.add(rs.getString(2));
            } else {
                userData.add(LoginClient.getRb().getString("msUserIsNotFound"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userData;
    }

    /**
     * The createUser method inserts a record about the user.
     */
    static ArrayList<String> createUser(String[] prm) {

        ArrayList<String> userData = new ArrayList<>();

        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(
                "SELECT firstName, lastName FROM Users WHERE login='" + prm[0] + "'");
            if (rs.next()) {
                userData.add(LoginClient.getRb().getString("msLoginIsBeenUsed"));
            } else {
                stmt.executeUpdate("INSERT INTO Users (login, firstName, lastName, password) " +
                    "VALUES ( '" + prm[0] + "'," +
                    " '" + prm[1] + "'," +
                    " '" + prm[2] + "'," +
                    " '" + prm[3] + "')");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userData;
    }

    /**
     * The saveQuiz method inserts a new record or updates an existing record
     * about the particular quiz.
     *
     * @param quiz the instance of the Quiz class.
     */
    static void saveQuiz(Quiz quiz) {

        if (quiz.getId() == 0) {

            String query = "INSERT INTO Quizzes (name, type, ownerId) VALUES (?, ?, ?)";

            try (PreparedStatement pstm = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                pstm.setString(1, quiz.getName());
                pstm.setString(2, quiz.getType().name());
                pstm.setString(3, quiz.getOwner().getId());
                pstm.executeUpdate();
                ResultSet rs = pstm.getGeneratedKeys();
                rs.next();
                quiz.setId(rs.getInt(1));

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            String query = "UPDATE Quizzes SET name=?, type=? WHERE id=?";

            try (PreparedStatement pstm = conn.prepareStatement(query)) {
                pstm.setString(1, quiz.getName());
                pstm.setString(2, quiz.getType().name());
                pstm.setInt(3, quiz.getId());
                pstm.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The getQuizzes method returns user's quizzes.
     *
     * @param user instance of the User class.
     * @return a two dimensional vector;
     * each row contains: a number of the row, a Quiz instance, a quiz type.
     */
    static Vector<Object> getQuizzes(User user) {

        Vector<Object> rows = new Vector<>();

        String query = "SELECT * FROM Quizzes WHERE ownerId=? ORDER BY id";

        try (PreparedStatement pstm = conn.prepareStatement(query)) {
            pstm.setString(1, user.getId());
            ResultSet rs = pstm.executeQuery();

            int num = 0;
            while (rs.next()) {
                Vector<Object> row = new Vector<>();

                row.add(++num);
                Quiz quiz = new Quiz(rs.getInt("id"), rs.getString("name"),
                    QuizTypes.valueOf(rs.getString("type")), user);

                row.add(quiz);
                row.add(quiz.getType());

                rows.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    /**
     * The saveQuestion method inserts a new record or updates an existing record
     * about the particular question.
     *
     * @param question the instance of the Question class.
     */
    static void saveQuestion(Question question) {

        if (question.getId() == 0) {

            String query = "INSERT INTO Questions (name, text, multipleChoice, quizId) VALUES (?, ?, ?, ?)";

            try (PreparedStatement pstm = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                pstm.setString(1, question.getName());
                pstm.setString(2, question.getText());
                pstm.setBoolean(3, question.getMultipleChoice());
                pstm.setInt(4, question.getQuiz().getId());
                pstm.executeUpdate();
                ResultSet rs = pstm.getGeneratedKeys();
                rs.next();
                question.setId(rs.getInt(1));

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            String query = "UPDATE Questions SET name=?, text=?, multipleChoice=? WHERE id=?";

            try (PreparedStatement pstm = conn.prepareStatement(query)) {
                pstm.setString(1, question.getName());
                pstm.setString(2, question.getText());
                pstm.setBoolean(3, question.getMultipleChoice());
                pstm.setInt(4, question.getId());
                pstm.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The getQuestions method returns questions of the quiz.
     *
     * @param quiz the instance of the Quiz class.
     * @return a two dimensional vector;
     * each row contains: a number of the row, a Question instance,
     * a text of the question, an attribute of multiple choice of the question.
     */
    static Vector<Object> getQuestions(Quiz quiz) {

        Vector<Object> rows = new Vector<>();

        String query = "SELECT * FROM Questions WHERE quizId=? ORDER BY id";

        try (PreparedStatement pstm = conn.prepareStatement(query)) {
            pstm.setInt(1, quiz.getId());
            ResultSet rs = pstm.executeQuery();

            int num = 0;
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(++num);
                Question question = new Question(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("text"),
                    rs.getBoolean("multipleChoice"),
                    quiz);
                row.add(question);
                row.add(question.getText());
                row.add(question.getMultipleChoice());

                rows.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    /**
     * The saveAnswer method inserts a new record or updates an existing record
     * about the particular answer.
     *
     * @param answer the instance of the Answer class.
     */
    static void saveAnswer(Answer answer) {

        if (answer.getId() == 0) {

            String query = "INSERT INTO Answers (text, correctness, questionId) " +
                "VALUES (?, ?, ?)";

            try (PreparedStatement pstm = conn.prepareStatement(query,
                Statement.RETURN_GENERATED_KEYS)) {
                pstm.setString(1, answer.getText());
                pstm.setBoolean(2, answer.getCorrectness());
                pstm.setInt(3, answer.getQuestion().getId());
                pstm.executeUpdate();
                ResultSet rs = pstm.getGeneratedKeys();
                rs.next();
                answer.setId(rs.getInt(1));

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            String query = "UPDATE Answers SET text=?, correctness=? WHERE id=?";

            try (PreparedStatement pstm = conn.prepareStatement(query)) {
                pstm.setString(1, answer.getText());
                pstm.setBoolean(2, answer.getCorrectness());
                pstm.setInt(3, answer.getId());
                pstm.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The getAnswers method returns answers of the question.
     *
     * @param question the instance of the Question class
     * @return a two dimensional vector;
     * each row contains: a number of the row, an Answer instance,
     * an attribute of correctness of the answer.
     */
    static Vector<Object> getAnswers(Question question) {

        Vector<Object> rows = new Vector<>();

        String query = "SELECT * FROM Answers WHERE questionId=? ORDER BY id";

        try (PreparedStatement pstm = conn.prepareStatement(query)) {
            pstm.setInt(1, question.getId());
            ResultSet rs = pstm.executeQuery();

            int num = 0;
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(++num);
                Answer answer = new Answer(
                    rs.getInt("id"),
                    rs.getString("text"),
                    rs.getBoolean("correctness"),
                    question);
                row.add(answer);
                row.add(answer.getCorrectness());

                rows.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    /**
     * The getUsersForAssignment method creates list of users which are assigned
     * or not to particular quiz depending on the inclusive parameter.
     *
     * @param quiz      the instance of the Quiz class.
     * @param inclusive are users assigned or not to the quiz.
     * @return the list of users
     */
    static ArrayList<User> getUsersForAssignment(Quiz quiz, boolean inclusive) {

        ArrayList<User> users = new ArrayList<>();

        String query;

        if (inclusive) {
            query = "SELECT * FROM Users WHERE login NOT IN " +
                "(SELECT userId FROM AssignedQuizzes WHERE quizId =?)";
        } else {
            query = "SELECT * FROM Users WHERE login IN " +
                "(SELECT userId FROM AssignedQuizzes WHERE quizId =? AND quizCompleted =?)";
        }

        try (PreparedStatement pstm = conn.prepareStatement(query)) {
            pstm.setInt(1, quiz.getId());
            if (!inclusive) {
                pstm.setBoolean(2, false);
            }
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                users.add(new User(rs.getString("login"), rs.getString("firstName"), rs.getString("lastName")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    /**
     * The assignQuizToUsers method inserts records about the assigned quiz to the users.
     *
     * @param users list of users.
     * @param quiz  the instance of the Quiz class.
     */
    static void assignQuizToUsers(ArrayList<User> users, Quiz quiz) {

        String query = "INSERT INTO AssignedQuizzes (userId, quizId, assignDate, quizCompleted, resultViewed ) " +
            " VALUES (?, ?, ?, ?, ?);";

        try (PreparedStatement pstm = conn.prepareStatement(query)) {

            for (User u : users) {
                pstm.setString(1, u.getId());
                pstm.setInt(2, quiz.getId());
                pstm.setDate(3, new Date(System.currentTimeMillis()));
                pstm.setBoolean(4, false);
                pstm.setBoolean(5, false);
                pstm.addBatch();
            }
            pstm.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * The removeQuizFromUsers method deletes records about the assigned quiz to the users.
     *
     * @param users list of users.
     * @param quiz  the instance of the Quiz class.
     */
    static void removeQuizFromUsers(ArrayList<User> users, Quiz quiz) {

        String query = "DELETE FROM AssignedQuizzes WHERE userId =? AND quizId =? AND quizCompleted=?";

        try (PreparedStatement pstm = conn.prepareStatement(query)) {

            for (User u : users) {
                pstm.setString(1, u.getId());
                pstm.setInt(2, quiz.getId());
                pstm.setBoolean(3, false);
                pstm.addBatch();
            }
            pstm.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * The getAssignedQuizzes returns quizzes assigned to the user.
     *
     * @param user the current user of the program.
     * @return a two dimensional vector;
     * each row contains: a number of the row, a date of assignment, a Quiz instance,
     * an User instance (author), a quiz type.
     */
    static Vector<Object> getAssignedQuizzes(User user) {

        Vector<Object> rows = new Vector<>();

        String query = "SELECT Users.login AS authorId, " +
            "Users.firstName AS authorFirstName, " +
            "Users.lastName  AS authorLastName, " +
            "Quizzes.id AS quizID, " +
            "Quizzes.name AS quizName, " +
            "Quizzes.type AS quizType," +
            "AssignedQuizzes.assignDate," +
            "AssignedQuizzes.quizCompleted  " +
            "FROM AssignedQuizzes  \n" +
            "INNER JOIN Quizzes ON Quizzes.id=AssignedQuizzes.quizId\n" +
            "INNER JOIN Users ON Users.login=Quizzes.ownerId\n" +
            "WHERE AssignedQuizzes.userId =?";

        try (PreparedStatement pstm = conn.prepareStatement(query)) {
            pstm.setString(1, user.getId());
            ResultSet rs = pstm.executeQuery();

            int num = 0;
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(++num);
                row.add(rs.getDate("assignDate"));
                User author = new User(
                    rs.getString("authorId"),
                    rs.getString("authorFirstName"),
                    rs.getString("authorLastName"));
                Quiz quiz = new Quiz(
                    rs.getInt("quizID"),
                    rs.getString("quizName"),
                    QuizTypes.valueOf(rs.getString("quizType")),
                    author);
                row.add(quiz);
                row.add(author);
                row.add(quiz.getType());
                row.add(rs.getBoolean("quizCompleted"));

                rows.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    /**
     * The saveResponses method saves responses during taking a quiz.
     *
     * @param rows       a two dimensional vector with the selected answers.
     * @param respondent the user who is taking a quiz.
     */
    static void saveResponses(Vector rows, User respondent) {

        String queryDel = "DELETE FROM QuizResponses " +
            "WHERE respondentId=? AND answerId=?";
        String queryAdd = "INSERT INTO QuizResponses " +
            "(respondentId, quizId, questionId, answerId, isSelected) " +
            " VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmDel = conn.prepareStatement(queryDel);
             PreparedStatement pstmAdd = conn.prepareStatement(queryAdd)) {

            for (Object object : rows) {

                Vector row = (Vector) object;

                boolean isSelected = (boolean) row.get(0);
                Answer answer = (Answer) row.get(1);

                //removes old records
                pstmDel.setString(1, respondent.getId());
                pstmDel.setInt(2, answer.getId());
                pstmDel.addBatch();

                //writes new records
                pstmAdd.setString(1, respondent.getId());
                pstmAdd.setInt(2, answer.getQuestion().getQuiz().getId());
                pstmAdd.setInt(3, answer.getQuestion().getId());
                pstmAdd.setInt(4, answer.getId());
                pstmAdd.setBoolean(5, isSelected);
                pstmAdd.addBatch();
            }
            pstmDel.executeBatch();
            pstmAdd.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * The getMarkedAnswers method returns selected answers during taking a quiz.
     *
     * @param respondent the user who is taking a quiz.
     * @param question   the instance of the Question class.
     * @return a two dimensional vector;
     * each row contains: a number of the row, an Answer instance,
     * selection of the answer.
     */
    static Vector<Object> getMarkedAnswers(User respondent, Question question) {

        Vector<Object> rows = new Vector<>();

        String query = "SELECT\n" +
            "  Answers.id,\n" +
            "  Answers.text,\n" +
            "  Answers.correctness,\n" +
            "  Responses.isSelected\n" +
            "FROM Answers\n" +
            "  LEFT JOIN\n" +
            "  (SELECT\n" +
            "     QuizResponses.isSelected,\n" +
            "     QuizResponses.answerId\n" +
            "   FROM QuizResponses\n" +
            "   WHERE QuizResponses.respondentId = ?) AS Responses\n" +
            "    ON Answers.id = Responses.answerId\n" +
            "WHERE Answers.questionId = ?\n" +
            "ORDER BY Answers.id";

        try (PreparedStatement pstm = conn.prepareStatement(query)) {

            pstm.setString(1, respondent.getId());
            pstm.setInt(2, question.getId());
            ResultSet rs = pstm.executeQuery();

            int num = 0;
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(++num);
                Answer answer = new Answer(
                    rs.getInt("id"),
                    rs.getString("text"),
                    rs.getBoolean("correctness"),
                    question);
                row.add(answer);
                row.add(rs.getBoolean("isSelected"));
                rows.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    /**
     * The finishQuiz method updates the record about an assigned quiz
     * making the quiz completed.
     *
     * @param respondent the user who is taking a quiz.
     * @param quiz       the instance of the Quiz class.
     */
    static void finishQuiz(User respondent, Quiz quiz) {
        String query = "UPDATE AssignedQuizzes \n" +
            "SET AssignedQuizzes.completeDate=?, AssignedQuizzes.quizCompleted=? \n" +
            "WHERE AssignedQuizzes.quizId=? AND AssignedQuizzes.userId= ?";
        try (PreparedStatement pstm = conn.prepareStatement(query)) {
            pstm.setDate(1, new Date(System.currentTimeMillis()));
            pstm.setBoolean(2, true);
            pstm.setInt(3, quiz.getId());
            pstm.setString(4, respondent.getId());

            pstm.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * The method getCompletedQuizzes returns completed quizzes.
     *
     * @param user the owner of quizzes; the current user.
     * @return a two dimensional vector;
     * each row contains: a date of completion, an User instance (respondent),
     * a Quiz instance, a type of quiz, the viewing of results.
     */
    static Vector getCompletedQuizzes(User user) {

        Vector<Object> rows = new Vector<>();

        String query = "SELECT\n" +
            "  AssignedQuizzes.userId,\n" +
            "  AssignedQuizzes.quizId,\n" +
            "  AssignedQuizzes.completeDate,\n" +
            "  AssignedQuizzes.resultViewed,\n" +
            "  Quizzes.name AS quizName,\n" +
            "  Quizzes.type AS quizType,\n" +
            "  Users.firstName,\n" +
            "  Users.lastName\n" +
            "FROM AssignedQuizzes\n" +
            "INNER JOIN Quizzes ON AssignedQuizzes.quizId = Quizzes.id\n" +
            "INNER JOIN Users ON AssignedQuizzes.userId = Users.login\n" +
            "WHERE Quizzes.ownerId = ? AND AssignedQuizzes.quizCompleted = ?";

        try (PreparedStatement pstm = conn.prepareStatement(query)) {
            pstm.setString(1, user.getId());
            pstm.setBoolean(2, true);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();

                row.add(rs.getDate("completeDate"));
                User respondent = new User(
                    rs.getString("userId"),
                    rs.getString("firstName"),
                    rs.getString("lastName"));
                Quiz quiz = new Quiz(
                    rs.getInt("quizId"),
                    rs.getString("quizName"),
                    QuizTypes.valueOf(rs.getString("quizType")),
                    user);
                row.add(respondent);
                row.add(quiz);
                row.add(quiz.getType());
                row.add(rs.getBoolean("resultViewed"));
                rows.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    /**
     * The getQuizResults method gets results of the quiz.
     *
     * @param respondent the user which results are received.
     * @param quiz       the Quiz instance.
     * @return a two dimensional vector;
     * each row contains: a Question instance,
     * an Answer instance, selection of the answer.
     */
    static Vector<Object> getQuizResults(User respondent, Quiz quiz) {
        Vector<Object> rows = new Vector<>();

        String query = "SELECT\n" +
            "  Questions.id AS questionId,\n" +
            "  Questions.name AS questionName,\n" +
            "  Questions.text AS questionText,\n" +
            "  Questions.multipleChoice AS multipleChoice,\n" +
            "  tbAnswers.id AS answerId,\n" +
            "  tbAnswers.text AS answerText,\n" +
            "  tbAnswers.correctness AS correctAnswer,\n" +
            "  tbAnswers.isSelected AS selectedAnswer\n" +
            "FROM  Questions\n" +
            "INNER JOIN\n" +
            "(SELECT\n" +
            "  Answers.questionId,\n" +
            "  Answers.id,\n" +
            "  Answers.text,\n" +
            "  Answers.correctness,\n" +
            "  Responses.isSelected\n" +
            "FROM Answers\n" +
            "  LEFT JOIN\n" +
            "  (SELECT\n" +
            "   QuizResponses.isSelected,\n" +
            "     QuizResponses.answerId\n" +
            "   FROM QuizResponses\n" +
            "   WHERE QuizResponses.respondentId = ?) AS Responses\n" +
            "    ON Answers.id = Responses.answerId) AS tbAnswers\n" +
            "ON tbAnswers.questionId = Questions.id\n" +
            "WHERE Questions.quizId = ?\n" +
            "ORDER BY questionId, answerId";

        try (PreparedStatement pstm = conn.prepareStatement(query)) {

            pstm.setString(1, respondent.getId());
            pstm.setInt(2, quiz.getId());
            ResultSet rs = pstm.executeQuery();

            Question currQuestion = null;

            while (rs.next()) {

                if (currQuestion == null || currQuestion.getId() != rs.getInt("questionId")) {
                    currQuestion = new Question(
                        rs.getInt("questionId"),
                        rs.getString("questionName"),
                        rs.getString("questionText"),
                        rs.getBoolean("multipleChoice"),
                        quiz);
                }
                Answer currAnswer = new Answer(
                    rs.getInt("answerId"),
                    rs.getString("answerText"),
                    rs.getBoolean("correctAnswer"),
                    currQuestion);
                Vector<Object> row = new Vector<>();
                row.add(currQuestion);
                row.add(currAnswer);
                row.add(rs.getBoolean("selectedAnswer"));
                rows.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    /**
     * The deleteReference method deletes a record from the database.
     *
     * @param reference the instance of the Reference class.
     * @return the result of deletion.
     */
    static boolean deleteReference(Reference<Integer> reference) {

        String tableName = null;
        if (reference instanceof Quiz) {
            tableName = "Quizzes";
        } else if (reference instanceof Question) {
            tableName = "Questions";
        } else if (reference instanceof Answer) {
            tableName = "Answers";
        }

        assert (tableName != null);

        String query = String.format("DELETE FROM  %s WHERE id = ?", tableName);
        try (PreparedStatement pstm = conn.prepareStatement(query)) {
            pstm.setInt(1, reference.getId());
            pstm.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * The makeResponseViewed method makes the result of the quiz viewed.
     *
     * @param respondent the instance of User class.
     * @param quiz       the instance of Quiz class.
     */
    static void makeResponseViewed(User respondent, Quiz quiz) {

        String query = "UPDATE AssignedQuizzes SET resultViewed =? " +
            "WHERE userId=? AND quizId=?";
        try (PreparedStatement pstm = conn.prepareStatement(query)) {
            pstm.setBoolean(1, true);
            pstm.setString(2, respondent.getId());
            pstm.setInt(3, quiz.getId());
            pstm.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}