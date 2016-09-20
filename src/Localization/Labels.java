package Localization;

import java.util.ListResourceBundle;

/**
 * @author Aleksei_Semenov 2016-09-19.
 */
public class Labels extends ListResourceBundle{

    @Override
    protected Object[][] getContents() {
        return new Object[][]{
                //LoginClient
                {"btSave","Save"},
                {"lbServer", "Server: "},
                {"lbDatabase", "Database: "},
                {"lbPassword", "Password: "},
                {"lbLogin", "Login: "},
                {"btTest","Test"},
                {"btCreate","Create"},
                {"lbLanguage","Language: "},
                {"lbFirstName","First name: "},
                {"lbLastName","Last name: "},
                {"tlConnection","Connection"},
                {"msUserCreated","User has been created!"},
                {"msConnectionSuccess","Connection has been established!"},
                {"msConnectionFailed","Impossible to connect\nwith the current parameters"},
                {"tlLogIn","Log in"},
                {"tlCreateUser","Create user"},
                {"tlSettings","Settings"},
                {"btSignIn","Sign in"},
        };
    }
}
