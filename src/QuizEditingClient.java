import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

/**
 * Created by aleksei on 17/08/16.
 */
public class QuizEditingClient extends JDialog {

    private QuizEditingGUI quizEditingGUI;
    private Quiz quiz;
    private Container container;

    //QuizEditingClient(User owner, Test test)
    public QuizEditingClient(ApplicationClient applicationClient) {

        super(applicationClient,true);

        quizEditingGUI = new QuizEditingGUI();


        container = getContentPane();
        container.setLayout(new BorderLayout());

        container.add(quizEditingGUI, BorderLayout.CENTER);

        setTitle("Quiz-Builder (Quiz Editing)");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        pack();

        setLocationRelativeTo(null);
        setVisible(true);
        validate();

    }

    public class QuizEditingGUI extends JPanel {

        JTextField quizName;
        JComboBox quizTypes;
        JButton buttonSaveQuiz, buttonNewQuestion,
                buttonEditQuestion, buttonDeleteQuestion;
        JScrollPane scrollPane;

        QuizEditingGUI(){

            quizName = new JTextField(30);
            quizName.setBorder(BorderFactory.createTitledBorder("Name"));

            quizTypes = new JComboBox(new String[]{"test","poll"});
            quizTypes.setBorder(BorderFactory.createTitledBorder("Type"));

            buttonSaveQuiz = new JButton("Save");
            buttonNewQuestion = new JButton("New");
            buttonEditQuestion = new JButton("Edit");
            buttonDeleteQuestion = new JButton("Delete");

            QuizHandler handler = new QuizHandler();
            quizTypes.addItemListener(handler);
            buttonSaveQuiz.addActionListener(handler);
            buttonNewQuestion.addActionListener(handler);
            buttonEditQuestion.addActionListener(handler);
            buttonDeleteQuestion.addActionListener(handler);

            JPanel header = new JPanel();
            header.add(quizName);
            header.add(quizTypes);
            header.add(buttonSaveQuiz);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(3,1));
            buttonPanel.add(buttonNewQuestion);
            buttonPanel.add(buttonEditQuestion);
            buttonPanel.add(buttonDeleteQuestion);

            Vector headings = new Vector();
            headings.addElement("N");
            headings.addElement("Question");
            headings.addElement("Multiple Choise");

            //Creates new table and adds it to the scroll pane
            JTable table = new JTable(new Vector(), headings);
            scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(530, 300));

            JPanel panelTabButt = new JPanel();
            panelTabButt.add(scrollPane);
            panelTabButt.add(buttonPanel);

            JPanel wrapper = new JPanel();
            wrapper.setLayout(new BoxLayout(wrapper,BoxLayout.Y_AXIS));
            wrapper.add(header);
            wrapper.add(panelTabButt);

            add(wrapper);
        }

        class QuizHandler implements ActionListener, ItemListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == quizEditingGUI.buttonNewQuestion){
                    new QuestionEditingClient(QuizEditingClient.this);
                }
            }

            @Override
            public void itemStateChanged(ItemEvent e) {

            }
        }

    }

    //Only for test
    public static void main(String[] args) {
        new QuizEditingClient(null);
    }
}
