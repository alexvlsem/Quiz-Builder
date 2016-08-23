import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

/**
 * @author Aleksei Semenov on 17/08/16.
 */
public class QuestionEditingClient extends JDialog {

    private QuestionEditingGUI questionEditingGUI;
    private Question question;
    private Container container;

    private class QuestionEditingGUI extends JPanel {

        JTextField questionName;
        JCheckBox multipleChoise;
        JTextArea questionText;
        JButton buttonSaveQuiz, buttonNewAnswer,
        buttonEditAnswer, buttonDeleteAnswer;

        QuestionEditingGUI(){

            questionName = new JTextField(20);
            questionName.setBorder(BorderFactory.createTitledBorder("Name"));
            multipleChoise = new JCheckBox("Multiple choise");
            questionText = new JTextArea(8,20);
            questionText.setBorder(BorderFactory.createTitledBorder("Question"));
            buttonSaveQuiz = new JButton("Save");
            buttonNewAnswer = new JButton("New");
            buttonEditAnswer = new JButton("Edit");
            buttonDeleteAnswer = new JButton("Delete");

            JPanel header = new JPanel();
            header.add(questionName);
            header.add(multipleChoise);
            header.add(buttonSaveQuiz);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(3,1));
            buttonPanel.add(buttonNewAnswer);
            buttonPanel.add(buttonEditAnswer);
            buttonPanel.add(buttonDeleteAnswer);

            Vector headings = new Vector();
            headings.addElement("#");
            headings.addElement("Answer");
            headings.addElement("Right");
            JTable table = new JTable(new Vector(),headings);
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(400,150));

            JPanel footer = new JPanel();
            footer.add(scrollPane);
            footer.add(buttonPanel);


            JPanel wrapper = new JPanel();
            wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
            wrapper.add(header);
            wrapper.add(new JScrollPane(questionText));
            wrapper.add(footer);

            add(wrapper);
        }

    }

    QuestionEditingClient(QuizEditingClient dialog){

        super(dialog,true);

        questionEditingGUI = new QuestionEditingGUI();

        container = getContentPane();
        container.setLayout(new BorderLayout());

        container.add(questionEditingGUI, BorderLayout.CENTER);

        setTitle("Quiz-Builder (Qestion Editing)");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        pack();

        setLocationRelativeTo(null);
        setVisible(true);
        validate();

    }

    private class QuestionHandler implements ActionListener, ItemListener{


        @Override
        public void actionPerformed(ActionEvent e) {

        }

        @Override
        public void itemStateChanged(ItemEvent e) {

        }
    }

    //Only for tests
    public static void main(String[] args) {
        new QuestionEditingClient(null);
    }
}
