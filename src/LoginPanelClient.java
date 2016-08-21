import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
            JTextField login;
            JPasswordField password;

            LoginPanel(){

                JPanel wrapper = new JPanel();
                wrapper.setLayout(new GridLayout(2,2));

                login = new JTextField(10);
                password = new JPasswordField(10);

                wrapper.add(new JLabel("Login:",JLabel.RIGHT));
                wrapper.add(login);
                wrapper.add(new JLabel("Password:",JLabel.RIGHT));
                wrapper.add(password);

                add(wrapper,BorderLayout.CENTER);
            }
        }

        class CreateUserPanel extends JPanel{
            JTextField login;
            JTextField password;
            JTextField firstName;
            JTextField lastName;
            JButton buttonCreateUser;

            CreateUserPanel(){

                setLayout(new BorderLayout());
                JPanel wrapper = new JPanel();
                wrapper.setLayout(new GridLayout(4,2));

                login = new JTextField(10);
                password = new JPasswordField();
                firstName = new JTextField();
                lastName = new JTextField();

                wrapper.add(new JLabel("Login (email):",JLabel.RIGHT));
                wrapper.add(login);
                wrapper.add(new JLabel("First name:", JLabel.RIGHT));
                wrapper.add(firstName);
                wrapper.add(new JLabel("Last name:",JLabel.RIGHT));
                wrapper.add(lastName);
                wrapper.add(new JLabel("Password:",JLabel.RIGHT));
                wrapper.add(password);

                add(wrapper,BorderLayout.NORTH);
                add(buttonCreateUser = new JButton("Create"),BorderLayout.SOUTH);
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

            add(tabbedPane);
        }
    }

    class LoginHandler implements ActionListener{

        LoginPanelGUI gui;

        LoginHandler(LoginPanelGUI gui){
            this.gui = gui;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == gui.loginPanel.password){

            }
            else if (e.getSource() == gui.createUserPanel.buttonCreateUser){

            }
        }




    }

    LoginPanelClient(){

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
