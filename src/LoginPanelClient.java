import java.awt.*;
import javax.swing.*;

/**
 * Created by aleksei on 17/08/16.
 */
public class LoginPanelClient extends JFrame {

    private User user;
    private LoginPanelGUI loginPanelGUI;
    private Container container;

    class LoginPanelGUI extends JPanel{

        private LoginPanel loginPanel;
        private CreateUserPanel createUserPanel;

        class LoginPanel extends JPanel{
            private JTextField email;
            private JPasswordField password;

            LoginPanel(){

                setLayout(new GridLayout(2,2));

                email = new JTextField(30);
                password = new JPasswordField(10);

                add(new JLabel("Login:"));
                add(email);
                add(new Label("Password:"));
                add(password);
            }

        }

        class CreateUserPanel extends JPanel{
            private JTextField email;
            private JTextField password;
            private JTextField firstName;
            private JTextField lastName;

            CreateUserPanel(){

                setLayout(new GridLayout(4,2));

                email = new JTextField(30);
                password = new JPasswordField(10);
                firstName = new JTextField(30);
                lastName = new JTextField(30);


                add(new JLabel("email:"));
                add(email);
                add(new JLabel("First name:"));
                add(firstName);
                add(new JLabel("Last name:"));
                add(lastName);
                add(new Label("password:"));
                add(password);
            }
        }

        LoginPanelGUI(){

            LoginPanel loginPanel = new LoginPanel();
            CreateUserPanel createUserPanel = new CreateUserPanel();

            JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

            tabbedPane.addTab("Log in",null, loginPanel,
                    "Enter your login(email) and password to login into the programm");

            tabbedPane.addTab("Create user",null, createUserPanel,
                    "Create a new user");

        }


    }

    LoginPanelClient(){

        LoginPanelGUI loginPanelGUI = new LoginPanelGUI();

        container = getContentPane();
        container.setLayout(new BorderLayout());

        container.add(loginPanelGUI, BorderLayout.CENTER);

        setTitle("Application");

        setSize(150,150);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //pack();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginPanelClient();
    }

}
