import javax.swing.*;
import java.awt.*;

/**
 * Created by aleksei on 17/08/16.
 */
public class QuizEditingClient extends JFrame {

    private TestEditingGUI testEditingGUI;
    private Quiz quiz;
    private Container container;

    //QuizEditingClient(User owner, Test test)
    public QuizEditingClient() {

        testEditingGUI = new TestEditingGUI();

        container = getContentPane();
        container.setLayout(new BorderLayout());

        container.add(testEditingGUI, BorderLayout.CENTER);

        setTitle("Quiz Builder");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pack();

        setLocationRelativeTo(null);
        setVisible(true);
        validate();

    }

    public class TestEditingGUI extends JPanel {

        JTextField quizName;
        JComboBox quizTypes;
        JButton buttonSaveQuiz, buttonNewQuestion,
                buttonEditQuestion, buttonDeleteQuestion;

        TestEditingGUI(){

            quizName = new JTextField(20);
            quizName.setBorder(BorderFactory.createTitledBorder("Name"));

            quizTypes = new JComboBox(new String[]{"test","poll"});
            quizTypes.setBorder(BorderFactory.createTitledBorder("Type"));


        }

    }


}
