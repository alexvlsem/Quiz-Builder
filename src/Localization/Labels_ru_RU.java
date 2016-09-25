package Localization;

import java.util.ListResourceBundle;

/**
 * @author Aleksei_Semenov 2016-09-19.
 */
public class Labels_ru_RU extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return new Object[][]{
                //LoginClient
                {"btSave", "Сохранить"},
                {"lbServer", "Сервер: "},
                {"lbDatabase", "База данных: "},
                {"lbPassword", "Пароль: "},
                {"lbLogin", "Логин: "},
                {"btTest", "Тест"},
                {"btCreate", "Создать"},
                {"lbLanguage","Язык: "},
                {"lbFirstName","Имя: "},
                {"lbLastName","Фамилия: "},
                {"tlConnection","Соединение"},
                {"msUserCreated","Пользователь создан!"},
                {"msConnectionSuccess","Соединение установлено!"},
                {"msConnectionFailed","Невозможно соединиться\nс текущими параметрами."},
                {"tlLogIn","Авторизация"},
                {"tlCreateUser","Регистрация"},
                {"tlSettings","Настройки"},
                {"btSignIn","Войти"},
                {"msCheckValue1"," может содержать только цифры буквы и нижние подчеркивания;\n" + "не может быть пустым или превышать " },
                {"msCheckValue2"," символов."},
                {"msCheckValue3","Проверьте значение"},

                //Application client
                {"btShow","Показать"},
                {"tlNewResponses","Новые ответы"},
                {"tlUncompletedQuizzes","Невыполненные квизы"},
                {"tlQuizName","Наименование квиза"},
                {"tlType","Тип"},
                {"btNew","Новый"},
                {"btEdit","Изменить"},
                {"btDelete","Удалить"},
                {"tlAllUsers","Все пользователи"},
                {"tlAssignedTo","Назначено"},
                {"tlDate","Дата"},
                {"tlRespondent","Респондент"},
                {"btReport","Отчет"},
                {"tlAuthor","Автор"},
                {"tlCompleted","Выполнен"},
                {"btStart","Начать"},
                {"msInfo1","Все выполненно!"},
                {"msInfo2","У вас "},
                {"msInfo3"," невыполненных квизов"},
                {"msInfo4",""},
                {"tlChangeUser","Сменить пользователя"},
                {"tlMain","Основное"},
                {"tlYourQuizzes","Ваши Квизы"},
                {"tlResponses","Ответы"},
                {"tlAssignedQuizzes","Назначенные Квизы"},
                {"msSelectTheQuiz","Выберите квиз"},
                {"msCheckValue4"," не может быть пустым или превышать "},
                {"msQuizIsCompleted","Квиз завершен!"},
                {"msSelectTheResponse","Выберите ответ"},

                //QuizEditingClient
                {"tlQuizEditing","Редактирование квиза"},
                {"tlQuestion","Вопрос"},
                {"tlText","Текст"},
                {"tlMultipleChoice","Множественный выбор"},
                {"msSaveTheQuiz","Сначала сохраните квиз"},
                {"msSelectTheQuestion","Выберите вопрос"},

                //QuestionEditingClient
                {"tlQuestionEditing","Редактирование Вопроса"},
                {"tlQuestionName","Вопрос"},
                {"tlAnswer","Ответ"},
                {"tlRight","Правильный"},
                {"msSaveTheQuestion","Сначала сохраните вопрос"},
                {"msSelectTheRow","Выберите ряд"},

                //AnswerEditingClient
                {"tlAnswerEditing","Редактирование Ответа"},
                {"lbRightAnswer","Правильный ответ"},

                //ReportClient
                {"btSaveToTheFile","Сохранить в файл"},
                {"tlReport","Отчет"},

                //QuizTakingClient
                {"btNext","Следующий"},
                {"btFinish","Закончить"},
                {"btPrevious","Предыдущий"},
                {"tlAllQuestions","Все вопросы"},
                {"tlQuizTaking","Прохождение Квиза"},
        };
    }
}
