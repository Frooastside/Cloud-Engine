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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class I18n {

  private static final I18n INSTANCE = new I18n();

  private final List<Language> languages = new ArrayList<>();
  private Language language;

  public static String get(String key, Object... arguments) {
    if (INSTANCE.language == null && !languages().isEmpty()) {
      setCurrentLanguage(languages().get(0));
    }
    String result;
    if (INSTANCE.language != null && INSTANCE.language.contains(key.toLowerCase())) {
      result = String.format(INSTANCE.language.get(key.toLowerCase()), arguments);
    } else {
      result = key + Arrays.toString(arguments);
    }
    return result;
  }

  public static void addLanguages(Language... languages) {
    INSTANCE.languages.addAll(Arrays.asList(languages));
  }

  public static void selectByCode(String languageCode) {
    Optional<Language> selectedLanguage =
        INSTANCE.languages.stream()
            .filter(language -> language.languageCode().equals(languageCode))
            .findFirst();
    selectedLanguage.ifPresent(I18n::setCurrentLanguage);
  }

  public static void selectByName(String languageName) {
    Optional<Language> selectedLanguage =
        INSTANCE.languages.stream()
            .filter(language -> language.languageName().equalsIgnoreCase(languageName))
            .findFirst();
    selectedLanguage.ifPresent(I18n::setCurrentLanguage);
  }

  public static List<Language> languages() {
    return INSTANCE.languages;
  }

  public static Language currentLanguage() {
    return INSTANCE.language;
  }

  public static void setCurrentLanguage(Language language) {
    if (!languages().contains(language)) {
      languages().add(language);
    }
    INSTANCE.language = language;
  }
}
