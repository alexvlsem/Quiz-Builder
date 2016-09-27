import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 * The QuizTakingClient class creates a form for taking a quiz;
 * is called from an instance of the ApplicationClient class.
 *
 * @author Aleksei_Semenov 17/08/16.
 */
class QuizTakingClient extends JDialog {

    private ResourceBundle rb = LoginClient.getRb();
    private QuizTakingGUI quizTakingGUI;
    private Quiz quiz;
    private User respondent;

    private class QuizTakingGUI extends JPanel {

        JTextArea questionText;
        JList<Question> questionList;
        JButton buttonPreviousQuestion, buttonNextQuestion,
                buttonSaveAnswer, buttonFinishQuiz;
        JScrollPane scrollPaneAnswers;
        JPanel answerPanel;
        Vector<Vector> answerList = new Vector<>();

        QuizTakingGUI() {

            questionText = new JTextArea();
            questionText.setEditable(false);
            JScrollPane scrollPaneText = new JScrollPane(questionText);
            scrollPaneText.setBorder(BorderFactory.createTitledBorder(rb.getString("tlQuestion")));
            scrollPaneText.setPreferredSize(new Dimension(400, 200));

            scrollPaneAnswers = new JScrollPane();
            scrollPaneAnswers.setPreferredSize(new Dimension(400, 200));

            buttonPreviousQuestion = new JButton(rb.getString("btPrevious"));
            buttonNextQuestion = new JButton(rb.getString("btNext"));
            buttonSaveAnswer = new JButton(rb.getString("btSave"));
            buttonFinishQuiz = new JButton(rb.getString("btFinish"));

            //makes equal width for navigation buttons
            JPanel btPanel = new JPanel();
            btPanel.setLayout(new GridLayout(1, 2));
            btPanel.add(buttonPreviousQuestion);
            btPanel.add(buttonNextQuestion);
            JPanel wrapPanel = new JPanel();
            wrapPanel.add(btPanel);

            JPanel leftPanel = new JPanel();
            leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
            leftPanel.add(wrapPanel);
            leftPanel.add(scrollPaneText);
            leftPanel.add(buttonSaveAnswer);
            leftPanel.add(scrollPaneAnswers);

            //fill the list of questions
            DefaultListModel<Question> lm = new DefaultListModel<>();
            Vector rows = DataBaseConnector.getQuestions(quiz);
            for (Object row : rows) {
                lm.addElement((Question) ((Vector) row).get(1));
            }
            questionList = new JList<>(lm);
            questionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            JScrollPane scrollPaneAllQuestions = new JScrollPane(questionList);
            scrollPaneAllQuestions.setBorder(
                    BorderFactory.createTitledBorder(rb.getString("tlAllQuestions")));
            scrollPaneAllQuestions.setPreferredSize(new Dimension(200, 400));

            JPanel rightPanel = new JPanel();
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
            rightPanel.add(scrollPaneAllQuestions);
            rightPanel.add(buttonFinishQuiz);

            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            add(leftPanel);
            add(rightPanel);
            setBorder(new EmptyBorder(10, 10, 10, 10));

            QuizTakingHandler handler = new QuizTakingHandler();
            buttonPreviousQuestion.addActionListener(handler);
            buttonNextQuestion.addActionListener(handler);
            buttonFinishQuiz.addActionListener(handler);
            buttonSaveAnswer.addActionListener(handler);
            questionList.addListSelectionListener(handler);
        }

        /**
         * Creates new panel of possible answers of the chosen question.
         *
         * @param question a current question.
         * @return panel with answers.
         */
        JPanel buildAnswerPanel(Question question) {
            answerList.removeAllElements();

            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.HORIZONTAL;

            ButtonGroup group = new ButtonGroup();

            Vector<Object> rows = DataBaseConnector.getMarkedAnswers(respondent, question);
            for (int i = 0; i < rows.size(); i++) {
                Vector currRow = (Vector) rows.get(i);

                Answer currAnswer = (Answer) currRow.get(1);
                boolean isSelected = (boolean) currRow.get(2);

                c.gridx = 0;
                c.gridy = i;
                if (question.getMultipleChoice()) {
                    JCheckBox checkBox = new JCheckBox();
                    checkBox.setSelected(isSelected);
                    panel.add(checkBox, c);
                } else {
                    JRadioButton rButton = new JRadioButton();
                    rButton.setSelected(isSelected);
                    group.add(rButton);
                    panel.add(rButton, c);
                }

                JTextField number = new JTextField();
                number.setEditable(false);
                number.setText("" + currRow.get(0));
                c.gridx = 1;
                c.gridy = i;
                panel.add(number, c);

                JTextArea textArea = new JTextArea();
                textArea.setEditable(false);
                textArea.setText(currAnswer.getText());
                c.gridx = 2;
                c.gridy = i;
                panel.add(textArea, c);

                Vector<Object> rowAL = new Vector<>();
                rowAL.add(false);
                rowAL.add(currAnswer);
                answerList.add(rowAL);
            }
            return panel;
        }
    }

    /**
     * The inner QuizTakingHandler class handles all events of the QuizTakingClient class.
     */
    private class QuizTakingHandler implements ActionListener, ListSelectionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(quizTakingGUI.buttonNextQuestion)) {

                ListModel lm = quizTakingGUI.questionList.getModel();
                int currInd = quizTakingGUI.questionList.getSelectedIndex();
                if (lm.getSize() > 0 && currInd < lm.getSize()) {
                    quizTakingGUI.questionList.setSelectedIndex(++currInd);
                }
            } else if (e.getSource().equals(quizTakingGUI.buttonPreviousQuestion)) {
                ListModel lm = quizTakingGUI.questionList.getModel();
                int currInd = quizTakingGUI.questionList.getSelectedIndex();
                if (lm.getSize() > 0 && currInd > 0) {
                    quizTakingGUI.questionList.setSelectedIndex(--currInd);
                }
            } else if (e.getSource().equals(quizTakingGUI.buttonSaveAnswer)) {

                /* Depends on the structure of the variable quizTakingGUI.answerPanel.
                Iterates through JPanel; each third component starting from zero
                is JCheckBox or JRadioButton */
                Component componentArr[] = quizTakingGUI.answerPanel.getComponents();
                for (int i = 0; i < componentArr.length; i += 3) {
                    Component component = componentArr[i];
                    boolean isSelected = false;
                    if (component instanceof JRadioButton) {
                        JRadioButton currButton = (JRadioButton) component;
                        isSelected = currButton.isSelected();
                    } else if (component instanceof JCheckBox) {
                        JCheckBox currButton = (JCheckBox) component;
                        isSelected = currButton.isSelected();
                    }
                    Vector<Object> row = quizTakingGUI.answerList.get(i / 3);
                    row.set(0, isSelected);
                }
                DataBaseConnector.saveResponses(quizTakingGUI.answerList, respondent);

            } else if (e.getSource().equals(quizTakingGUI.buttonFinishQuiz)) {
                DataBaseConnector.finishQuiz(respondent, quiz);
                QuizTakingClient.this.dispose();
            }
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting()) {
                return;
            }

            if (e.getSource().equals(quizTakingGUI.questionList)) {

                Question currQuestion = quizTakingGUI.questionList.getSelectedValue();
                quizTakingGUI.questionText.setText(currQuestion.getText()); //Set the text of the question
                quizTakingGUI.questionText.setCaretPosition(0); //move to the top

                //set the answer panel
                quizTakingGUI.answerPanel = quizTakingGUI.buildAnswerPanel(currQuestion);
                quizTakingGUI.scrollPaneAnswers.setViewportView(quizTakingGUI.answerPanel);

                //move scroll bars to the top and left.
                SwingUtilities.invokeLater(() -> {
                    quizTakingGUI.scrollPaneAnswers.getVerticalScrollBar().setValue(0);
                    quizTakingGUI.scrollPaneAnswers.getHorizontalScrollBar().setValue(0);
                });
            }
        }
    }

    /**
     * The QuizTakingClient constructor.
     */
    QuizTakingClient(ApplicationClient frame, Quiz quiz, User respondent) {
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
