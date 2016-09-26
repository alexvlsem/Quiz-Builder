/**
 * The Quiz class represents a quiz that is made by a particular user.
 *
 * @author Aleksei_Semenov 17/08/16.
 */
public class Quiz extends Reference {

    private String name;
    private QuizTypes type;
    private User owner;

    Quiz(int id, String name, QuizTypes type, User owner) {
        super(id);
        setName(name);
        setType(type);
        this.owner = owner;
    }

    //Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //Getters
    public User getOwner() {
        return owner;
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
