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

import org.joml.Vector3f;
import org.lwjgl.opengl.GL20;

public class UniformVector3f extends Uniform {

  private float currentX, currentY, currentZ;

  public UniformVector3f(String name) {
    super(name);
  }

  public void loadVector3f(Vector3f vector) {
    loadVector3f(vector.x, vector.y, vector.z);
  }

  public void loadVector3f(float x, float y, float z) {
    if (currentX != x || currentY != y || currentZ != z) {
      this.currentX = x;
      this.currentY = y;
      this.currentZ = z;
      GL20.glUniform3f(location(), x, y, z);
    }
  }

}
