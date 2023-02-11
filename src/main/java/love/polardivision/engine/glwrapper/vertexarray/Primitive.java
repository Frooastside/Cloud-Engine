/*
 * Copyright © 2022-2023 @Frooastside
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package love.polardivision.engine.glwrapper.vertexarray;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;

public enum Primitive {

  POINTS(GL11.GL_POINTS),
  LINE_STRIP(GL11.GL_LINE_STRIP),
  LINE_LOOP(GL11.GL_LINE_LOOP),
  LINES(GL11.GL_LINES),
  LINE_STRIP_ADJACENCY(GL32.GL_LINE_STRIP_ADJACENCY),
  LINES_ADJACENCY(GL32.GL_LINES_ADJACENCY),
  TRIANGLE_STRIP(GL11.GL_TRIANGLE_STRIP),
  TRIANGLE_FAN(GL11.GL_TRIANGLE_FAN),
  TRIANGLES(GL11.GL_TRIANGLES),
  TRIANGLE_STRIP_ADJACENCY(GL32.GL_TRIANGLE_STRIP_ADJACENCY),
  TRIANGLES_ADJACENCY(GL32.GL_TRIANGLES_ADJACENCY),
  PATCHES(GL40.GL_PATCHES);

  private final int value;

  Primitive(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }
}
