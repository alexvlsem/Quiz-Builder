import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import javax.swing.*;

/**
 * The LoginClient class is the entrance to the program; creates a form for signing in;
 * can be opened from an instance the ApplicationClient class when changing the user.
 *
 * @author Aleksei_Semenov on 17/08/16.
 */
public class LoginClient extends JFrame {

    private LoginPanelGUI loginPanelGUI;
    private Container container;

    private class LoginPanelGUI extends JPanel {

        LoginPanel loginPanel;
        CreateUserPanel createUserPanel;

        LoginPanelGUI() {

            loginPanel = new LoginPanel();
            createUserPanel = new CreateUserPanel();

            LoginHandler handler = new LoginHandler();
            loginPanel.password.addActionListener(handler);
            loginPanel.buttonSignin.addActionListener(handler);
            createUserPanel.buttonCreateUser.addActionListener(handler);

            JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
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

            login = new JTextField("User login");
            password = new JPasswordField("User Pass");
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
        JTextField password;
        JTextField firstName;
        JTextField lastName;
        JButton buttonCreateUser;

        CreateUserPanel() {

            setLayout(new BorderLayout());
            JPanel wrapper = new JPanel();
            wrapper.setLayout(new GridLayout(4, 2));

            login = new JTextField("User login");
            password = new JPasswordField("User Pass");
            firstName = new JTextField();
            lastName = new JTextField();

            wrapper.add(new JLabel("Login (email):", JLabel.RIGHT));
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

    private class LoginHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == loginPanelGUI.loginPanel.password ||
                    e.getSource() == loginPanelGUI.loginPanel.buttonSignin) {

                String problem = checkLoginPanel();
                if (problem.length() == 0) {

                    String login = loginPanelGUI.loginPanel.login.getText();
                    String password = new String(loginPanelGUI.loginPanel.password.getPassword());

                    ResultSet result = DataBaseConnector.getUserRecord(new String[]{login, password});

                    //There will be verification
                    //Gets the first and the last name from result

                    User currentUser = new User(login, password, "John", "Smith");

                    dispose();
                    new ApplicationClient(currentUser);
                }

            } else if (e.getSource() == loginPanelGUI.createUserPanel.buttonCreateUser) {

                String problem = checkCreateUserPanel();
                if (problem.length() == 0) {

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
    }

    public LoginClient() {

        loginPanelGUI = new LoginPanelGUI();

        container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(loginPanelGUI, BorderLayout.CENTER);

        setTitle("Quiz Builder");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();

        setLocationRelativeTo(null);
        setVisible(true);
        validate();
    }

    //Start for the program
    public static void main(String[] args) {
        new LoginClient();
    }
}
