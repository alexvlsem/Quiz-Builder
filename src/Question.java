/**
 * The Question class represents a question from the quiz.
 *
 * @author Aleksei_Semenov 10/08/16.
 */
class Question extends Reference<Integer> {

    private String name;
    private String text;
    private boolean multipleChoice;
    private Quiz quiz;

    Question(Integer id, String name, String text, boolean multipleChoice, Quiz quiz) {
        super(id);
        setName(name);
        setText(text);
        setMultipleChoice(multipleChoice);
        setQuiz(quiz);
    }

    //Getters
    String getText() {
        return text;
    }

    boolean getMultipleChoice() {
        return multipleChoice;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    String getName() {
        return name;
    }

    //Setters
    void setText(String text) {
        this.text = text;
    }

    void setMultipleChoice(boolean multipleChoice) {
        this.multipleChoice = multipleChoice;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
