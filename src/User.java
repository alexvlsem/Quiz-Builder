/**
 * The User class represents users and respondents.
 *
 * @author Aleksei_Semenov 17/08/16.
 */
public class User {

    private String login;
    private String firstName;
    private String lastName;

    public User(String login, String firstName, String lastName) {
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    //Getters
    public String getFirstName() {
        return firstName;
    }

    public String getLogin() {
        return login;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName();
    }
}