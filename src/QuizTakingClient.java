import javax.jws.soap.SOAPBinding;
import javax.swing.*;
import javax.xml.parsers.FactoryConfigurationError;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The QuizTakingClient class creates a form for taking a quiz;
 * is called from an instance of the ApplicationClient class.
 *
 * @author Aleksei_Semenov 17/08/16.
 */
public class QuizTakingClient extends JDialog {

    private QuizTakingGUI quizTakingGUI;
    private Quiz quiz;
    private User respondent;
    private Container container;

    private class QuizTakingGUI extends JPanel {

        JPanel answerPanel;
        JTextArea questionText;
        JList questionList;
        JButton buttonNextQuestion,
                buttonSaveAnswer, buttonFinishQuiz;

        QuizTakingGUI() {

            questionText = new JTextArea(8, 25);
            questionText.setBorder(BorderFactory.createTitledBorder("Question"));
            answerPanel = buildAnswerPanel();

            buttonNextQuestion = new JButton("Next");
            buttonSaveAnswer = new JButton("Save");
            buttonFinishQuiz = new JButton("Finish");

            JPanel leftPanel = new JPanel();
            leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
            leftPanel.add(buttonNextQuestion);
            leftPanel.add(questionText);
            leftPanel.add(buttonSaveAnswer);
            leftPanel.add(answerPanel);

            questionList = new JList();
            JScrollPane scrollPaneAllQuestions = new JScrollPane();
            scrollPaneAllQuestions.add(questionList);
            scrollPaneAllQuestions.setBorder(BorderFactory.createTitledBorder("All questions"));
            scrollPaneAllQuestions.setPreferredSize(new Dimension(150, 300));

            JPanel rightPanel = new JPanel();
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
            rightPanel.add(scrollPaneAllQuestions);
            rightPanel.add(buttonFinishQuiz);

            add(leftPanel);
            add(rightPanel);

        }


        JPanel buildAnswerPanel() {

            //There will be a query to the database to get list of possible answers for the question.

            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            for (int i = 0; i < 4; i++) {

                JCheckBox checkBox = new JCheckBox();
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx = 0;
                c.gridy = i;
                panel.add(checkBox, c);

                JTextField number = new JTextField();
                number.setText("" + (i + 1));
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx = 1;
                c.gridy = i;
                panel.add(number, c);

                JTextField textField = new JTextField(20);
                textField.setText("Answer " + (i + 1));
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx = 2;
                c.gridy = i;
                panel.add(textField, c);

            }

            return panel;
        }
    }

    private class QuizTakingHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    public QuizTakingClient(ApplicationClient frame) {
        super(frame, true);

        quizTakingGUI = new QuizTakingGUI();

        container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(quizTakingGUI, BorderLayout.CENTER);

        setTitle("Quiz-Builder (Quiz Taking)");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        pack();

        setLocationRelativeTo(null);
        setVisible(true);
        validate();
    }

    //Only to test the form
    public static void main(String[] args) {
        new QuizTakingClient(null);
    }

}
