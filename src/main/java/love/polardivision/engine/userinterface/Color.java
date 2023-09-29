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

import org.joml.Vector3f;
import org.joml.Vector4f;

public class Color {

  private final Vector4f rawColor;

  public Color(Vector4f rawColor) {
    this.rawColor = rawColor;
  }

  public Color(float r, float g, float b, float a) {
    this.rawColor = new Vector4f(r, g, b, a);
  }

  public Color(Vector3f rawColor) {
    this.rawColor = new Vector4f(rawColor, 1.0f);
  }

  public Color(float r, float g, float b) {
    this.rawColor = new Vector4f(r, g, b, 1.0f);
  }

  public Vector4f rawColor() {
    return rawColor;
  }
}
