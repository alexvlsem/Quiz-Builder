import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * The DataBaseConnector class gets data from and writes to the database.
 *
 * @author Aleksei_Semenov 20/08/16.
 */
public class DataBaseConnector {

    private static Connection conn;

    private static String
            server = "192.168.100.112\\SQLEXPRESS",
            database = "QuizBuilder",
            login = "sa",
            password = "67351";

    // Create a variable for the connection string.
    private static String connectionUrl = "jdbc:sqlserver://" + server + ";" +
            "databaseName=" + database + ";" +
            "user=" + login + ";" +
            "password=" + password + ";";

    public static void createConnection() {

        if (conn == null) {
            try {
                conn = DriverManager.getConnection(connectionUrl);
                //System.out.println("connection ok");

                prepareDatabase();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
                conn = null;
                //System.out.println("connection closed");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void prepareDatabase() throws SQLException {

        try (Statement stmt = conn.createStatement()) { //
            DatabaseMetaData dbmd = conn.getMetaData();

            createUsersTable(dbmd, stmt);
            createQuizzesTable(dbmd, stmt);
            createQuestionsTable(dbmd, stmt);
            createAnswersTable(dbmd, stmt);
            createAssignedQuizzesTable(dbmd, stmt);
            createQuizResponsesTable(dbmd, stmt);
        }
    }

    private static void createUsersTable(
            DatabaseMetaData dbmd, Statement stmt) throws SQLException {
        ResultSet tables = dbmd.getTables(null, null, "Users", null);
        if (!tables.next()) {
            //System.out.println("Table Users doesn't exist"); // Table does not exist

            String sql = "CREATE TABLE Users " +
                    "(login     VARCHAR(25)  NOT NULL PRIMARY KEY, " +
                    " firstName VARCHAR(255) NOT NULL, " +
                    " lastName  VARCHAR(255) NOT NULL, " +
                    " password  VARCHAR(8))";

            stmt.executeUpdate(sql);
            //System.out.println("Table Users has been created");
        } else {
            //System.out.println("Table Users exists"); // Table exists
        }
    }

    private static void createQuizzesTable(
            DatabaseMetaData dbmd, Statement stmt) throws SQLException {
        ResultSet tables = dbmd.getTables(null, null, "Quizzes", null);
        if (!tables.next()) {
            //System.out.println("Table Quizzes doesn't exist");
            String sql = "CREATE TABLE Quizzes " +
                    "(id        INTEGER IDENTITY(1,1) NOT NULL PRIMARY KEY, " +
                    " name      VARCHAR(255) NOT NULL ," +
                    " type      VARCHAR(25) NOT NULL ," +
                    " ownerId   VARCHAR(25) NOT NULL FOREIGN KEY REFERENCES Users(login))";

            stmt.executeUpdate(sql);
        } else {
            //System.out.println("Table Quizzes exists");
        }
    }

    private static void createQuestionsTable(
            DatabaseMetaData dbmd, Statement stmt) throws SQLException {
        ResultSet tables = dbmd.getTables(null, null, "Questions", null);
        if (!tables.next()) {
            String sql = "CREATE TABLE Questions " +
                    "(id             INTEGER IDENTITY(1,1) NOT NULL PRIMARY KEY, " +
                    " name           VARCHAR(255) NOT NULL ," +
                    " text           VARCHAR(2000) NOT NULL ," +
                    " multipleChoice BIT NOT NULL," +
                    " quizId         INTEGER NOT NULL FOREIGN KEY REFERENCES Quizzes(id))";

            stmt.executeUpdate(sql);
        }
    }

    private static void createAnswersTable(
            DatabaseMetaData dbmd, Statement stmt) throws SQLException {
        ResultSet tables = dbmd.getTables(null, null, "Answers", null);
        if (!tables.next()) {
            String sql = "CREATE TABLE Answers " +
                    "(id          INTEGER IDENTITY(1,1) NOT NULL PRIMARY KEY, " +
                    " text        VARCHAR(2000) NOT NULL ," +
                    " correctness BIT NOT NULL," +
                    " questionId  INTEGER NOT NULL FOREIGN KEY REFERENCES Questions(id))";

            stmt.executeUpdate(sql);
        }
    }

    private static void createAssignedQuizzesTable(
            DatabaseMetaData dbmd, Statement stmt) throws SQLException {
        ResultSet tables = dbmd.getTables(null, null, "AssignedQuizzes", null);
        if (!tables.next()) {
            String sql = "CREATE TABLE AssignedQuizzes " +
                    "(userId        VARCHAR(25) NOT NULL FOREIGN KEY REFERENCES Users(login)," +
                    " quizId        INTEGER NOT NULL FOREIGN KEY REFERENCES Quizzes(id)," +
                    " assignDate    DATETIME NOT NULL ," +
                    " quizCompleted BIT NOT NULL," +
                    " completeDate  DATETIME," +
                    " resultViewed  BIT NOT NULL)";

            stmt.executeUpdate(sql);
        }
    }

    public static void createQuizResponsesTable(
            DatabaseMetaData dbmd, Statement stmt) throws SQLException {
        ResultSet tables = dbmd.getTables(null, null, "QuizResponses", null);
        if (!tables.next()) {
            String sql = "CREATE TABLE QuizResponses " +
                    "(respondentId VARCHAR(25) NOT NULL FOREIGN KEY REFERENCES Users(login)," +
                    " quizId       INTEGER NOT NULL FOREIGN KEY REFERENCES Quizzes(id)," +
                    " questionId   INTEGER NOT NULL FOREIGN KEY REFERENCES Questions(id)," +
                    " answerId     INTEGER FOREIGN KEY REFERENCES Answers(id))";

            stmt.executeUpdate(sql);
        }
    }

    public static ArrayList<String> getUserRecord(String[] prm) {

        ArrayList<String> userData = new ArrayList<>();

        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(
                    "SELECT firstName, lastName FROM Users WHERE login='" + prm[0] + "' AND password= '" + prm[1] + "'");
            if (rs.next()) {
                userData.add(rs.getString(1));
                userData.add(rs.getString(2));
            } else {
                userData.add("User is not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userData;
    }

    public static ArrayList<String> createUser(String[] prm) {

        ArrayList<String> userData = new ArrayList<>();

        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(
                    "SELECT firstName, lastName FROM Users WHERE login='" + prm[0] + "'");
            if (rs.next()) {
                userData.add("The login has already been used,\ntry to enter another one.");
            } else {
                stmt.executeUpdate("INSERT INTO Users (login, firstName, lastName, password) " +
                        "VALUES ( '" + prm[0] + "', '" + prm[1] + "', '" + prm[2] + "', '" + prm[3] + "')");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userData;
    }

    public static void saveQuiz(Quiz quiz) {

        if (quiz.getId() == 0) {

            String query = "INSERT INTO Quizzes (name, type, ownerId) VALUES (?, ?, ?)";

            try (PreparedStatement pstm = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                pstm.setString(1, quiz.getName());
                pstm.setString(2, quiz.getType().name());
                pstm.setString(3, quiz.getOwner().getLogin());
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

    public static Vector getQuizzes(User user) {

        Vector rows = new Vector();

        String query = "SELECT * FROM Quizzes WHERE ownerId=? ORDER BY id";

        try (PreparedStatement pstm = conn.prepareStatement(query)) {
            pstm.setString(1, user.getLogin());
            ResultSet rs = pstm.executeQuery();

            int num = 0;
            while (rs.next()) {
                Vector row = new Vector();

                row.add(++num);
                Quiz quiz = new Quiz(rs.getInt("id"), rs.getString("name"), QuizTypes.valueOf(rs.getString("type")), user);

                row.add(quiz);
                row.add(quiz.getType());

                rows.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    public static void saveQuestion(Question question) {

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

    public static Vector getQuestions(Quiz quiz) {

        Vector rows = new Vector();

        String query = "SELECT * FROM Questions WHERE quizId=? ORDER BY id";

        try (PreparedStatement pstm = conn.prepareStatement(query)) {
            pstm.setInt(1, quiz.getId());
            ResultSet rs = pstm.executeQuery();

            int num = 0;
            while (rs.next()) {
                Vector row = new Vector();
                row.add(++num);
                Question question = new Question(rs.getInt("id"), rs.getString("name"),
                        rs.getString("text"), rs.getBoolean("multipleChoice"), quiz);
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

    public static void saveAnswer(Answer answer) {

        if (answer.getId() == 0) {

            String query = "INSERT INTO Answers (text, correctness, questionId) VALUES (?, ?, ?)";

            try (PreparedStatement pstm = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
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

    public static Vector getAnswers(Question question) {

        Vector rows = new Vector();

        String query = "SELECT * FROM Answers WHERE questionId=? ORDER BY id";

        try (PreparedStatement pstm = conn.prepareStatement(query)) {
            pstm.setInt(1, question.getId());
            ResultSet rs = pstm.executeQuery();

            int num = 0;
            while (rs.next()) {
                Vector row = new Vector();
                row.add(++num);
                Answer answer = new Answer(rs.getInt("id"), rs.getString("text"), rs.getBoolean("correctness"), question);
                row.add(answer);
                row.add(answer.getCorrectness());

                rows.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    public static ArrayList<User> getUsersForAssignment(Quiz quiz, boolean inclusive) {

        ArrayList<User> users = new ArrayList<>();

        String query = "SELECT * FROM Users WHERE login " + (inclusive ? "NOT" : "") + " IN " +
                "(SELECT userId FROM AssignedQuizzes WHERE quizId =? AND quizCompleted =?)";

        try (PreparedStatement pstm = conn.prepareStatement(query)) {
            pstm.setInt(1, quiz.getId());
            pstm.setBoolean(2, false);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                users.add(new User(rs.getString("login"), rs.getString("firstName"), rs.getString("lastName")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public static void assignQuizToUsers(ArrayList<User> users, Quiz quiz) {

        String query = "INSERT INTO AssignedQuizzes (userId, quizId, assignDate, quizCompleted, resultViewed ) " +
                " VALUES (?, ?, ?, ?, ?);";

        try (PreparedStatement pstm = conn.prepareStatement(query)) {

            for (User u : users) {
                pstm.setString(1, u.getLogin());
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

    public static void removeQuizFromUsers(ArrayList<User> users, Quiz quiz) {

        String query = "DELETE FROM AssignedQuizzes WHERE userId =? AND quizId =? AND quizCompleted=?";

        try (PreparedStatement pstm = conn.prepareStatement(query)) {

            for (User u : users) {
                pstm.setString(1, u.getLogin());
                pstm.setInt(2, quiz.getId());
                pstm.setBoolean(3, false);
                pstm.addBatch();
            }
            pstm.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Vector getAssignedQuizzes(User user) {

        Vector rows = new Vector();

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
            pstm.setString(1, user.getLogin());
            ResultSet rs = pstm.executeQuery();

            int num = 0;
            while (rs.next()) {
                Vector row = new Vector();
                row.add(++num);
                row.add(rs.getDate("assignDate"));
                User author = new User(rs.getString("authorId"), rs.getString("authorFirstName"), rs.getString("authorLastName"));
                Quiz quiz = new Quiz(rs.getInt("quizID"), rs.getString("quizName"), QuizTypes.valueOf(rs.getString("quizType")), author);
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

}
