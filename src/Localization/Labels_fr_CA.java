package Localization;

import java.util.ListResourceBundle;

/**
 * The Labels_fr_CA class.
 *
 * @author Aleksei_Semenov 2016-09-21.
 */
public class Labels_fr_CA extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return new Object[][]{

            //LoginClient
            {"btSave", "Conserver"},
            {"lbServer", "Serveur: "},
            {"lbDatabase", "Base de données"},
            {"lbPassword", "Mot de passe"},
            {"lbLogin", "S'identifier"},
            {"btTest", "Test"},
            {"btCreate", "Créer"},
            {"lbLanguage", "La langue"},
            {"lbFirstName", "Prénom"},
            {"lbLastName", "Nom de famille"},
            {"tlConnection", "Connexion"},
            {"msUserCreated", "L'utilisateur a été créé!"},
            {"msConnectionSuccess", "Connexion a été établi!"},
            {"msConnectionFailed", "Impossible de se connecter\navec les paramètres actuels"},
            {"tlLogIn", "Authorization"},
            {"tlCreateUser", "Créer un utilisateur"},
            {"tlSettings", "Paramètres"},
            {"btSignIn", "se connecter"}

            //Please continue who have better French than Google Translate.

        };
    }
}
