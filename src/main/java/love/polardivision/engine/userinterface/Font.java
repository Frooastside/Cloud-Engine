/*
 * Copyright © 2022-2023 @Frooastside
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package love.polardivision.engine.userinterface;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import love.polardivision.engine.wrappers.gl.texture.Texture;

public abstract class Font {

  public static final Character DEFAULT_CHARACTER = new Character(0, 0, 0, 0, 0, 0, 0, 0, 0);
  public static final int SPACE_CODEPOINT = 32;

  private final Map<Integer, Character> supportedCharacters = new HashMap<>();
  private int characterHeight;
  private Texture texture;

  public Character character(int codepoint) {
    return supportedCharacters.getOrDefault(codepoint, DEFAULT_CHARACTER);
  }

  public Map<Integer, Character> supportedCharacters() {
    return supportedCharacters;
  }

  public int characterHeight() {
    return characterHeight;
  }

  public void setCharacterHeight(int characterHeight) {
    this.characterHeight = characterHeight;
  }

  public Texture texture() {
    return texture;
  }

  public void setTexture(Texture texture) {
    this.texture = texture;
  }

  public record Character(
      double xTextureCoordinate,
      double yTextureCoordinate,
      double xMaxTextureCoordinate,
      double yMaxTextureCoordinate,
      double xOffset,
      double yOffset,
      double xSize,
      double ySize,
      double xAdvance)
      implements Serializable {}
}
