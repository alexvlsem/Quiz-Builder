import javax.swing.JFrame;

/**
 * Created by aleksei on 17/08/16.
 */
public class TestEditingClient extends JFrame{

    private TestEditingGUI testEditingGUI;
    private Test test;

    public TestEditingClient(User owner, Test test){
        this.test = test;
    }

    public class TestEditingGUI extends JFrame{
    }

}
