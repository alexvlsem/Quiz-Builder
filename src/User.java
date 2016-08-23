/**
 * The User class represents users and respondents.
 *
 * @author Aleksei_Semenov 17/08/16.
 */
public class User {

    private String login;
    private String password;
    private String firstName;
    private String lastName;

    public User(String login, String password, String firstName, String lastName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }


    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return login;
    }

    public String getLastName() {
        return lastName;
    }


}
