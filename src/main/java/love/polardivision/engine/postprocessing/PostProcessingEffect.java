/*
 * Copyright © 2022-2023 @Frooastside
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package love.polardivision.engine.postprocessing;

import love.polardivision.engine.utils.BufferUtils;
import love.polardivision.engine.wrappers.gl.DataType;
import love.polardivision.engine.wrappers.gl.vertexarray.Primitive;
import love.polardivision.engine.wrappers.gl.vertexarray.VertexArrayObject;
import love.polardivision.engine.wrappers.gl.vertexarray.vertexbuffer.BufferTarget;
import love.polardivision.engine.wrappers.gl.vertexarray.vertexbuffer.BufferUsage;
import love.polardivision.engine.wrappers.gl.vertexarray.vertexbuffer.VertexBufferObject;
import org.lwjgl.opengl.GL11;

public abstract class PostProcessingEffect {

  private static final float[] FULLSCREEN_QUAD_POSITIONS = new float[]{
    -1, 1,
    -1, -1,
    1, 1,
    1, -1};

  private final VertexArrayObject fullscreenQuad;

  {
    fullscreenQuad = new VertexArrayObject(Primitive.TRIANGLE_STRIP, FULLSCREEN_QUAD_POSITIONS.length / 2);
    fullscreenQuad.initialize();
    fullscreenQuad.bind();

    VertexBufferObject positionBuffer = new VertexBufferObject(DataType.FLOAT, BufferTarget.ARRAY_BUFFER, BufferUsage.STATIC_DRAW);
    positionBuffer.initialize();
    positionBuffer.storeFloatData(BufferUtils.store(FULLSCREEN_QUAD_POSITIONS));
    fullscreenQuad.appendVertexBufferObject(positionBuffer, 0, 2, false, 0, 0);

    fullscreenQuad.unbind();
  }

  protected void bind() {
    fullscreenQuad.bind();
    fullscreenQuad.enableVertexAttributes();
    GL11.glDisable(GL11.GL_DEPTH_TEST);
  }

  protected void draw() {
    fullscreenQuad.draw();
  }

  protected void unbind() {
    GL11.glEnable(GL11.GL_DEPTH_TEST);
    fullscreenQuad.disableVertexAttributes();
    fullscreenQuad.unbind();
  }

  public abstract void delete();

  protected VertexArrayObject fullscreenQuad() {
    return fullscreenQuad;
  }
}
