import javax.swing.*;
import java.awt.*;

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

        JTextField answerName;
        JTextArea answerText;
        JCheckBox rightAnswer;
        JButton buttonSave;

        AnswerEditingGUI() {

            answerName = new JTextField(20);
            answerName.setBorder(BorderFactory.createTitledBorder("Name"));
            rightAnswer = new JCheckBox("Right answer");
            buttonSave = new JButton("Save");
            answerText = new JTextArea(8, 35);

            JPanel header = new JPanel();
            header.add(answerName);
            header.add(rightAnswer);
            header.add(buttonSave);

            JPanel wrapper = new JPanel();
            wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
            wrapper.add(header);
            wrapper.add(new JScrollPane(answerText));

            add(wrapper);
        }
    }


    AnswerEditingClient(QuestionEditingClient dialog) {
        super(dialog, true);
        answerEditingGUI = new AnswerEditingGUI();

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

    //Only to test the form
    public static void main(String[] args) {
        new AnswerEditingClient(null);
    }

}
