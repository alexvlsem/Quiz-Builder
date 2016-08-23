import java.awt.*;
import javax.swing.*;

/**
 * The ReportClient class creates a form of the report of taking a quiz;
 * is called from an instance of the ApplicationClient class.
 *
 * @author Aleksei_Semenov 17/08/16.
 */
public class ReportClient extends JDialog {

    private ReportGUI reportGUI;
    private User respondent;
    private Quiz quiz;
    private Container container;

    class ReportGUI extends JPanel {

        JTextArea textArea;
        JButton buttonSaveToTheFile;

        ReportGUI() {

            buttonSaveToTheFile = new JButton("Save to the file");

            textArea = new JTextArea(30, 50);
            JScrollPane scrollPane = new JScrollPane(textArea);

            JPanel buttonsPanel = new JPanel();
            buttonsPanel.add(buttonSaveToTheFile);

            JPanel tablePanel = new JPanel();
            tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
            tablePanel.add(buttonsPanel);
            tablePanel.add(scrollPane);

            setLayout(new BorderLayout());
            add(new JScrollPane(tablePanel), BorderLayout.CENTER);
        }
    }

    ReportClient(ApplicationClient frame) {

        super(frame, true);

        reportGUI = new ReportGUI();

        container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(reportGUI, BorderLayout.CENTER);

        setTitle("Quiz Builder (Report)");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        pack();

        setLocationRelativeTo(null);
        setVisible(true);
        validate();
    }

    //Only to test the form
    public static void main(String[] args) {
        new ReportClient(null);
    }
}
