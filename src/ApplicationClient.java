import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 * The ApplicationClient class creates the main form of the program;
 * it is opened from an instance of the LoginClient class.
 *
 * @author Aleksei_Semenov 17/08/16.
 */
public class ApplicationClient extends JFrame {

    private static ResourceBundle rb = LoginClient.rb;

    private User user;
    private ApplicationGUI applicationGUI;

    public ApplicationClient(User user) {
        this.user = user;

        applicationGUI = new ApplicationGUI();
        applicationGUI.assignedQuizPanel.refreshInfo();

        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(applicationGUI, BorderLayout.CENTER);

        setTitle("Quiz-Builder");
        addWindowListener(applicationGUI.handler);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        pack();

        setLocationRelativeTo(null);
        setVisible(true);
        validate();
    }

    /**
     * The inner MainPanel creates the main panel containing general information.
     */
    private class MainPanel extends JPanel {

        JTextArea textNewResponses, textUncompletedQuizes;
        JButton buttonShowNewResponses, bottomShowUncompletedQuizes;

        MainPanel() {

            textNewResponses = new JTextArea(4, 20);
            textNewResponses.setEditable(false);
            buttonShowNewResponses = new JButton(rb.getString("btShow"));

            textUncompletedQuizes = new JTextArea(4, 20);
            textUncompletedQuizes.setEditable(false);
            bottomShowUncompletedQuizes = new JButton(rb.getString("btShow"));

            //Puts text area to a scroll pane and add a title to the scroll pane
            JScrollPane scrollPaneNewResponses = new JScrollPane(textNewResponses);
            scrollPaneNewResponses.setBorder(BorderFactory.
                    createTitledBorder(rb.getString("tlNewResponses")));

            //Puts a scroll pane and a button to a flat panel
            JPanel panelNewResponses = new JPanel();
            panelNewResponses.add(scrollPaneNewResponses);
            panelNewResponses.add(buttonShowNewResponses);

            //Puts text area to a scroll pane and add a title to the scroll pane
            JScrollPane scrollUncompletedQuizes = new JScrollPane(textUncompletedQuizes);
            scrollUncompletedQuizes.setBorder(BorderFactory.
                    createTitledBorder(rb.getString("tlUncompletedQuizzes")));

            //Puts a scroll pane and a button to a flat panel
            JPanel panelUncompletedQuizes = new JPanel();
            panelUncompletedQuizes.add(scrollUncompletedQuizes);
            panelUncompletedQuizes.add(bottomShowUncompletedQuizes);

            JPanel wrapper = new JPanel();
            wrapper.setLayout(new GridLayout(3, 1));

            wrapper.add(new JLabel(new ImageIcon("images/logo.png")));
            wrapper.add(panelNewResponses);
            wrapper.add(panelUncompletedQuizes);
            //wrapper.setPreferredSize(new Dimension(100,100));

            //setLayout(new FlowLayout());
            add(new JScrollPane(wrapper));
        }
    }

    private class YourQuizPanel extends JPanel {

        JButton buttonNewQuiz, buttonEditQuiz, buttonDeleteQuiz,
                buttonAddUsers, buttonRemoveUsers;
        JList listAllUsers, listAssignedToUsers;

        JTable table;
        DefaultTableModel dm;
        Vector<String> headings = new Vector<>();

        YourQuizPanel() {

            headings.addElement(rb.getString("tlNumber"));
            headings.addElement(rb.getString("tlQuizName"));
            headings.addElement(rb.getString("tlType"));

            //Creates new table and adds it to the scroll pane
            dm = new DefaultTableModel(DataBaseConnector.getQuizzes(user), headings);
            table = new JTable(dm);
            formatTable();

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(530, 200));

            JPanel buttonsPanel = new JPanel();
            buttonsPanel.setLayout(new GridLayout(3, 1));

            buttonNewQuiz = new JButton(rb.getString("btNew"));
            buttonEditQuiz = new JButton(rb.getString("btEdit"));
            buttonDeleteQuiz = new JButton(rb.getString("btDelete"));

            buttonsPanel.add(buttonNewQuiz);
            buttonsPanel.add(buttonEditQuiz);
            buttonsPanel.add(buttonDeleteQuiz);

            JPanel tablePanel = new JPanel();
            tablePanel.add(scrollPane);
            tablePanel.add(buttonsPanel);

            listAllUsers = new JList();
            listAssignedToUsers = new JList();
            JScrollPane spAllUsers = new JScrollPane(listAllUsers);
            JScrollPane spAssignUsers = new JScrollPane(listAssignedToUsers);
            spAllUsers.setBorder(BorderFactory.createTitledBorder(rb.getString("tlAllUsers")));
            spAssignUsers.setBorder(BorderFactory.createTitledBorder(rb.getString("tlAssignedTo")));
            spAllUsers.setPreferredSize(new Dimension(250, 120));
            spAssignUsers.setPreferredSize(new Dimension(250, 120));

            buttonAddUsers = new JButton(">");
            buttonRemoveUsers = new JButton("<");

            JPanel buttonUserPanel = new JPanel();
            buttonUserPanel.setLayout(new GridLayout(2, 1));
            buttonUserPanel.add(buttonAddUsers);
            buttonUserPanel.add(buttonRemoveUsers);

            JPanel usersPanel = new JPanel();
            usersPanel.add(new JScrollPane(spAllUsers));
            usersPanel.add(buttonUserPanel);
            usersPanel.add(new JScrollPane(spAssignUsers));

            setLayout(new BorderLayout());
            add(new JScrollPane(tablePanel), BorderLayout.NORTH);
            add(new JScrollPane(usersPanel), BorderLayout.CENTER);
        }

        void formatTable() {

            assert (table != null) : "The variable table in the instance of the inner YourQuizPanel class " +
                    "of the ApplicationClient class is null";

            table.getColumn(rb.getString("tlNumber")).setMaxWidth(50);
            table.getColumn(rb.getString("tlQuizName")).setPreferredWidth(200);

            for (int c = 0; c < table.getColumnCount(); c++) {
                Class<?> col_class = table.getColumnClass(c);
                table.setDefaultEditor(col_class, null);        // remove editor
            }
        }

        void refreshQuizTable() {

            int currRow = table.getSelectedRow();

            dm.setDataVector(DataBaseConnector.getQuizzes(user), headings);

            formatTable();

            if (currRow >= 0) {
                table.setRowSelectionInterval(currRow, currRow);
            }
        }

        void refreshUserLists() {

            int rowInd = table.getSelectedRow();
            if (rowInd >= 0) {
                Quiz currQuiz = getCurrentQuiz();

                DefaultListModel<User> lm = new DefaultListModel<>();
                for (User u : DataBaseConnector.getUsersForAssignment(currQuiz, true)) {
                    lm.addElement(u);
                }
                listAllUsers.setModel(lm);

                lm = new DefaultListModel<>();
                for (User u : DataBaseConnector.getUsersForAssignment(currQuiz, false)) {
                    lm.addElement(u);
                }
                listAssignedToUsers.setModel(lm);
            }
        }

        Quiz getCurrentQuiz() {

            Quiz currQuiz = null;
            int rowInd = table.getSelectedRow();
            if (rowInd >= 0) {
                currQuiz = (Quiz) table.getValueAt(rowInd, 1);
            }
            return currQuiz;
        }
    }

    private class ResponsesPanel extends JPanel {

        JScrollPane scrollPane;
        JButton buttonCreateReport;

        ResponsesPanel() {

            Vector<String> headings = new Vector<>();
            headings.addElement(rb.getString("tlDate"));
            headings.addElement(rb.getString("tlRespondent"));
            headings.addElement(rb.getString("tlQuizName"));
            headings.addElement(rb.getString("tlType"));

            //Creates new table and adds it to the scroll pane
            JTable table = new JTable(new Vector(), headings);
            scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(530, 300));

            JPanel buttonsPanel = new JPanel();

            buttonCreateReport = new JButton(rb.getString("btReport"));

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

        JTable table;
        DefaultTableModel dm;
        JButton buttonStartQuiz;
        Vector<String> headings = new Vector<>();

        AssignedQuizPanel() {

            headings.addElement(rb.getString("tlNumber"));
            headings.addElement(rb.getString("tlDate"));
            headings.addElement(rb.getString("tlQuizName"));
            headings.addElement(rb.getString("tlAuthor"));
            headings.addElement(rb.getString("tlType"));
            headings.addElement(rb.getString("tlCompleted"));

            //Creates new table and adds it to the scroll pane
            dm = new DefaultTableModel(DataBaseConnector.getAssignedQuizzes(user), headings);
            table = new JTable(dm);
            formatTable();

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(530, 300));

            buttonStartQuiz = new JButton(rb.getString("btStart"));

            JPanel buttonsPanel = new JPanel();
            buttonsPanel.add(buttonStartQuiz);

            JPanel tablePanel = new JPanel();
            tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
            tablePanel.add(buttonsPanel);
            tablePanel.add(scrollPane);

            setLayout(new BorderLayout());
            add(new JScrollPane(tablePanel), BorderLayout.CENTER);
        }

        void formatTable() {

            table.getColumn(rb.getString("tlNumber")).setMaxWidth(50);
            table.getColumn(rb.getString("tlQuizName")).setPreferredWidth(200);

            for (int c = 0; c < table.getColumnCount(); c++) {
                Class<?> col_class = table.getColumnClass(c);
                table.setDefaultEditor(col_class, null);        // remove editor
            }
        }

        void refreshTable() {

            int currRow = table.getSelectedRow();
            dm.setDataVector(DataBaseConnector.getAssignedQuizzes(user), headings);

            formatTable();
            refreshInfo();
            if (currRow >= 0) {
                table.setRowSelectionInterval(currRow, currRow);
            }
        }

        void refreshInfo() {
            int newQuizzes = 0;

            for (int i = 0; i < table.getRowCount(); i++) {
                if (!(boolean) table.getValueAt(i, 5)) {
                    newQuizzes++;
                }
            }

            String info = rb.getString("msInfo1");
            if (newQuizzes > 0) {
                info = rb.getString("msInfo2") + newQuizzes + rb.getString("msInfo3")
                        + (newQuizzes > 1 ? rb.getString("msInfo4") : "");
            }
            applicationGUI.mainPanel.textUncompletedQuizes.setText(info);

        }

        Quiz getCurrentQuiz() {

            Quiz currQuiz = null;
            int rowInd = table.getSelectedRow();
            if (rowInd >= 0) {
                currQuiz = (Quiz) table.getValueAt(rowInd, 2);
            }
            return currQuiz;
        }
    }

    private class ApplicationGUI extends JPanel {

        MainPanel mainPanel;
        YourQuizPanel yourQuizPanel;
        ResponsesPanel responsesPanel;
        AssignedQuizPanel assignedQuizPanel;
        JComboBox profileAction;
        JTabbedPane tabbedPane;
        ApplicationHandler handler;

        ApplicationGUI() {

            mainPanel = new MainPanel();
            yourQuizPanel = new YourQuizPanel();
            responsesPanel = new ResponsesPanel();
            assignedQuizPanel = new AssignedQuizPanel();
            profileAction = new JComboBox(new String[]{user.toString(),
                    rb.getString("tlChangeUser")});

            handler = new ApplicationHandler();

            profileAction.addItemListener(handler);
            mainPanel.buttonShowNewResponses.addActionListener(handler);
            mainPanel.bottomShowUncompletedQuizes.addActionListener(handler);
            yourQuizPanel.buttonNewQuiz.addActionListener(handler);
            yourQuizPanel.buttonEditQuiz.addActionListener(handler);
            yourQuizPanel.buttonDeleteQuiz.addActionListener(handler);
            yourQuizPanel.buttonAddUsers.addActionListener(handler);
            yourQuizPanel.buttonRemoveUsers.addActionListener(handler);
            yourQuizPanel.table.getSelectionModel().addListSelectionListener(handler);
            responsesPanel.buttonCreateReport.addActionListener(handler);
            assignedQuizPanel.buttonStartQuiz.addActionListener(handler);

            tabbedPane = new JTabbedPane();
            tabbedPane.addTab(rb.getString("tlMain"), mainPanel);
            tabbedPane.add(rb.getString("tlYourQuizzes"), yourQuizPanel);
            tabbedPane.add(rb.getString("tlResponses"), responsesPanel);
            tabbedPane.add(rb.getString("tlAssignedQuizzes"), assignedQuizPanel);

            tabbedPane.setSelectedIndex(3);

            JPanel actionWrapper = new JPanel();
            actionWrapper.add(profileAction);

            setLayout(new BorderLayout());
            add(actionWrapper, BorderLayout.NORTH);
            add(tabbedPane, BorderLayout.CENTER);
        }
    }

    private class ApplicationHandler extends WindowAdapter implements ActionListener, ItemListener, ListSelectionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == applicationGUI.mainPanel.buttonShowNewResponses) {
                applicationGUI.tabbedPane.setSelectedIndex(2);
            } else if (e.getSource() == applicationGUI.mainPanel.bottomShowUncompletedQuizes) {
                applicationGUI.tabbedPane.setSelectedIndex(3);
            } else if (e.getSource() == applicationGUI.responsesPanel.buttonCreateReport) {
                new ReportClient(ApplicationClient.this);
            } else if (e.getSource() == applicationGUI.yourQuizPanel.buttonNewQuiz) {
                new QuizEditingClient(ApplicationClient.this, new Quiz(0, null, null, user));
                applicationGUI.yourQuizPanel.refreshQuizTable();
            } else if (e.getSource() == applicationGUI.yourQuizPanel.buttonEditQuiz) {

                Quiz currQuiz = applicationGUI.yourQuizPanel.getCurrentQuiz();
                if (currQuiz == null) {
                    JOptionPane.showMessageDialog(ApplicationClient.this, rb.getString("msSelectTheQuiz"));
                    return;
                }
                new QuizEditingClient(ApplicationClient.this, currQuiz);
                applicationGUI.yourQuizPanel.refreshQuizTable();

            } else if (e.getSource() == applicationGUI.assignedQuizPanel.buttonStartQuiz) {

                Quiz currQuiz = applicationGUI.assignedQuizPanel.getCurrentQuiz();
                if (currQuiz == null) {
                    JOptionPane.showMessageDialog(ApplicationClient.this, rb.getString("msSelectTheQuiz"));
                    return;
                }
                new QuizTakingClient(ApplicationClient.this, currQuiz, user);
                applicationGUI.assignedQuizPanel.refreshTable();

            } else if (e.getSource() == applicationGUI.yourQuizPanel.buttonAddUsers) {
                ArrayList<User> userList = new ArrayList<>(
                        applicationGUI.yourQuizPanel.listAllUsers.getSelectedValuesList());
                if (userList.size() > 0) {
                    DataBaseConnector.assignQuizToUsers(userList, applicationGUI.yourQuizPanel.getCurrentQuiz());
                    applicationGUI.yourQuizPanel.refreshUserLists();
                    applicationGUI.assignedQuizPanel.refreshTable();
                }
            } else if (e.getSource() == applicationGUI.yourQuizPanel.buttonRemoveUsers) {
                ArrayList<User> userList = new ArrayList<>(
                        applicationGUI.yourQuizPanel.listAssignedToUsers.getSelectedValuesList());
                if (userList.size() > 0) {
                    DataBaseConnector.removeQuizFromUsers(userList, applicationGUI.yourQuizPanel.getCurrentQuiz());
                    applicationGUI.yourQuizPanel.refreshUserLists();
                    applicationGUI.assignedQuizPanel.refreshTable();
                }
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

        public void windowClosing(WindowEvent e) {
            DataBaseConnector.closeConnection();
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting()) {
                return;
            }
            applicationGUI.yourQuizPanel.refreshUserLists();

        }
    }

    public static boolean fieldIsCorrect(String s, int maxLength, String fieldName) {

        s = s.trim();
        if (s.length() > 0 && s.length() <= maxLength) {
            return true;
        } else {
            JOptionPane.showMessageDialog(null, fieldName + rb.getString("msCheckValue4")
                            + maxLength + rb.getString("msCheckValue2"), rb.getString("msCheckValue3"),
                    JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
}
