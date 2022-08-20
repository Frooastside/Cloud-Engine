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
