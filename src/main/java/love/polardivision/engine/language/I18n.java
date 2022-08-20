/*
 * Copyright 2022 @Frooastside
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package love.polardivision.engine.language;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import love.polardivision.engine.utils.BufferUtils;

public class I18n {

  private static final I18n INSTANCE = new I18n();

  private final List<Language> languages = new ArrayList<>();
  private Language currentLanguage;

  public static String get(String key, Object... arguments) {
    String result = key + Arrays.toString(arguments);
    if (INSTANCE.currentLanguage == null && !languages().isEmpty()) {
      setCurrentLanguage(languages().get(0));
    }
    if (INSTANCE.currentLanguage != null && INSTANCE.currentLanguage.contains(key.toLowerCase())) {
      result = String.format(INSTANCE.currentLanguage.get(key.toLowerCase()), arguments);
    }
    return result;
  }

  public static void addLanguages(Language... languages) {
    INSTANCE.languages.addAll(Arrays.asList(languages));
  }

  public static void loadFromDirectory(File directory) {
    File[] languageFiles = directory.listFiles(file -> file.exists() && file.isFile() && file.getName().endsWith(".lang"));
    if (languageFiles != null) {
      Language[] languages = new Language[languageFiles.length];
      for (int i = 0, languageFilesLength = languageFiles.length; i < languageFilesLength; i++) {
        try {
          languages[i] = Language.createFromStream(BufferUtils.fileToStream(languageFiles[i]));
        } catch (FileNotFoundException ignored) {
        }
      }
      addLanguages(languages);
    }
  }

  public static void selectByCode(String languageCode) {
    INSTANCE.languages.stream()
      .filter(language -> language.languageCode().equals(languageCode))
      .forEach(language -> INSTANCE.currentLanguage = language);
  }

  public static void selectByName(String languageName) {
    INSTANCE.languages.stream()
      .filter(language -> language.languageName().equalsIgnoreCase(languageName))
      .forEach(language -> INSTANCE.currentLanguage = language);
  }

  public static List<Language> languages() {
    return INSTANCE.languages;
  }

  public static Language currentLanguage() {
    return INSTANCE.currentLanguage;
  }

  public static void setCurrentLanguage(Language currentLanguage) {
    INSTANCE.currentLanguage = currentLanguage;
  }
}
