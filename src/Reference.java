/**
 * The abstract generic Reference class is extended by the Quiz, Question,
 * Answer and User classes.
 *
 * @author Aleksei_Semenov 2016-10-19.
 */
abstract class Reference<T> {

    private T id;

    Reference(T id) {
        setId(id);
    }

    //Getters
    T getId() {
        return id;
    }

    //Setters
    void setId(T id) {
        this.id = id;
    }
}
