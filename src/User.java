/**
 * The User class represents users and respondents.
 *
 * @author Aleksei_Semenov 17/08/16.
 */
class User extends Reference<String> {

    private String firstName;
    private String lastName;

    User(String id, String firstName, String lastName) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    //Getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName();
    }
}