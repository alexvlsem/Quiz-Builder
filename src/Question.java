/**
 * The Question class represents a question from the quiz.
 *
 * @author Aleksei_Semenov 10/08/16.
 */
public class Question {

    private int id;
    private String name;
    private String text;
    private boolean multipleChoice;
    private Quiz quiz;

    public Question(int id, String name, String text, boolean multipleChoice, Quiz quiz) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.multipleChoice = multipleChoice;
        this.quiz = quiz;
    }

    public String getText() {
        return text;
    }

    public boolean getMultipleChoice() {
        return multipleChoice;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setMultipleChoice(boolean multipleChoice) {
        this.multipleChoice = multipleChoice;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
