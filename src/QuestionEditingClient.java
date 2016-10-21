import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 * The QuestionEditingClient class creates the form
 * for editing a question of the quiz;
 * is called from an instance of the QuizEditingClient class.
 *
 * @author Aleksei_Semenov 17/08/16.
 */
class QuestionEditingClient extends JDialog {

    private ResourceBundle rb = LoginClient.getRb();
    private QuestionEditingGUI questionEditingGUI;
    private Question question;

    QuestionEditingClient(QuizEditingClient dialog, Question question) {

        super(dialog, true);
        this.question = question;

        questionEditingGUI = new QuestionEditingGUI();

        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(questionEditingGUI, BorderLayout.CENTER);

        setTitle("Quiz-Builder (" + rb.getString("tlQuestionEditing") + ")");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        validate();
    }

    /**
     * The inner QuestionEditingGUI class creates a panel for editing a question.
     */
    private class QuestionEditingGUI extends JPanel {

        JTextField questionName;
        JCheckBox multipleChoice;
        JTextArea questionText;
        JButton buttonSaveQuiz, buttonNewAnswer, buttonEditAnswer,
            buttonDeleteAnswer;

        Vector<String> headings = new Vector<>();
        DefaultTableModel dm;
        JTable table;

        QuestionEditingGUI() {

            questionName = new JTextField(20);
            questionName.setBorder(BorderFactory.createTitledBorder(
                rb.getString("tlQuestionName")));
            multipleChoice = new JCheckBox(rb.getString("tlMultipleChoice"));
            questionText = new JTextArea(8, 20);
            questionText.setBorder(BorderFactory.createTitledBorder(
                rb.getString("tlText")));

            if (question.getName() != null) {
                questionName.setText(question.getName());
                questionText.setText(question.getText());
                multipleChoice.setSelected(question.getMultipleChoice());
            }

            buttonSaveQuiz = new JButton(rb.getString("btSave"));
            buttonNewAnswer = new JButton(rb.getString("btNew"));
            buttonEditAnswer = new JButton(rb.getString("btEdit"));
            buttonDeleteAnswer = new JButton(rb.getString("btDelete"));

            QuestionHandler handler = new QuestionHandler();
            buttonSaveQuiz.addActionListener(handler);
            buttonNewAnswer.addActionListener(handler);
            buttonEditAnswer.addActionListener(handler);
            buttonDeleteAnswer.addActionListener(handler);

            JPanel header = new JPanel();
            header.add(questionName);
            header.add(multipleChoice);
            header.add(buttonSaveQuiz);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(3, 1));
            buttonPanel.add(buttonNewAnswer);
            buttonPanel.add(buttonEditAnswer);
            buttonPanel.add(buttonDeleteAnswer);

            headings.addElement(rb.getString("tlNumber"));
            headings.addElement(rb.getString("tlAnswer"));
            headings.addElement(rb.getString("tlRight"));

            dm = new DefaultTableModel(DataBaseConnector.getAnswers(question),
                headings);
            table = new JTable(dm);
            formatTable();

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(400, 150));

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

        void formatTable() {

            assert (table != null) : "The variable table in the instance of " +
                "the inner QuestionEditingGUI class " +
                "of the QuestionEditingClient class is null";

            table.getColumn(rb.getString("tlNumber")).setMaxWidth(50);
            table.getColumn(rb.getString("tlAnswer")).setPreferredWidth(200);

            for (int c = 0; c < table.getColumnCount(); c++) {
                Class<?> col_class = table.getColumnClass(c);
                table.setDefaultEditor(col_class, null);   // remove editor
            }
        }

        void refreshAnswers() {

            int currRow = table.getSelectedRow();

            dm.setDataVector(DataBaseConnector.getAnswers(question), headings);
            formatTable();
            if (currRow >= 0 && currRow < table.getRowCount()) {
                table.setRowSelectionInterval(currRow, currRow);
            }
        }
    }

    /**
     * The inner QuestionHandler class handles all events of the
     * QuestionEditingClient instance.
     */
    private class QuestionHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == questionEditingGUI.buttonNewAnswer) {
                if (question.getId() == 0) {
                    JOptionPane.showMessageDialog(QuestionEditingClient.this,
                        rb.getString("msSaveTheQuestion"));
                    return;
                }
                new AnswerEditingClient(QuestionEditingClient.this,
                    new Answer(0, null, false, question));
                questionEditingGUI.refreshAnswers();
            } else if (e.getSource() == questionEditingGUI.buttonEditAnswer) {

                Object answer =
                    ApplicationClient.getTableValue(questionEditingGUI.table, 1);

                if (answer == null) {
                    JOptionPane.showMessageDialog(QuestionEditingClient.this,
                        rb.getString("msSelectTheRow"));
                } else {
                    new AnswerEditingClient(QuestionEditingClient.this,
                        (Answer) answer);
                    questionEditingGUI.refreshAnswers();
                }
            } else if (e.getSource() == questionEditingGUI.buttonSaveQuiz) {

                String questionName = questionEditingGUI.questionName.getText(),
                    questionText = questionEditingGUI.questionText.getText();
                if (ApplicationClient.fieldIsCorrect(questionName, 225,
                    rb.getString("tlQuestionName")) &&
                    ApplicationClient.fieldIsCorrect(questionText, 2000,
                        rb.getString("tlText"))) {
                    question.setName(questionName);
                    question.setText(questionText);
                    question.setMultipleChoice(
                        questionEditingGUI.multipleChoice.isSelected());
                    DataBaseConnector.saveQuestion(question);
                }
            } else if (e.getSource() == questionEditingGUI.buttonDeleteAnswer) {
                Object answer =
                    ApplicationClient.getTableValue(questionEditingGUI.table, 1);

                if (answer == null) {
                    JOptionPane.showMessageDialog(QuestionEditingClient.this,
                        rb.getString("msSelectTheRow"));
                } else {
                    if (!DataBaseConnector.deleteReference((Answer) answer)) {
                        JOptionPane.showMessageDialog(QuestionEditingClient.this,
                            rb.getString("msAnswerCantBeRemoved"));
                    } else {
                        questionEditingGUI.refreshAnswers();
                    }
                }
            }
        }
    }
}
