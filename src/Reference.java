/**
 * The abstract Reference class is extended by Quiz, Question and Answer
 * classes.
 *
 * @author Aleksei_Semenov 2016-09-25.
 */
abstract class Reference {

    private int id;

    Reference(int id) {
        setId(id);
    }

    //Getters
    int getId() {
        return id;
    }

    //Setters
    void setId(int id) {
        this.id = id;
    }
}
