/**
 * The abstract Reference class is extended by Quiz, Question and Answer classes.
 *
 * @author Aleksei_Semenov 2016-09-25.
 */
public abstract class Reference {

    private int id;

    public Reference(int id) {
        setId(id);
    }

    //Getters
    public int getId() {
        return id;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }
}
