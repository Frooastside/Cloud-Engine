/*
 * Copyright © 2022-2023 @Frooastside
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
    File[] languageFiles =
        directory.listFiles(
            file -> file.exists() && file.isFile() && file.getName().endsWith(".lang"));
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
