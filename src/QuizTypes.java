/**
 * The QuizTypes enum represents types of the quiz.
 *
 * @author Aleksei_Semenov 2016-09-07.
 */
enum QuizTypes {
    TEST("test"), POLL("poll");

    String name;

    QuizTypes(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}