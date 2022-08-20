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
