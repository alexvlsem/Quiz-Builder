/**
 * Created by aleksei on 17/08/16.
 */
public class User {

    private String email;
    private String password;
    private String firstName;
    private String lastName;

    public User(String email,String password,String firstName, String lastName){
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }


    public String getFirstName(){
        return firstName;
    }

    public String getEmail(){
        return email;
    }

    public String getLastName(){
        return lastName;
    }


}
