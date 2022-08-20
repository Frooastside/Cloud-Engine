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

package love.polardivision.engine.shader.uniforms;

import org.joml.Vector2f;
import org.lwjgl.opengl.GL20;

public class UniformVector2f extends Uniform {

  private float currentX, currentY;

  public UniformVector2f(String name) {
    super(name);
  }

  public void loadVector2f(Vector2f vector) {
    loadVector2f(vector.x, vector.y);
  }

  public void loadVector2f(float x, float y) {
    if (currentX != x || currentY != y) {
      this.currentX = x;
      this.currentY = y;
      GL20.glUniform2f(location(), x, y);
    }
  }

}
