import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

/**
 * The QuizEditingClient class creates a form for editing a quiz;
 * its instance is called from an instance of the ApplicationClient class.
 *
 * @author Aleksei_Semenov 17/08/16.
 */
public class QuizEditingClient extends JDialog {

    private QuizEditingGUI quizEditingGUI;
    private Quiz quiz;
    private Container container;

    public QuizEditingClient(ApplicationClient applicationClient, Quiz quiz) {

        super(applicationClient, true);

        assert (quiz != null):
                "The instance of the Quiz class is null in the QuizEditingClient constructor";

        this.quiz = quiz;

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
        Vector<String> headings = new Vector<>();
        DefaultTableModel dm;
        JTable table;

        QuizEditingGUI() {

            quizName = new JTextField(30);
            quizName.setBorder(BorderFactory.createTitledBorder("Quiz Name"));
            quizTypes = new JComboBox(QuizTypes.values());
            quizTypes.setBorder(BorderFactory.createTitledBorder("Type"));

            if (quiz.getName() != null) {
                quizName.setText(quiz.getName());
                quizTypes.setSelectedItem(quiz.getType());
            }

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
            buttonPanel.setLayout(new GridLayout(3, 1));
            buttonPanel.add(buttonNewQuestion);
            buttonPanel.add(buttonEditQuestion);
            buttonPanel.add(buttonDeleteQuestion);

            headings.addElement("N");
            headings.addElement("Question");
            headings.addElement("Text");
            headings.addElement("Multiple Choise");

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

        void formatTable(){

            table.getColumn("N").setMaxWidth(50);
            table.getColumn("Text").setPreferredWidth(200);

            for (int c = 0; c < table.getColumnCount(); c++)
            {
                Class<?> col_class = table.getColumnClass(c);
                table.setDefaultEditor(col_class, null);        // remove editor
            }
        }

        void refreshQuestions() {

            int currRow = table.getSelectedRow();

            dm.setDataVector(DataBaseConnector.getQuestions(quiz), headings);
            formatTable();
            if (currRow >= 0) {
                table.setRowSelectionInterval(currRow, currRow);
            }
        }

        class QuizHandler implements ActionListener, ItemListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == quizEditingGUI.buttonNewQuestion) {
                    if (quiz.getId() == 0){
                        JOptionPane.showMessageDialog(QuizEditingClient.this, "Save the Quiz first");
                        return;
                    }
                    new QuestionEditingClient(QuizEditingClient.this, new Question(0, null, null, true, quiz));
                    refreshQuestions();
                } else if (e.getSource() == quizEditingGUI.buttonEditQuestion) {

                    JTable table = quizEditingGUI.table;

                    int rowInd = table.getSelectedRow();
                    if (rowInd < 0) {
                        JOptionPane.showMessageDialog(QuizEditingClient.this, "Select the Question");
                    } else {
                        new QuestionEditingClient(QuizEditingClient.this, (Question) table.getValueAt(rowInd, 1));
                        refreshQuestions();
                    }

                } else if (e.getSource() == quizEditingGUI.buttonSaveQuiz) {
                    quiz.setName(quizEditingGUI.quizName.getText());
                    quiz.setType((QuizTypes) quizEditingGUI.quizTypes.getSelectedItem());
                    DataBaseConnector.saveQuiz(quiz);
                }
            }

            @Override
            public void itemStateChanged(ItemEvent e) {

            }
        }
    }
}
