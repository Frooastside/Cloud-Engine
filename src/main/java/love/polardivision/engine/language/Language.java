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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public record Language(String languageCode, String languageName,
                       Map<String, String> translatedStrings) {

  public static Language createFromStream(InputStream inputStream) {
    Map<String, String> translatedStrings = new HashMap<>();
    String languageCode = "[]_[]";
    String languageName = "Invalid Language";
    try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
      while (bufferedReader.ready()) {
        String line = bufferedReader.readLine();
        if (line.contains("=") && !line.strip().startsWith("#")) {
          String key = line.substring(0, line.indexOf("="));
          String value = line.substring(line.indexOf("=") + 1);
          if (key.equals("language.code")) {
            languageCode = value;
          } else if (key.equals("language.name")) {
            languageName = value;
          } else {
            translatedStrings.put(key, value);
          }
        }
      }
    } catch (IOException ignored) {
    }
    return new Language(languageCode, languageName, translatedStrings);
  }

  public boolean contains(String key) {
    return translatedStrings.containsKey(key);
  }

  public String get(String key) {
    return translatedStrings.get(key);
  }
}
