import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;

/**
 * The LoginClient class is the entrance to the program; creates a form for signing in,
 * creating a user and saving settings. Its instance can be called from an instance of
 * the ApplicationClient class during changing the user.
 *
 * @author Aleksei_Semenov on 17/08/16.
 */
public class LoginClient extends JFrame {

    private static Settings settings;
    private static ResourceBundle rb;

    private LoginPanelGUI loginPanelGUI;
    private JTabbedPane tabbedPane;

    /**
     * The LoginPanelGUI class creates interface of the login panel.
     */
    private class LoginPanelGUI extends JPanel {

        LoginPanel loginPanel;
        CreateUserPanel createUserPanel;
        SettingsPanel settingsPanel;
        LoginHandler handler;

        LoginPanelGUI() {

            loginPanel = new LoginPanel();
            createUserPanel = new CreateUserPanel();
            settingsPanel = new SettingsPanel();

            handler = new LoginHandler();
            loginPanel.password.addActionListener(handler);
            loginPanel.buttonSignIn.addActionListener(handler);
            createUserPanel.buttonCreateUser.addActionListener(handler);
            settingsPanel.buttonSave.addActionListener(handler);
            settingsPanel.buttonTestConnection.addActionListener(handler);

            tabbedPane = new JTabbedPane(JTabbedPane.TOP);
            tabbedPane.addTab(rb.getString("tlLogIn"), loginPanel);
            tabbedPane.addTab(rb.getString("tlCreateUser"), createUserPanel);
            tabbedPane.addTab(rb.getString("tlSettings"), settingsPanel);

            add(tabbedPane);
        }
    }

    /**
     * The inner LoginPanel class creates a login panel.
     */
    private class LoginPanel extends JPanel {
        JTextField login;
        JPasswordField password;
        JButton buttonSignIn;

        LoginPanel() {

            login = new JTextField(15);
            password = new JPasswordField();
            buttonSignIn = new JButton(rb.getString("btSignIn"));

            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            JLabel logoLabel = new JLabel(createImageIcon("images/logo.png"));

            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridwidth = 2;
            c.gridy = 0;
            add(logoLabel, c);

            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridwidth = 1;
            c.gridx = 0;
            c.gridy = 1;
            add(new JLabel(rb.getString("lbLogin"), JLabel.LEFT), c);

            c.gridx = 1;
            c.gridy = 1;
            add(login, c);

            c.gridx = 0;
            c.gridy = 2;
            add(new JLabel(rb.getString("lbPassword"), JLabel.LEFT), c);

            c.gridx = 1;
            c.gridy = 2;
            add(password, c);

            c.fill = GridBagConstraints.CENTER;
            c.gridx = 1;
            c.gridy = 3;
            add(buttonSignIn, c);
        }
    }

    /**
     * The inner CreateUserPanel class creates a panel for creating a user.
     */
    private class CreateUserPanel extends JPanel {
        JTextField login, firstName, lastName;
        JPasswordField password;
        JButton buttonCreateUser;

        CreateUserPanel() {

            login = new JTextField(15);
            password = new JPasswordField();
            firstName = new JTextField();
            lastName = new JTextField();
            buttonCreateUser = new JButton(rb.getString("btCreate"));

            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 0;
            add(new JLabel(rb.getString("lbLogin"), JLabel.LEFT), c);

            c.gridx = 1;
            c.gridy = 0;
            add(login, c);

            c.gridx = 0;
            c.gridy = 1;
            add(new JLabel(rb.getString("lbFirstName"), JLabel.LEFT), c);

            c.gridx = 1;
            c.gridy = 1;
            add(firstName, c);

            c.gridx = 0;
            c.gridy = 2;
            add(new JLabel(rb.getString("lbLastName"), JLabel.LEFT), c);

            c.gridx = 1;
            c.gridy = 2;
            add(lastName, c);

            c.gridx = 0;
            c.gridy = 3;
            add(new JLabel(rb.getString("lbPassword"), JLabel.LEFT), c);

            c.gridx = 1;
            c.gridy = 3;
            add(password, c);

            c.gridx = 1;
            c.gridy = 4;
            add(buttonCreateUser, c);
        }
    }

    /**
     * The inner SettingsPanel class creates a panel for storing settings.
     */
    private class SettingsPanel extends JPanel {

        JTextField server, database, login;
        JPasswordField password;
        JButton buttonSave;
        JButton buttonTestConnection;
        JComboBox<InterfaceLanguages> boxLanguage;

        SettingsPanel() {

            server = new JTextField(15);
            database = new JTextField();
            login = new JTextField();
            password = new JPasswordField();
            buttonSave = new JButton(rb.getString("btSave"));
            buttonTestConnection = new JButton(rb.getString("btTest"));
            boxLanguage = new JComboBox<>(InterfaceLanguages.values());

            if (settings != null) {
                server.setText(settings.getServer());
                database.setText(settings.getDatabase());
                login.setText(settings.getLogin());
                password.setText(new String(settings.getPassword()));
                boxLanguage.setSelectedItem(settings.getLocalization());
            }

            JPanel connectionPanel = new JPanel();
            connectionPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 0;
            connectionPanel.add(new JLabel(rb.getString("lbServer"), JLabel.LEFT), c);

            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 1;
            c.gridy = 0;
            connectionPanel.add(server, c);

            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 1;
            connectionPanel.add(new JLabel(rb.getString("lbDatabase"), JLabel.LEFT), c);

            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 1;
            c.gridy = 1;
            connectionPanel.add(database, c);

            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 2;
            connectionPanel.add(new JLabel(rb.getString("lbLogin"), JLabel.LEFT), c);

            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 1;
            c.gridy = 2;
            connectionPanel.add(login, c);

            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 3;
            connectionPanel.add(new JLabel(rb.getString("lbPassword"), JLabel.LEFT), c);

            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 1;
            c.gridy = 3;
            connectionPanel.add(password, c);

            c.gridx = 1;
            c.gridy = 4;
            connectionPanel.add(buttonTestConnection, c);

            connectionPanel.setBorder(BorderFactory.createTitledBorder(
                    rb.getString("tlConnection")));

            JPanel languagePanel = new JPanel();
            languagePanel.add(new JLabel(rb.getString("lbLanguage")));
            languagePanel.add(boxLanguage);
            languagePanel.add(buttonSave);

            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            add(connectionPanel);
            add(languagePanel);
        }
    }

    /**
     * The inner LoginHandler class handles all actions of the LoginPanelGUI and LoginClient instances.
     */
    private class LoginHandler extends WindowAdapter implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == loginPanelGUI.loginPanel.password ||
                    e.getSource() == loginPanelGUI.loginPanel.buttonSignIn) {

                String login = loginPanelGUI.loginPanel.login.getText();
                String password = new String(loginPanelGUI.loginPanel.password.getPassword());

                if (checkValue(login, 25, rb.getString("lbLogin")) &&
                        checkValue(password, 8, rb.getString("lbPassword"))) {

                    DataBaseConnector.createConnection();

                    ArrayList<String> userData = DataBaseConnector.getUserRecord(
                            new String[]{login, password});

                    if (userData.size() == 2) {
                        User currentUser = new User(login, userData.get(0), userData.get(1));

                        dispose();
                        new ApplicationClient(currentUser);
                    } else {
                        JOptionPane.showMessageDialog(loginPanelGUI, userData.get(0));
                    }
                }
            } else if (e.getSource() == loginPanelGUI.createUserPanel.buttonCreateUser) {

                String login = loginPanelGUI.createUserPanel.login.getText();
                String firstName = loginPanelGUI.createUserPanel.firstName.getText();
                String lastName = loginPanelGUI.createUserPanel.lastName.getText();
                String password = new String(loginPanelGUI.createUserPanel.password.getPassword());

                if (checkValue(login, 25, rb.getString("lbLogin")) &&
                        checkValue(firstName, 50, rb.getString("lbFirstName")) &&
                        checkValue(lastName, 50, rb.getString("lbLastName")) &&
                        checkValue(password, 8, rb.getString("lbPassword"))) {

                    DataBaseConnector.createConnection();
                    ArrayList<String> userData = DataBaseConnector.createUser(
                            new String[]{login, firstName, lastName, password});
                    if (userData.size() == 1) {
                        JOptionPane.showMessageDialog(loginPanelGUI, userData.get(0));
                    } else {
                        JOptionPane.showMessageDialog(loginPanelGUI, rb.getString("msUserCreated"));
                        loginPanelGUI.loginPanel.login.setText(login);
                        tabbedPane.setSelectedIndex(0);
                    }
                }
            } else if (e.getSource() == loginPanelGUI.settingsPanel.buttonSave) {

                if (settings == null) {
                    settings = Settings.getInstance();
                }

                settings.setServer(loginPanelGUI.settingsPanel.server.getText());
                settings.setDatabase(loginPanelGUI.settingsPanel.database.getText());
                settings.setLogin(loginPanelGUI.settingsPanel.login.getText());
                settings.setPassword(loginPanelGUI.settingsPanel.password.getPassword());
                settings.setLocalization((
                        InterfaceLanguages) loginPanelGUI.settingsPanel.boxLanguage.getSelectedItem());
                try {
                    ObjectOutputStream os = new ObjectOutputStream(
                            new FileOutputStream(new File(LoginClient.class.getResource("").getFile(), "settings.txt")));
                    os.writeObject(settings);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            } else if (e.getSource() == loginPanelGUI.settingsPanel.buttonTestConnection) {
                if (settings == null) {
                    settings = Settings.getInstance();
                }
                settings.setServer(loginPanelGUI.settingsPanel.server.getText());
                settings.setDatabase(loginPanelGUI.settingsPanel.database.getText());
                settings.setLogin(loginPanelGUI.settingsPanel.login.getText());
                settings.setPassword(loginPanelGUI.settingsPanel.password.getPassword());
                settings.setLocalization((
                        InterfaceLanguages) loginPanelGUI.settingsPanel.boxLanguage.getSelectedItem());

                DataBaseConnector.closeConnection();
                DataBaseConnector.createConnection();
                if (DataBaseConnector.connectionStatus()) {
                    JOptionPane.showMessageDialog(loginPanelGUI, rb.getString("msConnectionSuccess"), "",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(loginPanelGUI, rb.getString("msConnectionFailed"), "",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        public void windowClosing(WindowEvent e) {
            DataBaseConnector.closeConnection();
        }
    }

    /**
     * The LoginClient class constructor.
     */
    public LoginClient() {

        loginPanelGUI = new LoginPanelGUI();

        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(loginPanelGUI, BorderLayout.CENTER);

        setTitle("Quiz Builder");
        addWindowListener(loginPanelGUI.handler);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();

        setLocationRelativeTo(null);
        setVisible(true);
        validate();
    }

    /**
     * The checkValue method checks values of the forms.
     *
     * @param s         the entered value
     * @param maxLength the max length of the value
     * @param fieldName the name of the entered value
     * @return boolean the result.
     */
    private static boolean checkValue(String s, int maxLength, String fieldName) {

        Pattern pattern = Pattern.compile(String.format("[a-zA-Zа-яА-Я\\d_]{1,%d}", maxLength));
        Matcher matcher = pattern.matcher(s);
        if (matcher.find() && matcher.group().equals(s)) {
            return true;
        } else {
            JOptionPane.showMessageDialog(null, fieldName + rb.getString("msCheckValue1")
                            + maxLength + rb.getString("msCheckValue2"), rb.getString("msCheckValue3"),
                    JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    /**
     * The loadSettings method loads settings from the file,
     * instantiates the settings and rb variables.
     */
    private static void loadSettings() {

        if (LoginClient.class.getResource("settings.txt") != null) {
            try {
                ObjectInputStream ois = new ObjectInputStream(
                        new FileInputStream(LoginClient.class.getResource("settings.txt").getFile()));
                settings = (Settings) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        Locale locale = new Locale("en");
        if (settings != null) {
            locale = new Locale(settings.getLocalization().getLanguage(),
                    settings.getLocalization().getCountry());
        }
        rb = ResourceBundle.getBundle("Localization.Labels", locale);
    }

    /**
     * The entrance of the program.
     */
    public static void main(String[] args) {

        try {
            // Set cross-platform Java L&F (also called "Metal")
            UIManager.setLookAndFeel(
                    UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException |
                InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        loadSettings();

        LoginClient lc = new LoginClient();
        //For the testing
        /*lc.loginPanelGUI.loginPanel.login.setText("al");
        lc.loginPanelGUI.loginPanel.password.setText("123");
        lc.loginPanelGUI.loginPanel.buttonSignIn.doClick();*/

    }

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     */
    static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = LoginClient.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * Getter for current resource bundle.
     *
     * @return current resource bundle.
     */
    static ResourceBundle getRb(){
        return rb;
    }

    /**
     * Getter for current settings.
     *
     * @return current settings.
     */
    static Settings getSettings(){
        return settings;
    }
}
