import javax.swing.*;
import java.awt.*;

/**
 * Created by aleksei on 17/08/16.
 */
public class AnswerEditingClient extends JDialog {

    private AnswerEditingGUI answerEditingGUI;
    private Answer answer;
    private Container container;

    public class AnswerEditingGUI extends JPanel {

        JTextField answerName;
        JTextArea answerText;
        JCheckBox rightAnswer;
        JButton buttonSave;

        AnswerEditingGUI(){

            answerName = new JTextField(20);
            answerName.setBorder(BorderFactory.createTitledBorder("Name"));
            rightAnswer = new JCheckBox("Right answer");
            buttonSave = new JButton("Save");

            JPanel header = new JPanel();
            header.add(answerName);
            header.add(rightAnswer);
            header.add(buttonSave);

            answerText = new JTextArea(8,35);

            JPanel wrapper = new JPanel();
            wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
            wrapper.add(header);
            wrapper.add(new JScrollPane(answerText));

            add(wrapper);
        }
    }


    AnswerEditingClient(QuestionEditingClient dialog){
        super(dialog,true);
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

    //Only for tests
    public static void main(String[] args) {
        new AnswerEditingClient(null);
    }

}
