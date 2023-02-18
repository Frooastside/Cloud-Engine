/*
 * Copyright © 2023 @Frooastside
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package love.polardivision.engine.wrappers.gl.vertexarray.vertexbuffer;

import org.lwjgl.opengl.GL15;

public enum BufferUsage {

  STREAM_DRAW(GL15.GL_STREAM_DRAW),
  STREAM_READ(GL15.GL_STREAM_READ),
  STREAM_COPY(GL15.GL_STREAM_COPY),
  STATIC_DRAW(GL15.GL_STATIC_DRAW),
  STATIC_READ(GL15.GL_STATIC_READ),
  STATIC_COPY(GL15.GL_STATIC_COPY),
  DYNAMIC_DRAW(GL15.GL_DYNAMIC_DRAW),
  DYNAMIC_READ(GL15.GL_DYNAMIC_READ),
  DYNAMIC_COPY(GL15.GL_DYNAMIC_COPY);

  private final int value;

  BufferUsage(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }
}
