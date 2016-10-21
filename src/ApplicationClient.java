import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 * The ApplicationClient class creates the main form of the program;
 * it is opened from an instance of the LoginClient class.
 *
 * @author Aleksei_Semenov 17/08/16.
 */
class ApplicationClient extends JFrame {

    private static ResourceBundle rb = LoginClient.getRb();

    private User user;
    private ApplicationGUI applicationGUI;

    ApplicationClient(User user) {
        this.user = user;

        applicationGUI = new ApplicationGUI();
        applicationGUI.assignedQuizPanel.refreshInfo();
        applicationGUI.responsesPanel.refreshInfo();

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

            wrapper.add(new JLabel(LoginClient.createImageIcon("images/logo.png")));
            wrapper.add(panelNewResponses);
            wrapper.add(panelUncompletedQuizes);

            //setLayout(new FlowLayout());
            add(new JScrollPane(wrapper));
        }
    }

    /**
     * The inner YourQuizPanel class crates user's quiz panel;
     * allows creating, editing and deleting of the quizzes.
     */
    private class YourQuizPanel extends JPanel {

        JButton buttonNewQuiz, buttonEditQuiz, buttonDeleteQuiz,
            buttonAddUsers, buttonRemoveUsers, buttonSortUsers,
            buttonSearchUsers;
        JList<User> listAllUsers, listAssignedToUsers;

        JTable table;
        DefaultTableModel dm;
        Vector<String> headings = new Vector<>();

        /**
         * The YourQuizPanel constructor
         */
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

            listAllUsers = new JList<>();
            listAssignedToUsers = new JList<>();
            JScrollPane spAllUsers = new JScrollPane(listAllUsers);
            JScrollPane spAssignUsers = new JScrollPane(listAssignedToUsers);
            spAllUsers.setBorder(BorderFactory.createTitledBorder(
                rb.getString("tlAllUsers")));
            spAssignUsers.setBorder(BorderFactory.createTitledBorder(
                rb.getString("tlAssignedTo")));
            spAllUsers.setPreferredSize(new Dimension(250, 120));
            spAssignUsers.setPreferredSize(new Dimension(250, 120));

            buttonAddUsers = new JButton(">");
            buttonRemoveUsers = new JButton("<");
            buttonSortUsers = new JButton(rb.getString("btSort"));
            buttonSearchUsers = new JButton(rb.getString("btSearch"));

            JPanel buttonUserPanel = new JPanel();
            buttonUserPanel.setLayout(new GridLayout(4, 1));
            buttonUserPanel.add(buttonAddUsers);
            buttonUserPanel.add(buttonRemoveUsers);
            buttonUserPanel.add(buttonSortUsers);
            buttonUserPanel.add(buttonSearchUsers);


            JPanel usersPanel = new JPanel();
            usersPanel.add(new JScrollPane(spAllUsers));
            usersPanel.add(buttonUserPanel);
            usersPanel.add(new JScrollPane(spAssignUsers));

            setLayout(new BorderLayout());
            add(new JScrollPane(tablePanel), BorderLayout.NORTH);
            add(new JScrollPane(usersPanel), BorderLayout.CENTER);
        }

        void formatTable() {

            assert (table != null) : "The variable table in the instance of " +
                "the inner YourQuizPanel class " +
                "of the ApplicationClient class is null";

            table.getColumn(rb.getString("tlNumber")).setMaxWidth(50);
            table.getColumn(rb.getString("tlQuizName")).setPreferredWidth(200);

            for (int c = 0; c < table.getColumnCount(); c++) {
                Class<?> col_class = table.getColumnClass(c);
                table.setDefaultEditor(col_class, null);        // remove editor
            }
            table.setAutoCreateRowSorter(true);
        }

        void refreshQuizTable() {

            int currRow = table.getSelectedRow();

            dm.setDataVector(DataBaseConnector.getQuizzes(user), headings);

            formatTable();

            if (currRow >= 0 && currRow < table.getRowCount()) {
                table.setRowSelectionInterval(currRow, currRow);
            }
        }

        void refreshUserLists() {

            int rowInd = table.getSelectedRow();
            if (rowInd >= 0) {
                Quiz currQuiz = (Quiz) getTableValue(applicationGUI.yourQuizPanel.table, 1);

                DefaultListModel<User> lm1 = new DefaultListModel<>();
                DataBaseConnector.getUsersForAssignment(currQuiz, true).forEach(lm1::addElement);
                listAllUsers.setModel(lm1);

                DefaultListModel<User> lm2 = new DefaultListModel<>();
                DataBaseConnector.getUsersForAssignment(currQuiz, false).forEach(lm2::addElement);
                listAssignedToUsers.setModel(lm2);
            }
        }
    }

    /**
     * The inner ResponsesPanel class creates panel with responses.
     */
    private class ResponsesPanel extends JPanel {

        JTable table;
        JButton buttonCreateReport;
        Vector<String> headings;
        DefaultTableModel dm;

        ResponsesPanel() {

            headings = new Vector<>();
            headings.addElement(rb.getString("tlDate"));
            headings.addElement(rb.getString("tlRespondent"));
            headings.addElement(rb.getString("tlQuizName"));
            headings.addElement(rb.getString("tlType"));
            headings.addElement(rb.getString("tlViewed"));

            //Creates new table and adds it to the scroll pane
            dm = new DefaultTableModel(
                DataBaseConnector.getCompletedQuizzes(user), headings);
            table = new JTable(dm);
            formatTable();


            JScrollPane scrollPane = new JScrollPane(table);
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

        void refreshTable() {
            int currRow = table.getSelectedRow();

            dm.setDataVector(DataBaseConnector.getCompletedQuizzes(user), headings);

            formatTable();
            refreshInfo();
            if (currRow >= 0 && currRow < table.getRowCount()) {
                table.setRowSelectionInterval(currRow, currRow);
            }
        }

        void formatTable() {

            table.getColumn(rb.getString("tlDate")).setPreferredWidth(100);
            table.getColumn(rb.getString("tlType")).setPreferredWidth(50);

            for (int c = 0; c < table.getColumnCount(); c++) {
                Class<?> col_class = table.getColumnClass(c);
                table.setDefaultEditor(col_class, null);        // remove editor
            }
            table.setAutoCreateRowSorter(true);
        }

        void refreshInfo() {
            int newResponses = 0;

            for (int i = 0; i < table.getRowCount(); i++) {
                if (!(boolean) table.getValueAt(i, 4)) {
                    newResponses++;
                }
            }

            String info = rb.getString("msInfo5");
            if (newResponses > 0) {
                info = rb.getString("msInfo2") + newResponses +
                    rb.getString("msInfo6") +
                    (newResponses > 1 ? rb.getString("msInfo7") : "");
            }
            applicationGUI.mainPanel.textNewResponses.setText(info);
        }
    }

    /**
     * The inner AssignedQuizPanel class creates panel
     * with the quizzes assigned to the user by other users.
     */
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
            dm = new DefaultTableModel(
                DataBaseConnector.getAssignedQuizzes(user), headings);
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
            table.setAutoCreateRowSorter(true);
        }

        void refreshTable() {

            int currRow = table.getSelectedRow();
            dm.setDataVector(
                DataBaseConnector.getAssignedQuizzes(user), headings);

            formatTable();
            refreshInfo();
            if (currRow >= 0 && currRow < table.getRowCount()) {
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
    }

    /**
     * The inner ApplicationGUI class creates the main interface of the program.
     */
    private class ApplicationGUI extends JPanel {

        MainPanel mainPanel;
        YourQuizPanel yourQuizPanel;
        ResponsesPanel responsesPanel;
        AssignedQuizPanel assignedQuizPanel;
        JComboBox<String> profileAction;
        JTabbedPane tabbedPane;
        ApplicationHandler handler;

        ApplicationGUI() {

            mainPanel = new MainPanel();
            yourQuizPanel = new YourQuizPanel();
            responsesPanel = new ResponsesPanel();
            assignedQuizPanel = new AssignedQuizPanel();
            profileAction = new JComboBox<>(new String[]{user.toString(),
                rb.getString("tlChangeUser")});

            handler = new ApplicationHandler();

            profileAction.addItemListener(handler);
            mainPanel.buttonShowNewResponses.addActionListener(handler);
            mainPanel.bottomShowUncompletedQuizes.addActionListener(handler);
            yourQuizPanel.buttonNewQuiz.addActionListener(handler);
            yourQuizPanel.buttonEditQuiz.addActionListener(handler);
            yourQuizPanel.buttonDeleteQuiz.addActionListener(handler);
            yourQuizPanel.buttonAddUsers.addActionListener(handler);
            yourQuizPanel.buttonSortUsers.addActionListener(handler);
            yourQuizPanel.buttonSearchUsers.addActionListener(handler);
            yourQuizPanel.buttonRemoveUsers.addActionListener(handler);
            yourQuizPanel.table.getSelectionModel().
                addListSelectionListener(handler);
            responsesPanel.buttonCreateReport.addActionListener(handler);
            assignedQuizPanel.buttonStartQuiz.addActionListener(handler);

            tabbedPane = new JTabbedPane();
            tabbedPane.addTab(rb.getString("tlMain"), mainPanel);
            tabbedPane.add(rb.getString("tlYourQuizzes"), yourQuizPanel);
            tabbedPane.add(rb.getString("tlResponses"), responsesPanel);
            tabbedPane.add(rb.getString("tlAssignedQuizzes"), assignedQuizPanel);

            JPanel actionWrapper = new JPanel();
            actionWrapper.add(profileAction);

            setLayout(new BorderLayout());
            add(actionWrapper, BorderLayout.NORTH);
            add(tabbedPane, BorderLayout.CENTER);

        }
    }

    /**
     * The inner ApplicationHandler class handles all events of the ApplicationClient instance.
     */
    private class ApplicationHandler
        extends WindowAdapter
        implements ActionListener, ItemListener, ListSelectionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == applicationGUI.mainPanel.buttonShowNewResponses) {
                applicationGUI.tabbedPane.setSelectedIndex(2);
            } else if (e.getSource() == applicationGUI.mainPanel.bottomShowUncompletedQuizes) {
                applicationGUI.tabbedPane.setSelectedIndex(3);
            } else if (e.getSource() == applicationGUI.responsesPanel.buttonCreateReport) {

                Object currRespondent = getTableValue(applicationGUI.responsesPanel.table, 1);
                Object currQuiz = getTableValue(applicationGUI.responsesPanel.table, 2);
                if (currRespondent == null || currQuiz == null) {
                    JOptionPane.showMessageDialog(
                        ApplicationClient.this, rb.getString("msSelectTheResponse"));
                    return;
                }
                new ReportClient(ApplicationClient.this, (User) currRespondent, (Quiz) currQuiz);

                if (!(boolean) getTableValue(applicationGUI.responsesPanel.table, 4)) {
                    DataBaseConnector.makeResponseViewed((User) currRespondent, (Quiz) currQuiz);
                    applicationGUI.responsesPanel.refreshTable();
                }

            } else if (e.getSource() == applicationGUI.yourQuizPanel.buttonNewQuiz) {

                new QuizEditingClient(ApplicationClient.this, new Quiz(0, null, null, user));
                applicationGUI.yourQuizPanel.refreshQuizTable();

            } else if (e.getSource() == applicationGUI.yourQuizPanel.buttonEditQuiz) {

                Object currQuiz = getTableValue(applicationGUI.yourQuizPanel.table, 1);
                if (currQuiz == null) {
                    JOptionPane.showMessageDialog(ApplicationClient.this,
                        rb.getString("msSelectTheQuiz"));
                    return;
                }
                new QuizEditingClient(ApplicationClient.this, (Quiz) currQuiz);
                applicationGUI.yourQuizPanel.refreshQuizTable();

            } else if (e.getSource() == applicationGUI.yourQuizPanel.buttonDeleteQuiz) {
                Object currQuiz = getTableValue(applicationGUI.yourQuizPanel.table, 1);
                if (currQuiz == null) {
                    JOptionPane.showMessageDialog(ApplicationClient.this,
                        rb.getString("msSelectTheQuiz"));
                } else {
                    if (!DataBaseConnector.deleteReference((Quiz) currQuiz)) {
                        JOptionPane.showMessageDialog(ApplicationClient.this,
                            rb.getString("msQuizCantBeRemoved"));
                    } else {
                        applicationGUI.yourQuizPanel.refreshQuizTable();
                    }
                }
            } else if (e.getSource() == applicationGUI.assignedQuizPanel.buttonStartQuiz) {

                Object currQuiz = getTableValue(applicationGUI.assignedQuizPanel.table, 2);
                if (currQuiz == null) {
                    JOptionPane.showMessageDialog(ApplicationClient.this,
                        rb.getString("msSelectTheQuiz"));
                    return;
                } else if ((boolean) getTableValue(applicationGUI.assignedQuizPanel.table, 5)) {
                    JOptionPane.showMessageDialog(ApplicationClient.this,
                        rb.getString("msQuizIsCompleted"));
                    return;
                }
                new QuizTakingClient(ApplicationClient.this, (Quiz) currQuiz, user);
                applicationGUI.assignedQuizPanel.refreshTable();

            } else if (e.getSource() == applicationGUI.yourQuizPanel.buttonAddUsers) {

                ArrayList<User> userList = new ArrayList<>(
                    applicationGUI.yourQuizPanel.listAllUsers.getSelectedValuesList());

                if (userList.size() > 0) {
                    DataBaseConnector.assignQuizToUsers(userList,
                        (Quiz) getTableValue(applicationGUI.yourQuizPanel.table, 1));
                    applicationGUI.yourQuizPanel.refreshUserLists();
                    applicationGUI.assignedQuizPanel.refreshTable();
                }
            } else if (e.getSource() == applicationGUI.yourQuizPanel.buttonRemoveUsers) {

                ArrayList<User> userList = new ArrayList<>(
                    applicationGUI.yourQuizPanel.listAssignedToUsers.getSelectedValuesList());

                if (userList.size() > 0) {
                    DataBaseConnector.removeQuizFromUsers(userList,
                        (Quiz) getTableValue(applicationGUI.yourQuizPanel.table, 1));
                    applicationGUI.yourQuizPanel.refreshUserLists();
                    applicationGUI.assignedQuizPanel.refreshTable();
                }
            } else if (e.getSource() == applicationGUI.yourQuizPanel.buttonSortUsers) {
                selectFromPopupMenu(applicationGUI.yourQuizPanel.buttonSortUsers);
            } else if (e.getSource() == applicationGUI.yourQuizPanel.buttonSearchUsers) {
                selectFromPopupMenu(applicationGUI.yourQuizPanel.buttonSearchUsers);
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

        /**
         * The selectFromPopupMenu method creates a popup menu, sorts the list
         * of all users or finds a user depending on a calling button and a
         * selected item of the menu.
         *
         * @param button that calls the popup menu
         */
        private void selectFromPopupMenu(JButton button) {
            String[] menuItems = {rb.getString("lbFirstName"),
                rb.getString("lbLastName"), rb.getString("lbLogin")};
            JPopupMenu popup = new JPopupMenu();

            ActionListener menuListener = new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    //A collection that is sorted or searched
                    List<User> users = new ArrayList<>();
                    ListModel<User> model =
                        applicationGUI.yourQuizPanel.listAllUsers.getModel();
                    for (int index = 0; index < model.getSize(); index++) {
                        users.add(model.getElementAt(index));
                    }

                    //if the users' list is empty then stop the flow
                    if (users.size() == 0) {
                        return;
                    }

                    String attribute = event.getActionCommand();
                    int commandIndex = Arrays.asList(menuItems).indexOf(attribute);
                    //Creates a comparator
                    Comparator<User> comparator = new Comparator<User>() {
                        @Override
                        public int compare(User o1, User o2) {
                            switch (commandIndex) {
                                case 0:
                                    return o1.getFirstName().compareTo(o2.getFirstName());
                                case 1:
                                    return o1.getLastName().compareTo(o2.getLastName());
                                default:
                                    return o1.getId().compareTo(o2.getId());
                            }
                        }
                    };

                    //Sorts the users' collection
                    Collections.sort(users, comparator);

                    if (button == applicationGUI.yourQuizPanel.buttonSearchUsers) {

                        String searchValue = JOptionPane.showInputDialog(applicationGUI,
                            rb.getString("tlEnterTheValue") + attribute);
                        if (searchValue == null) {
                            return; //the enter was canceled
                        }

                        User searchingUser;
                        switch (commandIndex) {
                            case 0:
                                searchingUser = new User(null, searchValue, null);
                                break;
                            case 1:
                                searchingUser = new User(null, null, searchValue);
                                break;
                            default:
                                searchingUser = new User(searchValue, null, null);
                                break;
                        }

                        //Searches the user in the collection
                        int index = Collections.binarySearch(users, searchingUser, comparator);

                        if (index >= 0) { //if a user is found then selects him in the list
                            searchingUser = users.get(index);
                            applicationGUI.yourQuizPanel.listAllUsers.
                                setSelectedIndex(((DefaultListModel<User>) model).
                                    indexOf(searchingUser));
                        }

                    } else if (button == applicationGUI.yourQuizPanel.buttonSortUsers) {
                        //Fills the list with the sorted collection
                        DefaultListModel<User> lm = new DefaultListModel<>();
                        users.forEach(lm::addElement);
                        applicationGUI.yourQuizPanel.listAllUsers.setModel(lm);
                    }

                }
            };

            //Fills the menu
            JMenuItem item;
            for (String itemName : menuItems) {
                popup.add(item = new JMenuItem(itemName));
                item.setHorizontalTextPosition(JMenuItem.RIGHT);
                item.addActionListener(menuListener);
            }
            popup.setBorder(new BevelBorder(BevelBorder.RAISED));
            //Shows menu under the button
            popup.show(button, (int) button.getAlignmentX(),
                (int) button.getAlignmentY() + button.getHeight());
        }
    }

    /**
     * The fieldIsCorrect method checks that the value is not empty and does't
     * exceed max length.
     *
     * @param string    the checked value.
     * @param maxLength max length of the checked value.
     * @param fieldName the name of the field name of the checked value.
     * @return result of checking
     */
    static boolean fieldIsCorrect(String string, int maxLength, String fieldName) {

        string = string.trim();
        if (string.length() > 0 && string.length() <= maxLength) {
            return true;
        } else {
            JOptionPane.showMessageDialog(null,
                fieldName + rb.getString("msCheckValue4") +
                    maxLength + rb.getString("msCheckValue2"),
                rb.getString("msCheckValue3"),
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * The getTableValue method gets a value from the table.
     *
     * @param table  the table from which value is gotten.
     * @param column the number of column.
     * @return the gotten value or null.
     */
    static Object getTableValue(JTable table, int column) {

        Object returnedValue = null;

        int rowInd = table.getSelectedRow();
        if (rowInd >= 0) {
            returnedValue = table.getValueAt(rowInd, column);
        }
        return returnedValue;
    }
}
