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

public record ColorSet(
    Color background,
    Color baseElement,
    Color element,
    Color text,
    Color accent,
    Color negativeAccent) {

  public static final ColorSet DARK_MODE =
      new ColorSet(
          new Color(0.2f, 0.2f, 0.2f),
          new Color(0.24f, 0.24f, 0.24f),
          new Color(0.28f, 0.28f, 0.28f),
          new Color(1.0f, 1.0f, 1.0f),
          new Color(0.1215f, 0.5529f, 0.1215f),
          new Color(0.7f, 0.02f, 0.02f));
}
