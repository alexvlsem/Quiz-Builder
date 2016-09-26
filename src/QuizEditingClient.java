import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 * The QuizEditingClient class creates a form for editing a quiz;
 * its instance is called from an instance of the ApplicationClient class.
 *
 * @author Aleksei_Semenov 17/08/16.
 */
class QuizEditingClient extends JDialog {

    private ResourceBundle rb = LoginClient.rb;
    private QuizEditingGUI quizEditingGUI;
    private Quiz quiz;

    QuizEditingClient(ApplicationClient applicationClient, Quiz quiz) {

        super(applicationClient, true);
        this.quiz = quiz;

        quizEditingGUI = new QuizEditingGUI();

        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(quizEditingGUI, BorderLayout.CENTER);

        setTitle("Quiz-Builder (" + rb.getString("tlQuizEditing") + ")");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        pack();

        setLocationRelativeTo(null);
        setVisible(true);
        validate();
    }

    /**
     * The QuizEditingGUI class creates interface for editing a quiz.
     */
    private class QuizEditingGUI extends JPanel {

        JTextField quizName;
        JComboBox<QuizTypes> quizTypes;
        JButton buttonSaveQuiz, buttonNewQuestion,
                buttonEditQuestion, buttonDeleteQuestion;
        Vector<String> headings = new Vector<>();
        DefaultTableModel dm;
        JTable table;

        QuizEditingGUI() {

            quizName = new JTextField(30);
            quizName.setBorder(BorderFactory.createTitledBorder(rb.getString("tlQuizName")));
            quizTypes = new JComboBox<>(QuizTypes.values());
            quizTypes.setBorder(BorderFactory.createTitledBorder(rb.getString("tlType")));

            if (quiz.getName() != null) {
                quizName.setText(quiz.getName());
                quizTypes.setSelectedItem(quiz.getType());
            }

            buttonSaveQuiz = new JButton(rb.getString("btSave"));
            buttonNewQuestion = new JButton(rb.getString("btNew"));
            buttonEditQuestion = new JButton(rb.getString("btEdit"));
            buttonDeleteQuestion = new JButton(rb.getString("btDelete"));

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
            buttonPanel.setLayout(new GridLayout(3, 1));
            buttonPanel.add(buttonNewQuestion);
            buttonPanel.add(buttonEditQuestion);
            buttonPanel.add(buttonDeleteQuestion);

            headings.addElement(rb.getString("tlNumber"));
            headings.addElement(rb.getString("tlQuestion"));
            headings.addElement(rb.getString("tlText"));
            headings.addElement(rb.getString("tlMultipleChoice"));

            //Creates new table and adds it to the scroll pane
            dm = new DefaultTableModel(DataBaseConnector.getQuestions(quiz), headings);
            table = new JTable(dm);
            formatTable();

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(530, 300));

            JPanel panelTabButt = new JPanel();
            panelTabButt.add(scrollPane);
            panelTabButt.add(buttonPanel);

            JPanel wrapper = new JPanel();
            wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
            wrapper.add(header);
            wrapper.add(panelTabButt);

            add(wrapper);
        }

        void formatTable() {

            assert (table != null) : "The variable table in the instance of the inner QuizEditingGUI class " +
                    "of the QuizEditingClient class is null";

            table.getColumn(rb.getString("tlNumber")).setMaxWidth(50);
            table.getColumn(rb.getString("tlText")).setPreferredWidth(200);

            for (int c = 0; c < table.getColumnCount(); c++) {
                Class<?> col_class = table.getColumnClass(c);
                table.setDefaultEditor(col_class, null);        // remove editor
            }
        }

        void refreshQuestions() {

            int currRow = table.getSelectedRow();

            dm.setDataVector(DataBaseConnector.getQuestions(quiz), headings);
            formatTable();
            if (currRow >= 0 && currRow < table.getRowCount()) {
                table.setRowSelectionInterval(currRow, currRow);
            }
        }
    }

    /**
     * The QuizHandler class handles all events of the form.
     */
    private class QuizHandler implements ActionListener, ItemListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == quizEditingGUI.buttonNewQuestion) {
                if (quiz.getId() == 0) {
                    JOptionPane.showMessageDialog(QuizEditingClient.this, rb.getString("msSaveTheQuiz"));
                    return;
                }
                new QuestionEditingClient(QuizEditingClient.this, new Question(0, null, null, true, quiz));
                quizEditingGUI.refreshQuestions();
            } else if (e.getSource() == quizEditingGUI.buttonEditQuestion) {
                Object question = ApplicationClient.getTableValue(quizEditingGUI.table, 1);

                if (question == null) {
                    JOptionPane.showMessageDialog(QuizEditingClient.this, rb.getString("msSelectTheQuestion"));
                } else {
                    new QuestionEditingClient(QuizEditingClient.this, (Question) question);
                    quizEditingGUI.refreshQuestions();
                }
            } else if (e.getSource() == quizEditingGUI.buttonSaveQuiz) {

                String quizName = quizEditingGUI.quizName.getText();
                if (ApplicationClient.fieldIsCorrect(quizName, 255, rb.getString("tlQuizName"))) {
                    quiz.setName(quizName);
                    quiz.setType((QuizTypes) quizEditingGUI.quizTypes.getSelectedItem());
                    DataBaseConnector.saveQuiz(quiz);
                }
            } else if (e.getSource() == quizEditingGUI.buttonDeleteQuestion) {
                Object question = ApplicationClient.getTableValue(quizEditingGUI.table, 1);

                if (question == null) {
                    JOptionPane.showMessageDialog(QuizEditingClient.this, rb.getString("msSelectTheQuestion"));
                } else {
                    if (!DataBaseConnector.deleteReference((Question) question)) {
                        JOptionPane.showMessageDialog(QuizEditingClient.this, rb.getString("msQuestionCantBeRemoved"));
                    } else {
                        quizEditingGUI.refreshQuestions();
                    }
                }
            }
        }

        @Override
        public void itemStateChanged(ItemEvent e) {

        }
    }
}
