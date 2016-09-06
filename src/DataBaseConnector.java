import java.sql.*;
import java.util.ArrayList;

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
        if (!tables.next())
        {
            System.out.println("Table Quizzes doesn't exist");
            String sql = "CREATE TABLE Quizzes " +
                    "(id        INTEGER IDENTITY(1,1) NOT NULL PRIMARY KEY, " +
                    " name      VARCHAR(255) NOT NULL ,"+
                    " ownerId   VARCHAR(25) NOT NULL FOREIGN KEY REFERENCES Users(login))";

            stmt.executeUpdate(sql);
        }else {
            System.out.println("Table Quizzes exists");
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
                stmt.executeUpdate("INSERT INTO Users (login, firstName, lastName, password)" +
                        "VALUES ( '" + prm[0] + "', '" + prm[1] + "', '" + prm[2] + "', '" + prm[3] + "')");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userData;
    }


    /*public void create(User user) throws SQLException {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_INSERT,
                        Statement.RETURN_GENERATED_KEYS);
        ) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            // ...

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        }
    }

    try (Connection conn = DriverManager.getConnection(connectionUrl)) {
            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            DatabaseMetaData dbmd = conn.getMetaData();

            ResultSet tables = dbmd.getTables(null, null, "Users", null);
            if (tables.next()) {
                // Table exists
                System.out.println("Table Users exists");
            } else {
                // Table does not exist
                System.out.println("Table Users doesn't exist");


                Statement stmt = conn.createStatement();

                String sql = "CREATE TABLE Users " +
                        "(id int IDENTITY(1,1) PRIMARY KEY, " +
                        " email VARCHAR(255), " +
                        " firstName VARCHAR(255), " +
                        " lastName VARCHAR(255), " +
                        " password VARCHAR(8))";

                stmt.executeUpdate(sql);
                System.out.println("Table Users has been created");


                String sqlq = "INSERT INTO Users (email, firstName, lastName, password)" +
                        "VALUES ( 'a@2.com', 'Zara', 'Ali', 18)";
                stmt.executeUpdate(sqlq, Statement.RETURN_GENERATED_KEYS);

                ResultSet rs = stmt.getGeneratedKeys();
                rs.next();
                System.out.println(rs.getInt(1));

            }


            System.out.println("connection ok");
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            System.out.println("connection closed");
        }

    */

}
