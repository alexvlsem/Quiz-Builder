import java.io.Serializable;

/**
 * The InterfaceLanguages enum represents interface languages of the program.
 * Can be expanded as much as needed.
 *
 * @author Aleksei_Semenov 2016-09-19.
 */
public enum InterfaceLanguages implements Serializable {
    ENGLISH("English", "", ""),
    FRENCH("Français", "fr", "CA"),
    RUSSIAN("Русский", "ru", "RU");

    String name, language, country;

    InterfaceLanguages(String name, String language, String country) {
        this.name = name;
        this.language = language;
        this.country = country;
    }

    @Override
    public String toString() {
        return name;
    }

    //Getters for creating locales
    public String getLanguage() {
        return language;
    }

    public String getCountry() {
        return country;
    }
}