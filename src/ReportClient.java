import java.awt.*;
import javax.swing.*;

/**
 * Created by aleksei on 17/08/16.
 */
public class ReportClient extends JFrame {

    private ReportGUI reportGUI;
    private User respondent;
    private Quiz quiz;
    private Container container;

    class ReportGUI extends JPanel {

        JTextArea textArea;
        JButton buttonSaveToTheFile;

        ReportGUI(){

            buttonSaveToTheFile = new JButton("Save to the file");

            textArea = new JTextArea(30,50);
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

    ReportClient(){

        reportGUI = new ReportGUI();

        container = getContentPane();
        container.setLayout(new BorderLayout());

        container.add(reportGUI, BorderLayout.CENTER);

        setTitle("Quiz Builder");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        pack();

        setLocationRelativeTo(null);
        setVisible(true);
        //validate();
    }

    //Only for tests
    public static void main(String[] args) {
      new ReportClient();
    }

}
