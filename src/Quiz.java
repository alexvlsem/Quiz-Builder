/**
 * The Quiz class represents a quiz that is made by a particular user.
 *
 * @author Aleksei_Semenov 17/08/16.
 */
class Quiz extends Reference<Integer> {

    private String name;
    private QuizTypes type;
    private User owner;

    Quiz(Integer id, String name, QuizTypes type, User owner) {
        super(id);
        setName(name);
        setType(type);
        this.owner = owner;
    }

    //Setters
    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    //Getters
    User getOwner() {
        return owner;
    }

    QuizTypes getType() {
        return type;
    }

    void setType(QuizTypes type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return name;
    }
}
