import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;

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
                System.out.println("connection ok");

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
                System.out.println("connection closed");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void prepareDatabase() throws SQLException {

        Statement stmt = conn.createStatement(); //
        DatabaseMetaData dbmd = conn.getMetaData();

        createUsersTable(dbmd, stmt);
        createQuizzesTable(dbmd, stmt);
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
                    " ownerId   VARCHAR(25) NOT NULL FOREIGN KEY REFERENCES Users(login))";

            stmt.executeUpdate(sql);
        } else {
            //System.out.println("Table Quizzes exists");
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

            String query = "INSERT INTO Quizzes (name, ownerId) VALUES (?, ?)";

            try (PreparedStatement pstm = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                pstm.setString(1, quiz.getName());
                pstm.setString(2, quiz.getOwner().getLogin());
                pstm.executeUpdate();
                ResultSet rs = pstm.getGeneratedKeys();
                rs.next();
                quiz.setId(rs.getInt(1));

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            String query = "UPDATE Quizzes SET name=? WHERE id=?";

            try (PreparedStatement pstm = conn.prepareStatement(query)) {
                pstm.setString(1, quiz.getName());
                pstm.setInt(2,quiz.getId());
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
            pstm.setString(1,user.getLogin());
            ResultSet rs = pstm.executeQuery();

            while (rs.next()){
                Vector row = new Vector();
                row.add(rs.getInt("id"));
                row.add(rs.getString("name"));
                row.add(rs.getString("ownerId"));

                rows.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rows;
    }

}
