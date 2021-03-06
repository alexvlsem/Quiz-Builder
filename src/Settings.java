import java.io.Serializable;

/**
 * The Settings class represents settings of the program.
 *
 * @author Aleksei_Semenov 2016-09-18.
 */
class Settings implements Serializable {

    private static final long serialVersionUID = 135737672033666209L;

    //Singleton
    private volatile static Settings settings;

    private String
        server,
        database,
        login;
    private char[] password;
    private InterfaceLanguages localization;


    private Settings() {
    }

    public static Settings getInstance() {
        if (settings == null) {
            synchronized (Settings.class) {
                if (settings == null) {
                    settings = new Settings();
                }
            }
        }
        return settings;
    }

    //Getters
    InterfaceLanguages getLocalization() {
        return localization;
    }

    String getServer() {
        return server;
    }

    String getDatabase() {
        return database;
    }

    String getLogin() {
        return login;
    }

    char[] getPassword() {
        return password;
    }

    //Setters
    void setLocalization(InterfaceLanguages localization) {
        this.localization = localization;
    }

    void setServer(String server) {
        this.server = server;
    }

    void setDatabase(String database) {
        this.database = database;
    }

    void setLogin(String login) {
        this.login = login;
    }

    void setPassword(char[] password) {
        this.password = password;
    }
}
