/**
 * The Answer class represents a possible answer on the question.
 *
 * @author Aleksei_Semenov 10/08/16.
 */
public class Answer extends Reference {

    private String text;
    private boolean correctness;
    private Question question;

    public Answer(int id, String text, boolean correctness, Question question) {
        super(id);
        setText(text);
        setCorrectness(correctness);
        setQuestion(question);
    }

    //Setters
    public String getText() {
        return text;
    }

    public Question getQuestion() {
        return question;
    }

    public boolean getCorrectness() {
        return correctness;
    }

    //Getters
    public void setText(String text) {
        this.text = text;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public void setCorrectness(boolean correctness) {
        this.correctness = correctness;
    }

    @Override
    public String toString() {
        return text;
    }
}
