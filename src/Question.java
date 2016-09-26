/**
 * The Question class represents a question from the quiz.
 *
 * @author Aleksei_Semenov 10/08/16.
 */
public class Question extends Reference {

    private String name;
    private String text;
    private boolean multipleChoice;
    private Quiz quiz;

    public Question(int id, String name, String text, boolean multipleChoice, Quiz quiz) {
        super(id);
        setName(name);
        setText(text);
        setMultipleChoice(multipleChoice);
        setQuiz(quiz);
    }

    //Getters
    public String getText() {
        return text;
    }

    public boolean getMultipleChoice() {
        return multipleChoice;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public String getName() {
        return name;
    }

    //Setters
    public void setText(String text) {
        this.text = text;
    }

    public void setMultipleChoice(boolean multipleChoice) {
        this.multipleChoice = multipleChoice;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
