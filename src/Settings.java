import java.io.Serializable;

/**
 * @author Aleksei_Semenov 2016-09-18.
 */
public class Settings implements Serializable {

    private String
            server,
            database,
            login;
    private char[] password;
    private InterfaceLanguages localization;

    public InterfaceLanguages getLocalization() {
        return localization;
    }

    public void setLocalization(InterfaceLanguages localization) {
        this.localization = localization;
    }

    public String getServer() {
        return server;
    }

    public String getDatabase() {
        return database;
    }

    public String getLogin() {
        return login;
    }

    public char[] getPassword() {
        return password;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }
}
