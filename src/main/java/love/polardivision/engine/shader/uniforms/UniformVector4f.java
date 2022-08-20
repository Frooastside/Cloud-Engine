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

import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;

public class UniformVector4f extends Uniform {

  private float currentX, currentY, currentZ, currentW;

  public UniformVector4f(String name) {
    super(name);
  }

  public void loadVector4f(Vector4f vector) {
    loadVector4f(vector.x, vector.y, vector.z, vector.w);
  }

  public void loadVector4f(float x, float y, float z, float w) {
    if (currentX != x || currentY != y || currentZ != z || currentW != w) {
      this.currentX = x;
      this.currentY = y;
      this.currentZ = z;
      this.currentW = w;
      GL20.glUniform4f(location(), x, y, z, w);
    }
  }

}
