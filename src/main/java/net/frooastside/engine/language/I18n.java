package net.frooastside.engine.language;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class I18n {

  private static final I18n instance = new I18n();

  private final List<Language> languages = new ArrayList<>();
  private Language currentLanguage;

  public static String get(String key, Object... args) {
    String result = key;
    if (instance.currentLanguage.contains(key.toLowerCase())) {
      result = String.format(instance.currentLanguage.get(key.toLowerCase()), args);
    }
    return result;
  }

  public static void addLanguages(Language... languages) {
    instance.languages.addAll(Arrays.asList(languages));
  }

  public static void loadFromDirectory(File directory) {
    File[] languageFiles = directory.listFiles(file -> file.exists() && file.isFile() && file.getName().endsWith(".lang"));
    if (languageFiles != null) {
      Language[] languages = new Language[languageFiles.length];
      for (int i = 0, languageFilesLength = languageFiles.length; i < languageFilesLength; i++) {
        languages[i] = Language.createFromFile(languageFiles[i]);
      }
      addLanguages(languages);
    }
  }

  public static void selectByCode(String languageCode) {
    instance.languages.stream()
      .filter(language -> language.languageCode().equals(languageCode))
      .forEach(language -> instance.currentLanguage = language);
  }

  public static void selectByName(String languageName) {
    instance.languages.stream()
      .filter(language -> language.languageName().equalsIgnoreCase(languageName))
      .forEach(language -> instance.currentLanguage = language);
  }

  public static List<Language> languages() {
    return instance.languages;
  }

  public Language currentLanguage() {
    return currentLanguage;
  }
}
