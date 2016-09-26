/**
 * The Answer class represents a possible answer on the question.
 *
 * @author Aleksei_Semenov 10/08/16.
 */
class Answer extends Reference {

    private String text;
    private boolean correctness;
    private Question question;

    Answer(int id, String text, boolean correctness, Question question) {
        super(id);
        setText(text);
        setCorrectness(correctness);
        setQuestion(question);
    }

    //Setters
    String getText() {
        return text;
    }

    public Question getQuestion() {
        return question;
    }

    boolean getCorrectness() {
        return correctness;
    }

    //Getters
    void setText(String text) {
        this.text = text;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    void setCorrectness(boolean correctness) {
        this.correctness = correctness;
    }

    @Override
    public String toString() {
        return text;
    }
}
