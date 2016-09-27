import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLWriter;

/**
 * The ReportClient class creates a form of the report of taking a quiz;
 * is called from an instance of the ApplicationClient class.
 *
 * @author Aleksei_Semenov 17/08/16.
 */
class ReportClient extends JDialog {

    private ResourceBundle rb = LoginClient.getRb();
    private ReportGUI reportGUI;
    private User respondent;
    private Quiz quiz;

    /**
     * The inner ReportGUI class creates the report panel.
     */
    private class ReportGUI extends JPanel {

        JButton buttonSaveToTheFile;
        JEditorPane editorPane;

        private ReportGUI() {

            buttonSaveToTheFile = new JButton(rb.getString("btSaveToTheFile"),
                    LoginClient.createImageIcon("images/Save16.gif"));

            editorPane = new JEditorPane();
            editorPane.setEditable(false);

            String htmlPage = "";
            if (quiz.getType().equals(QuizTypes.TEST)) {
                htmlPage = getTestReport();
            } else if (quiz.getType().equals(QuizTypes.POLL)) {
                htmlPage = getPollReport();
            }

            HTMLEditorKit kit = new HTMLEditorKit();
            editorPane.setEditorKit(kit);
            Document doc = kit.createDefaultDocument();
            editorPane.setDocument(doc);
            editorPane.setText(htmlPage);

            JScrollPane scrollPane = new JScrollPane(editorPane);
            scrollPane.setPreferredSize(new Dimension(550, 400));

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

    private String getTestReport() {

        Vector rows = DataBaseConnector.getQuizResults(respondent, quiz);

        String html = "<html>" +
                "<header>" +
                "<style>" +
                "body {color:#000; font-family: Verdana, times; margin: 4px; font-size:100%;}" +
                "h1 {color: blue;}" +
                "h3 {margin-bottom: 0px;}" +
                "#wrapper{width: 500px; margin: 0px auto;}" +
                ".mkGreen{color:green; font-weight:bold;}" +
                ".mkRed{color:red; font-weight:bold;}" +
                "</style>" +
                "</header>" +
                "<body>" +
                "<div id=\"wrapper\">" +
                "<h1>Quiz submissions: " + quiz.getName() + "</h1>" +
                "<h2>Respondent: " + respondent + "</h2>";

        int totalQuestions = 0;
        int correctQuestions = 0;
        boolean answersAreCorrect = false;
        Question currQuestion = null;

        for (Object obj : rows) {

            Vector row = (Vector) obj;

            if (!row.get(0).equals(currQuestion)) {
                currQuestion = (Question) row.get(0);
                html += "<h3>" + currQuestion + "</h3>" +
                        "<p>" + currQuestion.getText().replaceAll("\\n", "<br>") + "</p><br>";
                totalQuestions++;
                if (answersAreCorrect) {
                    correctQuestions++;
                }
                answersAreCorrect = true;
            }

            Answer currAnswer = (Answer) row.get(1);
            boolean isSelected = (boolean) row.get(2);

            if (answersAreCorrect) {
                answersAreCorrect = currAnswer.getCorrectness() == isSelected;
            }

            html += "&#8195;";//space
            if (currAnswer.getCorrectness()) {
                html += "&#8680;"; //a right arrow
            } else {
                html += "&#8195;";
            }

            if (isSelected) {
                if (currAnswer.getCorrectness()) {
                    html += " <span class=\"mkGreen\">V</span>";
                } else {
                    html += " <span class=\"mkRed\">X</span>";
                }
            } else {
                html += "&#8195;";
            }
            html += " " + currAnswer.getText().replaceAll("\\n", "<br>") + "<br>";
        }

        if (answersAreCorrect) {
            correctQuestions++;
        }

        double percent = 0.0;
        if (totalQuestions != 0) {
            percent = Math.round((double) correctQuestions / (double) totalQuestions * 100.0);
        }
        html += "<br><h3>Total: " + correctQuestions + " / " + totalQuestions + " - " + percent + "%</h3>" +
                "</div>" +
                "</body>" +
                "</html>";
        return html;
    }

    private String getPollReport() {

        Vector rows = DataBaseConnector.getQuizResults(respondent, quiz);

        String html = "<html>" +
                "<header>" +
                "<style>" +
                "body {color:#000; font-family: Verdana, times; margin: 4px; }" +
                "h1 {color: blue;}" +
                "h3 {margin-bottom: 0px;}" +
                "#wrapper{width: 400px; margin: 0px auto;}" +
                ".mkGreen{color:green; font-weight:bold;}" +
                ".mkRed{color:red; font-weight:bold;}" +
                "</style>" +
                "</header>" +
                "<body>" +
                "<div id=\"wrapper\">" +
                "<h1>Quiz submissions: " + quiz.getName() + "</h1>" +
                "<h2>Respondent: " + respondent + "</h2>";

        Question currQuestion = null;

        for (Object obj : rows) {

            Vector row = (Vector) obj;

            if (!row.get(0).equals(currQuestion)) {
                currQuestion = (Question) row.get(0);
                html += "<h3>" + currQuestion + "</h3>" +
                        "<p>" + currQuestion.getText().replaceAll("\\n", "<br>") + "</p><br>";
            }

            Answer currAnswer = (Answer) row.get(1);
            boolean isSelected = (boolean) row.get(2);

            if (isSelected) {
                html += "&#8195; " + currAnswer.getText().replaceAll("\\n", "<br>") + "<br>";
            }
        }

        html += "</div>" +
                "</body>" +
                "</html>";

        return html;
    }

    ReportClient(ApplicationClient frame, User respondent, Quiz quiz) {

        super(frame, true);
        this.respondent = respondent;
        this.quiz = quiz;

        reportGUI = new ReportGUI();

        ReportHandler handler = new ReportHandler();
        reportGUI.buttonSaveToTheFile.addActionListener(handler);

        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(reportGUI, BorderLayout.CENTER);

        setTitle("Quiz Builder (" + rb.getString("tlReport") + ")");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        pack();

        setLocationRelativeTo(null);
        setVisible(true);
        validate();
    }

    /**
     * The ReportHandler class saves the report to the file.
     */
    private class ReportHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource().equals(reportGUI.buttonSaveToTheFile)) {

                String fileName = (quiz + "_" + respondent + ".html").replaceAll(" ", "_");
                File file;

                //Create a file chooser
                JFileChooser fc = new JFileChooser();
                fc.setSelectedFile(new File(System.getProperty("user.home"), fileName));

                int returnVal = fc.showSaveDialog(ReportClient.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    file = fc.getSelectedFile();
                } else {
                    return;
                }

                try {
                    BufferedWriter out = new BufferedWriter(new FileWriter(file));

                    HTMLWriter hw = new HTMLWriter(out, (HTMLDocument) reportGUI.editorPane.getDocument());
                    hw.write();

                    out.flush();
                    out.close();
                } catch (IOException | BadLocationException exp) {
                    exp.printStackTrace();
                }
            }
        }
    }
}
