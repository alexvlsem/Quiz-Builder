import javax.jws.soap.SOAPBinding;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.parsers.FactoryConfigurationError;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 * The QuizTakingClient class creates a form for taking a quiz;
 * is called from an instance of the ApplicationClient class.
 *
 * @author Aleksei_Semenov 17/08/16.
 */
public class QuizTakingClient extends JDialog {

    private ResourceBundle rb = LoginClient.rb;
    private QuizTakingGUI quizTakingGUI;
    private Quiz quiz;
    private User respondent;

    private class QuizTakingGUI extends JPanel {

        JPanel answerPanel;
        JTextArea questionText;
        JList questionList;
        JButton buttonNextQuestion,
                buttonSaveAnswer, buttonFinishQuiz;
        JScrollPane scrollPaneAnswers;

        QuizTakingGUI() {

            questionText = new JTextArea();
            questionText.setEditable(false);
            JScrollPane scrollPaneText = new JScrollPane(questionText);
            scrollPaneText.setBorder(BorderFactory.createTitledBorder(rb.getString("tlQuestion")));
            scrollPaneText.setPreferredSize(new Dimension(300, 200));

            scrollPaneAnswers = new JScrollPane();
            scrollPaneAnswers.setPreferredSize(new Dimension(300, 200));

            buttonNextQuestion = new JButton(rb.getString("btNext"));
            buttonSaveAnswer = new JButton(rb.getString("btSave"));
            buttonFinishQuiz = new JButton(rb.getString("btFinish"));

            JPanel leftPanel = new JPanel();
            leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
            leftPanel.add(buttonNextQuestion);
            leftPanel.add(scrollPaneText);
            leftPanel.add(buttonSaveAnswer);
            leftPanel.add(scrollPaneAnswers);

            DefaultListModel<Question> lm = new DefaultListModel<>();
            Vector rows = DataBaseConnector.getQuestions(quiz);
            for (Object row : rows) {
                lm.addElement((Question) ((Vector) row).get(1));
            }
            questionList = new JList(lm);
            questionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            JScrollPane scrollPaneAllQuestions = new JScrollPane(questionList);
            scrollPaneAllQuestions.setBorder(
                    BorderFactory.createTitledBorder(rb.getString("tlAllQuestions")));
            scrollPaneAllQuestions.setPreferredSize(new Dimension(150, 300));

            JPanel rightPanel = new JPanel();
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
            rightPanel.add(scrollPaneAllQuestions);
            rightPanel.add(buttonFinishQuiz);

            add(leftPanel);
            add(rightPanel);

            QuizTakingHandler handler = new QuizTakingHandler();
            buttonNextQuestion.addActionListener(handler);
            buttonFinishQuiz.addActionListener(handler);
            buttonSaveAnswer.addActionListener(handler);
            questionList.addListSelectionListener(handler);

        }


        JPanel buildAnswerPanel(Question question) {

            Vector rows = DataBaseConnector.getAnswers(question);


            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            for(Object row: rows) {

                int i = rows.indexOf(row);



                JCheckBox checkBox = new JCheckBox();
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx = 0;
                c.gridy = i;
                panel.add(checkBox, c);

                JTextField number = new JTextField();
                number.setText("" + ((Vector) row).get(0));
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx = 1;
                c.gridy = i;
                panel.add(number, c);

                JTextField textField = new JTextField(20);
                textField.setText(((Answer)((Vector) row).get(1)).getText());
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx = 2;
                c.gridy = i;
                panel.add(textField, c);

            }

            return panel;
        }
    }

    private class QuizTakingHandler implements ActionListener, ListSelectionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(quizTakingGUI.buttonNextQuestion)) {

                ListModel lm = quizTakingGUI.questionList.getModel();
                int currInd = quizTakingGUI.questionList.getSelectedIndex();
                if (lm.getSize() > 0 && currInd < lm.getSize()) {
                    quizTakingGUI.questionList.setSelectedIndex(++currInd);
                }
            }

        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting()) {
                return;
            }

            if (e.getSource() == quizTakingGUI.questionList) {

                Question currQuestion = (Question) quizTakingGUI.questionList.getSelectedValue();
                quizTakingGUI.questionText.setText(currQuestion.getText());
                quizTakingGUI.questionText.setCaretPosition(0);

                quizTakingGUI.scrollPaneAnswers.setViewportView(quizTakingGUI.buildAnswerPanel(currQuestion));

            }
        }
    }

    public QuizTakingClient(ApplicationClient frame, Quiz quiz, User respondent) {
        super(frame, true);
        this.quiz = quiz;
        this.respondent = respondent;

        quizTakingGUI = new QuizTakingGUI();

        if (quizTakingGUI.questionList.getModel().getSize() > 0) {
            quizTakingGUI.questionList.setSelectedIndex(0);
        }

        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(quizTakingGUI, BorderLayout.CENTER);

        setTitle("Quiz-Builder (" + rb.getString("tlQuizTaking") + ")");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        pack();

        setLocationRelativeTo(null);
        setVisible(true);
        validate();
    }
}
