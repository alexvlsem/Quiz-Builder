import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.*;

/**
 * The LoginClient class is the entrance to the program; creates a form for signing in;
 * can be opened from an instance of the ApplicationClient class when changing the user.
 *
 * @author Aleksei_Semenov on 17/08/16.
 */
public class LoginClient extends JFrame {

    private LoginPanelGUI loginPanelGUI;
    private Container container;
    private JTabbedPane tabbedPane;

    private class LoginPanelGUI extends JPanel {

        LoginPanel loginPanel;
        CreateUserPanel createUserPanel;
        LoginHandler handler;

        LoginPanelGUI() {

            loginPanel = new LoginPanel();
            createUserPanel = new CreateUserPanel();

            handler = new LoginHandler();
            loginPanel.password.addActionListener(handler);
            loginPanel.buttonSignin.addActionListener(handler);
            createUserPanel.buttonCreateUser.addActionListener(handler);

            tabbedPane = new JTabbedPane(JTabbedPane.TOP);
            tabbedPane.addTab("Log in", loginPanel);
            tabbedPane.addTab("Create user", createUserPanel);

            add(tabbedPane);
        }
    }


    private class LoginPanel extends JPanel {
        JTextField login;
        JPasswordField password;
        JButton buttonSignin;

        LoginPanel() {

            login = new JTextField("a");
            password = new JPasswordField();
            buttonSignin = new JButton("Sign in");

            JPanel wrapper = new JPanel();
            wrapper.setLayout(new GridLayout(4, 2));
            wrapper.add(new JLabel("", JLabel.RIGHT));
            wrapper.add(new JLabel(""));
            wrapper.add(new JLabel("Login:", JLabel.RIGHT));
            wrapper.add(login);
            wrapper.add(new JLabel("Password:", JLabel.RIGHT));
            wrapper.add(password);
            wrapper.add(new JLabel(""));
            wrapper.add(buttonSignin);
            add(wrapper);
        }
    }

    private class CreateUserPanel extends JPanel {
        JTextField login;
        JPasswordField password;
        JTextField firstName;
        JTextField lastName;
        JButton buttonCreateUser;

        CreateUserPanel() {

            setLayout(new BorderLayout());
            JPanel wrapper = new JPanel();
            wrapper.setLayout(new GridLayout(4, 2));

            login = new JTextField();
            password = new JPasswordField();
            firstName = new JTextField();
            lastName = new JTextField();

            wrapper.add(new JLabel("Login :", JLabel.RIGHT));
            wrapper.add(login);
            wrapper.add(new JLabel("First name:", JLabel.RIGHT));
            wrapper.add(firstName);
            wrapper.add(new JLabel("Last name:", JLabel.RIGHT));
            wrapper.add(lastName);
            wrapper.add(new JLabel("Password:", JLabel.RIGHT));
            wrapper.add(password);

            add(wrapper, BorderLayout.NORTH);
            add(buttonCreateUser = new JButton("Create"), BorderLayout.SOUTH);
        }
    }

    private class LoginHandler extends WindowAdapter implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == loginPanelGUI.loginPanel.password ||
                    e.getSource() == loginPanelGUI.loginPanel.buttonSignin) {

                String problem = checkLoginPanel();
                if (problem.length() == 0) {

                    String login = loginPanelGUI.loginPanel.login.getText();
                    String password = new String(loginPanelGUI.loginPanel.password.getPassword());

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

                String problem = checkCreateUserPanel();
                if (problem.length() == 0) {
                    ArrayList<String> userData = DataBaseConnector.createUser(
                            new String[]{login, firstName, lastName, password});
                    if (userData.size() == 1) {
                        JOptionPane.showMessageDialog(loginPanelGUI, userData.get(0));
                    } else {
                        JOptionPane.showMessageDialog(loginPanelGUI, "User has been created!");
                        loginPanelGUI.loginPanel.login.setText(login);
                        tabbedPane.setSelectedIndex(0);
                    }
                }
            }
        }

        String checkLoginPanel() {

            String problem = "";

            return problem;
        }

        String checkCreateUserPanel() {

            String problem = "";

            return problem;
        }

        public void windowClosing(WindowEvent e) {
            DataBaseConnector.closeConnection();
        }

    }

    public LoginClient() {

        loginPanelGUI = new LoginPanelGUI();

        container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(loginPanelGUI, BorderLayout.CENTER);

        setTitle("Quiz Builder");
        addWindowListener(loginPanelGUI.handler);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();

        setLocationRelativeTo(null);
        setVisible(true);
        validate();

        DataBaseConnector.createConnection();
    }

    //Start for the program
    public static void main(String[] args) {
        new LoginClient();
    }
}
