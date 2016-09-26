/**
 * The User class represents users and respondents.
 *
 * @author Aleksei_Semenov 17/08/16.
 */
class User {

    private String login;
    private String firstName;
    private String lastName;

    User(String login, String firstName, String lastName) {
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    //Getters
    private String getFirstName() {
        return firstName;
    }

    String getLogin() {
        return login;
    }

    private String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName();
    }
}