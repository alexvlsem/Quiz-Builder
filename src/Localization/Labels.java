package Localization;

import java.util.ListResourceBundle;

/**
 * The Labels class for the default locale.
 *
 * @author Aleksei_Semenov 2016-09-19.
 */
public class Labels extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return new Object[][]{
                //LoginClient
                {"btSave", "Save"},
                {"lbServer", "Server: "},
                {"lbDatabase", "Database: "},
                {"lbPassword", "Password: "},
                {"lbLogin", "Login: "},
                {"btTest", "Test"},
                {"btCreate", "Create"},
                {"lbLanguage", "Language: "},
                {"lbFirstName", "First name: "},
                {"lbLastName", "Last name: "},
                {"tlConnection", "Connection"},
                {"msUserCreated", "User has been created!"},
                {"msConnectionSuccess", "Connection has been established!"},
                {"msConnectionFailed", "Impossible to connect\nwith the current parameters"},
                {"tlLogIn", "Log in"},
                {"tlCreateUser", "Create user"},
                {"tlSettings", "Settings"},
                {"btSignIn", "Sign in"},
                {"msCheckValue1", " can contain only numbers, characters, and underscores;" +
                        "\n" + "it cannot be empty or exceed "},
                {"msCheckValue2", " symbols."},
                {"msCheckValue3", "Check value"},

                //ApplicationClient
                {"btShow", "Show"},
                {"tlNewResponses", "New Responses"},
                {"tlUncompletedQuizzes", "Uncompleted Quizzes"},
                {"tlNumber", "N"},
                {"tlQuizName", "Quiz name"},
                {"tlType", "Type"},
                {"tlViewed", "Viewed"},
                {"btNew", "New"},
                {"btEdit", "Edit"},
                {"btDelete", "Delete"},
                {"tlAllUsers", "All users"},
                {"tlAssignedTo", "Assigned to"},
                {"tlDate", "Date"},
                {"tlRespondent", "Respondent"},
                {"btReport", "Report"},
                {"tlAuthor", "Author"},
                {"tlCompleted", "Completed"},
                {"btStart", "Start"},
                {"msInfo1", "Everything is done!"},
                {"msInfo2", "You have "},
                {"msInfo3", " uncompleted quiz"},
                {"msInfo4", "zes"},
                {"msInfo5", "Everything is viewed!"},
                {"msInfo6", " new answer"},
                {"msInfo7", "s"},
                {"tlChangeUser", "Change user"},
                {"tlMain", "Main"},
                {"tlYourQuizzes", "Your Quizzes"},
                {"tlResponses", "Responses"},
                {"tlAssignedQuizzes", "Assigned Quizzes"},
                {"msSelectTheQuiz", "Select the Quiz"},
                {"msCheckValue4", " cannot be empty or exceed "},
                {"msQuizIsCompleted", "Quiz is completed!"},
                {"msSelectTheResponse", "Select the Response"},
                {"msQuizCantBeRemoved", "The quiz can't be removed\nbecause it has questions."},

                //QuizEditingClient
                {"tlQuizEditing", "Quiz Editing"},
                {"tlQuestion", "Question"},
                {"tlText", "Text"},
                {"tlMultipleChoice", "Multiple Choice"},
                {"msSaveTheQuiz", "Save the Quiz first"},
                {"msSelectTheQuestion", "Select the Question"},
                {"msQuestionCantBeRemoved", "The question can't be removed\nbecause it has answers."},

                //QuestionEditingClient
                {"tlQuestionEditing", "Question Editing"},
                {"tlQuestionName", "Question name"},
                {"tlAnswer", "Answer"},
                {"tlRight", "Right"},
                {"msSaveTheQuestion", "Save the Question first"},
                {"msSelectTheRow", "Select the row"},
                {"msAnswerCantBeRemoved", "The answer can't be removed\nbecause it has been used in Quiz Responses."},

                //AnswerEditingClient
                {"tlAnswerEditing", "Answer Editing"},
                {"lbRightAnswer", "Right answer"},

                //ReportClient
                {"btSaveToTheFile", "Save to the file"},
                {"tlReport", "Report"},

                //QuizTakingClient
                {"btNext", "Next"},
                {"btFinish", "Finish"},
                {"btPrevious", "Previous"},
                {"tlAllQuestions", "All questions"},
                {"tlQuizTaking", "Quiz Taking"},

                //DataBaseConnector
                {"msUserIsNotFound", "User is not found."},
                {"msLoginIsBeenUsed", "The login has already been used,\ntry to enter another one."}
        };
    }
}
