/*
 * Copyright © 2022-2023 @Frooastside
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
