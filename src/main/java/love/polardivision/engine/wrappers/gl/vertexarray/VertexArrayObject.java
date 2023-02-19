/*
 * Copyright © 2023 @Frooastside
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package love.polardivision.engine.wrappers.gl.vertexarray;

import java.util.Arrays;
import java.util.Objects;
import love.polardivision.engine.wrappers.gl.GraphicalObject;
import love.polardivision.engine.wrappers.gl.vertexarray.vertexbuffer.VertexBufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class VertexArrayObject extends GraphicalObject {

  private final Primitive primitive;
  private int length;

  private final VertexBufferObject[] vertexBufferObjects = new VertexBufferObject[16];
  private VertexBufferObject indexBufferObject;

  public VertexArrayObject(Primitive primitive, int length) {
    this.primitive = primitive;
    this.length = length;
  }

  @Override
  public void initialize() {
    setIdentifier(GL30.glGenVertexArrays());
  }

  @Override
  public void bind() {
    GL30.glBindVertexArray(identifier());
  }

  @Override
  public void unbind() {
    GL30.glBindVertexArray(0);
  }

  @Override
  public void delete() {
    GL30.glDeleteVertexArrays(identifier());
    Arrays.stream(vertexBufferObjects).filter(Objects::nonNull).forEach(VertexBufferObject::delete);
  }

  public void appendVertexBufferObject(
      VertexBufferObject vertexBufferObject,
      int index,
      int valuesPerVertex,
      boolean shouldNormalize,
      int stride,
      int offset) {
    vertexBufferObject.bind();
    GL20.glVertexAttribPointer(
        index, valuesPerVertex, vertexBufferObject.dataType(), shouldNormalize, stride, offset);
    vertexBufferObject.unbind();
    vertexBufferObjects[index] = vertexBufferObject;
  }

  public void appendIndices(VertexBufferObject indexBuffer) {
    indexBuffer.bind();
    indexBufferObject = indexBuffer;
  }

  public void draw() {
    if (indexBufferObject != null) {
      GL11.glDrawElements(primitive.value(), length, indexBufferObject.dataType(), 0);
    } else {
      GL11.glDrawArrays(primitive.value(), 0, length);
    }
  }

  public void enableVertexAttributes() {
    for (int i = 0; i < vertexBufferObjects.length; i++) {
      if (vertexBufferObjects[i] != null) {
        GL20.glEnableVertexAttribArray(i);
      }
    }
  }

  public void disableVertexAttributes() {
    for (int i = 0; i < vertexBufferObjects.length; i++) {
      if (vertexBufferObjects[i] != null) {
        GL20.glDisableVertexAttribArray(i);
      }
    }
  }

  public Primitive primitive() {
    return primitive;
  }

  public VertexBufferObject vertexBufferObject(int index) {
    return vertexBufferObjects[index];
  }

  public VertexBufferObject indexBufferObject() {
    return indexBufferObject;
  }

  public int length() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }
}
