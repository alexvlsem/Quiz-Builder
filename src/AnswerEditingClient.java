import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The AnswerEditingClient class creates the form for editing an answer for the question;
 * is called from an instance of the QuestionEditingClient class.
 *
 * @author Aleksei_Semenov 17/08/16.
 */
public class AnswerEditingClient extends JDialog {

    private AnswerEditingGUI answerEditingGUI;
    private Answer answer;
    private Container container;

    private class AnswerEditingGUI extends JPanel {

        JTextArea answerText;
        JCheckBox rightAnswer;
        JButton buttonSave;

        AnswerEditingGUI() {

            answerText = new JTextArea(8, 35);
            rightAnswer = new JCheckBox("Right answer");

            if(answer.getId() != 0){
                answerText.setText(answer.getText());
                rightAnswer.setSelected(answer.getCorrectness());
            }
            buttonSave = new JButton("Save");

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

    AnswerEditingClient(QuestionEditingClient dialog, Answer answer) {
        super(dialog, true);

        assert (answer != null):
                "The instance of the Answer class is null in the AnswerEditingClient constructor";

        this.answer = answer;

        answerEditingGUI = new AnswerEditingGUI();

        AnswerHandler handler = new AnswerHandler();
        answerEditingGUI.buttonSave.addActionListener(handler);

        container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(answerEditingGUI, BorderLayout.CENTER);

        setTitle("Quiz-Builder (Answer Editing)");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        pack();

        setLocationRelativeTo(null);
        setVisible(true);
        validate();
    }

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
