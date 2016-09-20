import java.io.Serializable;

/**
 * @author Aleksei_Semenov 2016-09-19.
 */
public enum InterfaceLanguages implements Serializable {
    ENGLISH("English", "", ""),
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

    public String getLanguage() {
        return language;
    }

    public String getCountry() {
        return country;
    }
}