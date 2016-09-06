import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.*;

/**
 * The ApplicationClient class creates the main form of the program;
 * can be opened from an instance of the LoginClient class.
 *
 * @author Aleksei_Semenov 17/08/16.
 */
public class ApplicationClient extends JFrame {

    private User user;
    private ApplicationGUI applicationGUI;
    private Container container;

    public ApplicationClient(User user) {
        this.user = user;

        applicationGUI = new ApplicationGUI();

        container = getContentPane();
        container.setLayout(new BorderLayout());

        container.add(applicationGUI, BorderLayout.CENTER);

        setTitle("Quiz-Builder");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        pack();

        setLocationRelativeTo(null);
        setVisible(true);
        validate();
    }


    private class MainPanel extends JPanel {

        JTextArea textNewResponses, textUncompletedQuizes;
        JButton buttonShowNewResponses, bottomShowUncompletedQuizes;

        MainPanel() {

            textNewResponses = new JTextArea(4, 20);
            textNewResponses.setEditable(false);
            buttonShowNewResponses = new JButton("Show");

            textUncompletedQuizes = new JTextArea(4, 20);
            textUncompletedQuizes.setEditable(false);
            bottomShowUncompletedQuizes = new JButton("Show");

            //Puts text area to a scroll pane and add a title to the scroll pane
            JScrollPane scrollPaneNewResponses = new JScrollPane(textNewResponses);
            scrollPaneNewResponses.setBorder(BorderFactory.createTitledBorder("New Responses"));

            //Puts a scroll pane and a button to a flat panel
            JPanel panelNewResponses = new JPanel();
            panelNewResponses.add(scrollPaneNewResponses);
            panelNewResponses.add(buttonShowNewResponses);

            //Puts text area to a scroll pane and add a title to the scroll pane
            JScrollPane scrollUncompletedQuizes = new JScrollPane(textUncompletedQuizes);
            scrollUncompletedQuizes.setBorder(BorderFactory.createTitledBorder("Uncompleted Quizes"));

            //Puts a scroll pane and a button to a flat panel
            JPanel panelUncompletedQuizes = new JPanel();
            panelUncompletedQuizes.add(scrollUncompletedQuizes);
            panelUncompletedQuizes.add(bottomShowUncompletedQuizes);


            JPanel wrapper = new JPanel();
            wrapper.setLayout(new GridLayout(2, 1));

            wrapper.add(panelNewResponses);
            wrapper.add(panelUncompletedQuizes);
            //wrapper.setPreferredSize(new Dimension(100,100));

            //setLayout(new FlowLayout());
            add(new JScrollPane(wrapper));
        }
    }

    private class YourQuizPanel extends JPanel {

        JScrollPane scrollPane;
        JButton buttonNewQuiz, buttonEditQuiz, buttonDeleteQuiz,
                buttonAddUsers, buttonRemoveUsers, buttonRefreshAllUsers;
        JList listAllUsers, listAssignedToUsers;

        YourQuizPanel() {

            Vector<String> headings = new Vector<>();
            headings.addElement("Date");
            headings.addElement("Quiz name");
            headings.addElement("Type");

            //Creates new table and adds it to the scroll pane
            JTable table = new JTable(new Vector(), headings);
            scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(530, 200));


            JPanel buttonsPanel = new JPanel();
            buttonsPanel.setLayout(new GridLayout(3, 1));

            buttonNewQuiz = new JButton("New");
            buttonEditQuiz = new JButton("Edit");
            buttonDeleteQuiz = new JButton("Delete");

            buttonsPanel.add(buttonNewQuiz);
            buttonsPanel.add(buttonEditQuiz);
            buttonsPanel.add(buttonDeleteQuiz);

            JPanel tablePanel = new JPanel();
            tablePanel.add(scrollPane);
            tablePanel.add(buttonsPanel);

            listAllUsers = new JList();
            listAssignedToUsers = new JList();
            buttonAddUsers = new JButton(">");
            buttonRemoveUsers = new JButton("<");
            buttonRefreshAllUsers = new JButton("Refresh");

            listAllUsers.setBorder(BorderFactory.createTitledBorder("All users:"));
            listAssignedToUsers.setBorder(BorderFactory.createTitledBorder("Assigned to:"));

            JPanel buttonUserPanel = new JPanel();
            buttonUserPanel.setLayout(new GridLayout(3, 1));
            buttonUserPanel.add(buttonRefreshAllUsers);
            buttonUserPanel.add(buttonAddUsers);
            buttonUserPanel.add(buttonRemoveUsers);

            JPanel usersPanel = new JPanel();
            usersPanel.add(new JScrollPane(listAllUsers));
            usersPanel.add(buttonUserPanel);
            usersPanel.add(new JScrollPane(listAssignedToUsers));

            setLayout(new BorderLayout());
            add(new JScrollPane(tablePanel), BorderLayout.NORTH);
            add(new JScrollPane(usersPanel), BorderLayout.CENTER);
        }
    }

    private class ResponsesPanel extends JPanel {

        JScrollPane scrollPane;
        JButton buttonCreateReport;

        ResponsesPanel() {

            Vector<String> headings = new Vector<>();
            headings.addElement("Date");
            headings.addElement("Respondent");
            headings.addElement("Quiz name");
            headings.addElement("Type");

            //Creates new table and adds it to the scroll pane
            JTable table = new JTable(new Vector(), headings);
            scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(530, 300));

            JPanel buttonsPanel = new JPanel();

            buttonCreateReport = new JButton("Report");

            buttonsPanel.add(buttonCreateReport);

            JPanel tablePanel = new JPanel();
            tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));

            tablePanel.add(buttonsPanel);
            tablePanel.add(scrollPane);

            setLayout(new BorderLayout());
            add(new JScrollPane(tablePanel), BorderLayout.CENTER);
        }
    }

    private class AssignedQuizPanel extends JPanel {

        JScrollPane scrollPane;
        JButton buttonStartQuiz;

        AssignedQuizPanel() {

            Vector<String> headings = new Vector<>();
            headings.addElement("Date");
            headings.addElement("Quiz name");
            headings.addElement("Author");
            headings.addElement("Type");
            headings.addElement("Completed");

            //Creates new table and adds it to the scroll pane
            JTable table = new JTable(new Vector(), headings);
            scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(530, 300));

            buttonStartQuiz = new JButton("Start");

            JPanel buttonsPanel = new JPanel();
            buttonsPanel.add(buttonStartQuiz);

            JPanel tablePanel = new JPanel();
            tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
            tablePanel.add(buttonsPanel);
            tablePanel.add(scrollPane);

            setLayout(new BorderLayout());
            add(new JScrollPane(tablePanel), BorderLayout.CENTER);
        }
    }

    private class ApplicationGUI extends JPanel {

        MainPanel mainPanel;
        YourQuizPanel yourQuizPanel;
        ResponsesPanel responsesPanel;
        AssignedQuizPanel assignedQuizPanel;
        JComboBox profileAction;
        JTabbedPane tabbedPane;

        ApplicationGUI() {

            mainPanel = new MainPanel();
            yourQuizPanel = new YourQuizPanel();
            responsesPanel = new ResponsesPanel();
            assignedQuizPanel = new AssignedQuizPanel();
            profileAction = new JComboBox(new String[]{user.getFirstName() + " " + user.getLastName(), "Change user"});

            ApplicationHandler handler = new ApplicationHandler();

            profileAction.addItemListener(handler);
            mainPanel.buttonShowNewResponses.addActionListener(handler);
            mainPanel.bottomShowUncompletedQuizes.addActionListener(handler);
            yourQuizPanel.buttonNewQuiz.addActionListener(handler);
            yourQuizPanel.buttonEditQuiz.addActionListener(handler);
            yourQuizPanel.buttonDeleteQuiz.addActionListener(handler);
            yourQuizPanel.buttonRefreshAllUsers.addActionListener(handler);
            yourQuizPanel.buttonAddUsers.addActionListener(handler);
            yourQuizPanel.buttonRemoveUsers.addActionListener(handler);
            responsesPanel.buttonCreateReport.addActionListener(handler);
            assignedQuizPanel.buttonStartQuiz.addActionListener(handler);

            tabbedPane = new JTabbedPane();
            tabbedPane.addTab("Main", mainPanel);
            tabbedPane.add("Your Quizzes", yourQuizPanel);
            tabbedPane.add("Responses", responsesPanel);
            tabbedPane.add("Assigned Quizzes", assignedQuizPanel);

            JPanel actionWrapper = new JPanel();
            actionWrapper.add(profileAction);

            setLayout(new BorderLayout());
            add(actionWrapper, BorderLayout.NORTH);
            add(tabbedPane, BorderLayout.CENTER);
        }
    }

    private class ApplicationHandler implements ActionListener, ItemListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == applicationGUI.mainPanel.buttonShowNewResponses) {
                applicationGUI.tabbedPane.setSelectedIndex(2);
            } else if (e.getSource() == applicationGUI.mainPanel.bottomShowUncompletedQuizes) {
                applicationGUI.tabbedPane.setSelectedIndex(3);
            } else if (e.getSource() == applicationGUI.responsesPanel.buttonCreateReport) {
                new ReportClient(ApplicationClient.this);
            } else if (e.getSource() == applicationGUI.yourQuizPanel.buttonNewQuiz) {
                new QuizEditingClient(ApplicationClient.this,null);
            } else if (e.getSource() == applicationGUI.assignedQuizPanel.buttonStartQuiz) {
                new QuizTakingClient(ApplicationClient.this);
            }
        }

        @Override
        public void itemStateChanged(ItemEvent e) {

            if (e.getSource() == applicationGUI.profileAction) {
                if (e.getStateChange() == ItemEvent.SELECTED &&
                        applicationGUI.profileAction.getSelectedIndex() == 1) {
                    new LoginClient();
                    dispose();
                }
            }
        }
    }
}
