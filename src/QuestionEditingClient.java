import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 * The QuestionEditingClient class creates the form for editing a question of the quiz;
 * is called from an instance of the QuizEditingClient class.
 *
 * @author Aleksei_Semenov 17/08/16.
 */
public class QuestionEditingClient extends JDialog {

    private QuestionEditingGUI questionEditingGUI;
    private Question question;
    private Container container;

    public QuestionEditingClient(QuizEditingClient dialog, Question question) {

        super(dialog, true);

        this.question = question;

        questionEditingGUI = new QuestionEditingGUI();

        container = getContentPane();
        container.setLayout(new BorderLayout());

        container.add(questionEditingGUI, BorderLayout.CENTER);

        setTitle("Quiz-Builder (Question Editing)");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        pack();

        setLocationRelativeTo(null);
        setVisible(true);
        validate();
    }

    private class QuestionEditingGUI extends JPanel {

        JTextField questionName;
        JCheckBox multipleChoice;
        JTextArea questionText;
        JButton buttonSaveQuiz, buttonNewAnswer,
                buttonEditAnswer, buttonDeleteAnswer;

        Vector<String> headings = new Vector<>();
        DefaultTableModel dm;
        JTable table;

        QuestionEditingGUI() {

            questionName = new JTextField(20);
            questionName.setBorder(BorderFactory.createTitledBorder("Question name"));
            multipleChoice = new JCheckBox("Multiple choice");
            questionText = new JTextArea(8, 20);
            questionText.setBorder(BorderFactory.createTitledBorder("Text"));

            if (question.getName() != null) {
                questionName.setText(question.getName());
                questionText.setText(question.getText());
                multipleChoice.setSelected(question.getMultipleChoice());
            }

            buttonSaveQuiz = new JButton("Save");
            buttonNewAnswer = new JButton("New");
            buttonEditAnswer = new JButton("Edit");
            buttonDeleteAnswer = new JButton("Delete");

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

            headings.addElement("N");
            headings.addElement("Answer");
            headings.addElement("Right");

            dm = new DefaultTableModel(DataBaseConnector.getAnswers(question), headings);
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

            assert(table != null): "The variable table in the instance of the inner QuestionEditingGUI class "+
                    "of the QuestionEditingClient class is null";

            table.getColumn("N").setMaxWidth(50);
            table.getColumn("Answer").setPreferredWidth(200);

            for (int c = 0; c < table.getColumnCount(); c++) {
                Class<?> col_class = table.getColumnClass(c);
                table.setDefaultEditor(col_class, null);        // remove editor
            }
        }

        void refreshAnswers() {

            int currRow = table.getSelectedRow();

            dm.setDataVector(DataBaseConnector.getAnswers(question), headings);
            formatTable();
            if (currRow >= 0) {
                table.setRowSelectionInterval(currRow, currRow);
            }
        }
    }

    private class QuestionHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == questionEditingGUI.buttonNewAnswer) {
                if (question.getId() == 0) {
                    JOptionPane.showMessageDialog(QuestionEditingClient.this, "Save the Question first");
                    return;
                }
                new AnswerEditingClient(QuestionEditingClient.this, new Answer(0, null, false, question));
                questionEditingGUI.refreshAnswers();
            } else if (e.getSource() == questionEditingGUI.buttonEditAnswer) {

                JTable table = questionEditingGUI.table;

                int rowInd = table.getSelectedRow();
                if (rowInd < 0) {
                    JOptionPane.showMessageDialog(QuestionEditingClient.this, "Select the row");
                } else {
                    new AnswerEditingClient(QuestionEditingClient.this, (Answer) table.getValueAt(rowInd, 1));
                    questionEditingGUI.refreshAnswers();
                }

            } else if (e.getSource() == questionEditingGUI.buttonSaveQuiz) {
                question.setName(questionEditingGUI.questionName.getText());
                question.setText(questionEditingGUI.questionText.getText());
                question.setMultipleChoice(questionEditingGUI.multipleChoice.isSelected());
                DataBaseConnector.saveQuestion(question);
            }
        }
    }
}
