/**
 * The Quiz class represents a quiz that is made by a particular user.
 *
 * @author Aleksei_Semenov 17/08/16.
 */
public class Quiz {

    private String name;
    private User owner;
    private int id;

    Quiz(int id,String name,User owner){
        this.id = id;
        this.name = name;
        this.owner =  owner;
    }

    public String getName() {
        return name;
    }

    public User getOwner() {
        return owner;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
}
