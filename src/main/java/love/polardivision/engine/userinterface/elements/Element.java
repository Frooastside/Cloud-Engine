/*
 * Copyright © 2022-2023 @Frooastside
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package love.polardivision.engine.userinterface.elements;

import love.polardivision.engine.userinterface.Color;
import org.joml.Vector2f;
import org.joml.Vector4f;

public abstract class Element {

  private final Vector4f bounds = new Vector4f(0, 0, 1, 1);
  private final Vector2f pixelSize = new Vector2f();

  private Color color;
  private float alpha = 1.0f;
  private boolean visible;

  public abstract void update(double delta);

  public void updatePixelSize(Vector2f pixelSize) {
    this.pixelSize.set(pixelSize);
  }

  public void display(boolean show, float parentDelay) {
    if (show != visible) {
      this.visible = show;
    }
  }

  public Vector2f relativePixelPosition(float x, float y) {
    float rawX = x * pixelSize.x;
    float rawY = y * pixelSize.y;
    Vector4f bounds = bounds();
    return new Vector2f(
      Math.max(Math.min((rawX - bounds.x) / bounds.z, 1), 0),
      Math.max(Math.min((rawY - bounds.y) / bounds.w, 1), 0));
  }

  public boolean isPixelInside(float x, float y) {
    float rawX = x * pixelSize.x;
    float rawY = y * pixelSize.y;
    return isInside(rawX, rawY);
  }

  public boolean isInside(float rawX, float rawY) {
    Vector4f bounds = bounds();
    float xMin = bounds.x;
    float yMin = bounds.y;
    float xMax = xMin + bounds.z;
    float yMax = yMin + bounds.w;
    return rawX <= xMax && rawX >= xMin && rawY <= yMax && rawY >= yMin;
  }

  public Vector4f bounds() {
    return bounds;
  }

  public Vector2f pixelSize() {
    return pixelSize;
  }

  public float alpha() {
    return alpha;
  }

  public void setAlpha(float alpha) {
    this.alpha = alpha;
  }

  public boolean visible() {
    return visible;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  public Color color() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

}
