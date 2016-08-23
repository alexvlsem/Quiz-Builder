import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import javax.swing.*;

/**
 * Created by aleksei on 17/08/16.
 */
public class LoginClient extends JFrame {

    private LoginPanelGUI loginPanelGUI;
    private Container container;

    class LoginPanelGUI extends JPanel {

        LoginPanel loginPanel;
        CreateUserPanel createUserPanel;
        JButton buttonTest;

        LoginPanelGUI() {

            loginPanel = new LoginPanel();
            createUserPanel = new CreateUserPanel();

            LoginHandler handler = new LoginHandler();
            loginPanel.password.addActionListener(handler);
            createUserPanel.buttonCreateUser.addActionListener(handler);


            JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

            tabbedPane.addTab("Log in", loginPanel);
            tabbedPane.addTab("Create user", createUserPanel);

            add(tabbedPane);

        }
    }


    class LoginPanel extends JPanel {
        JTextField login;
        JPasswordField password;

        LoginPanel() {

            JPanel wrapper = new JPanel();
            wrapper.setLayout(new GridLayout(2, 2));

            login = new JTextField("User login");
            password = new JPasswordField("User Pass");

            wrapper.add(new JLabel("Login:", JLabel.RIGHT));
            wrapper.add(login);
            wrapper.add(new JLabel("Password:", JLabel.RIGHT));
            wrapper.add(password);

            add(wrapper, BorderLayout.CENTER);
        }
    }

    class CreateUserPanel extends JPanel {
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

    class LoginHandler implements ActionListener {


        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == loginPanelGUI.loginPanel.password) {

                String problem = checkLoginPanel();
                if (problem.length() == 0) {

                    String login = loginPanelGUI.loginPanel.login.getText();
                    String password = new String(loginPanelGUI.loginPanel.password.getPassword());

                    ResultSet result = DataBaseConnector.getUserRecord(new String[]{login, password});

                    //There will be verification
                    // Get the first and the last name from result

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

    LoginClient() {

        loginPanelGUI = new LoginPanelGUI();

        container = getContentPane();
        container.setLayout(new BorderLayout());

        container.add(loginPanelGUI, BorderLayout.CENTER);


        setTitle("Quiz Builder");

        //setSize(150,150);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pack();

        setLocationRelativeTo(null);
        setVisible(true);
        validate();
    }

    public static void main(String[] args) {
        new LoginClient();
    }

}
