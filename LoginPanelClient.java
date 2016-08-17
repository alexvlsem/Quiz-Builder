import java.awt.*;
import javax.swing.*;

/**
 * Created by aleksei on 17/08/16.
 */
public class LoginPanelClient extends JFrame {

    private User user;

    class LoginPanelGUI extends JPanel{

        private LoginPanel loginPanel;
        private CreateUserPanel createUserPanel;

        class LoginPanel extends JPanel{
            private JTextField email;
            private JTextField password;

        }

        class CreateUserPanel extends JPanel{
            private JTextField email;
            private JTextField password;
            private JTextField firstName;
            private JTextField lastName;

        }

    }



}
