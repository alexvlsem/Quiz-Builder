import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import javax.swing.*;

/**
 * Created by aleksei on 17/08/16.
 */
public class LoginPanelClient extends JFrame {

    private LoginPanelGUI loginPanelGUI;
    private Container container;

    class LoginPanelGUI extends JPanel {

        LoginPanel loginPanel;
        CreateUserPanel createUserPanel;

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

        LoginPanelGUI() {

            loginPanel = new LoginPanel();
            createUserPanel = new CreateUserPanel();

            JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

            tabbedPane.addTab("Log in", null, loginPanel,
                    "Enter your login(email) and password to login into the programm");

            tabbedPane.addTab("Create user", null, createUserPanel,
                    "Create a new user");

            add(tabbedPane);

            LoginHandler handler = new LoginHandler(this);
            createUserPanel.buttonCreateUser.addActionListener(handler);
            loginPanel.password.addActionListener(handler);


        }
    }

    class LoginHandler implements ActionListener {

        LoginPanelGUI gui;

        LoginHandler(LoginPanelGUI gui) {
            this.gui = gui;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == gui.loginPanel.password){

                String problem = checkLoginPanel();
                if (problem.length() == 0){

                    String login = gui.loginPanel.login.getText();
                    String password = new String(gui.loginPanel.password.getPassword());

                    ResultSet result = DataBaseConnector.getUserRecord(new String[] {login, password});

                    //There will be verification
                    // Get the first and the last name from result

                    User user = new User(login,password,"John","Smith");

                    dispose();
                    new ApplicationClient(user);
                }

            }
            else if (e.getSource() == gui.createUserPanel.buttonCreateUser){

                String problem = checkCreateUserPanel();
                if (problem.length() == 0){

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

    LoginPanelClient() {

        LoginPanelGUI loginPanelGUI = new LoginPanelGUI();

        container = getContentPane();
        container.setLayout(new BorderLayout());

        container.add(loginPanelGUI, BorderLayout.CENTER);

        setTitle("Quiz Builder");

        //setSize(150,150);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pack();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginPanelClient();
    }

}
