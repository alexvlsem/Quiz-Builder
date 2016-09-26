import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * The AnswerEditingClient class creates the form for editing an answer for the question;
 * is called from an instance of the QuestionEditingClient class.
 *
 * @author Aleksei_Semenov 17/08/16.
 */
class AnswerEditingClient extends JDialog {

    private ResourceBundle rb = LoginClient.rb;
    private AnswerEditingGUI answerEditingGUI;
    private Answer answer;

    /**
     * The inner AnswerEditingGUI class creates panel for editing the answer
     */
    private class AnswerEditingGUI extends JPanel {

        JTextArea answerText;
        JCheckBox rightAnswer;
        JButton buttonSave;

        AnswerEditingGUI() {

            answerText = new JTextArea(8, 35);
            rightAnswer = new JCheckBox(rb.getString("lbRightAnswer"));

            if (answer.getId() != 0) {
                answerText.setText(answer.getText());
                rightAnswer.setSelected(answer.getCorrectness());
            }
            buttonSave = new JButton(rb.getString("btSave"));

            JPanel header = new JPanel();
            header.add(rightAnswer);
            header.add(buttonSave);

            JPanel wrapper = new JPanel();
            wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
            wrapper.add(header);
            wrapper.add(new JScrollPane(answerText));

            add(wrapper);
        }
    }

    /**
     * The AnswerEditingClient constructor.
     *
     * @param dialog the calling dialog.
     * @param answer the instance of the Answer class.
     */
     AnswerEditingClient(QuestionEditingClient dialog, Answer answer) {
        super(dialog, true);

        this.answer = answer;

        answerEditingGUI = new AnswerEditingGUI();

        AnswerHandler handler = new AnswerHandler();
        answerEditingGUI.buttonSave.addActionListener(handler);

        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(answerEditingGUI, BorderLayout.CENTER);

        setTitle("Quiz-Builder (" + rb.getString("tlAnswerEditing") + ")");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        pack();

        setLocationRelativeTo(null);
        setVisible(true);
        validate();
    }

    /**
     * The AnswerHandler class saves the answer
     */
    private class AnswerHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == answerEditingGUI.buttonSave) {
                answer.setText(answerEditingGUI.answerText.getText());
                answer.setCorrectness(answerEditingGUI.rightAnswer.isSelected());
                DataBaseConnector.saveAnswer(answer);
            }
        }
    }
}
