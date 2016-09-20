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
        };
    }
}
