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

package love.polardivision.engine.postprocessing;

import love.polardivision.engine.glwrapper.DataType;
import love.polardivision.engine.glwrapper.vertexarray.Primitive;
import love.polardivision.engine.glwrapper.vertexarray.VertexArrayObject;
import love.polardivision.engine.glwrapper.vertexarray.vertexbuffer.BufferTarget;
import love.polardivision.engine.glwrapper.vertexarray.vertexbuffer.BufferUsage;
import love.polardivision.engine.glwrapper.vertexarray.vertexbuffer.VertexBufferObject;
import net.frooastside.engine.resource.BufferUtils;
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
