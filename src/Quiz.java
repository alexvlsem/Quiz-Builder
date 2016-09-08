/**
 * The Quiz class represents a quiz that is made by a particular user.
 *
 * @author Aleksei_Semenov 17/08/16.
 */
public class Quiz {

    private int id;
    private String name;
    private QuizTypes type;
    private User owner;

    Quiz(int id, String name, QuizTypes type, User owner) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.owner = owner;
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

    public QuizTypes getType() {
        return type;
    }

    public void setType(QuizTypes type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return name;
    }
}
