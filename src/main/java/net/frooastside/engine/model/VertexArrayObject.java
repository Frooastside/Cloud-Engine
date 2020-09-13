package net.frooastside.engine.model;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.Arrays;
import java.util.Objects;

public class VertexArrayObject {

  private final int identifier;
  private int length;

  private final VertexBufferObject[] vertexBufferObjects = new VertexBufferObject[16];
  private VertexBufferObject indexBufferObject;


  public VertexArrayObject(int identifier, int length) {
    this.identifier = identifier;
    this.length = length;
  }

  public void appendVertexBufferObject(VertexBufferObject vertexBufferObject, int index, int valuesPerVertex, boolean shouldNormalize, int stride, int offset) {
    vertexBufferObject.bind();
    GL20.glVertexAttribPointer(index, valuesPerVertex, vertexBufferObject.dataType(), shouldNormalize, stride, offset);
    vertexBufferObject.unbind();
    vertexBufferObjects[index] = vertexBufferObject;
  }

  public void appendIndices(VertexBufferObject indexBuffer) {
    indexBuffer.bind();
    indexBufferObject = indexBuffer;
  }

  public void draw() {
    if (indexBufferObject != null) {
      GL11.glDrawElements(GL11.GL_TRIANGLES, length, indexBufferObject.dataType(), 0);
    } else {
      GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, length);
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

  public void bind() {
    GL30.glBindVertexArray(identifier);
  }

  public void unbind() {
    GL30.glBindVertexArray(0);
  }

  public void delete() {
    GL30.glDeleteVertexArrays(identifier);
    Arrays.stream(vertexBufferObjects).filter(Objects::nonNull).forEach(VertexBufferObject::delete);
  }

  public static int generateIdentifier() {
    return GL30.glGenVertexArrays();
  }

  public VertexBufferObject getVertexBufferObject(int index) {
    return vertexBufferObjects[index];
  }

  public void setLength(int length) {
    this.length = length;
  }

  public int length() {
    return length;
  }

  public static VertexArrayObject create2DFor(float[] positions) {
    VertexArrayObject vertexArrayObject = new VertexArrayObject(generateIdentifier(), positions.length / 2);
    vertexArrayObject.bind();
    VertexBufferObject positionBuffer = VertexBufferObject.createVertexBufferObject(BufferDataType.FLOAT, BufferTarget.ARRAY_BUFFER, BufferUsage.STATIC_DRAW);
    positionBuffer.storeFloatData(VertexBufferUtils.store(positions));
    vertexArrayObject.appendVertexBufferObject(positionBuffer, 0, 2, false, 0, 0);
    vertexArrayObject.unbind();
    return vertexArrayObject;
  }

  public static VertexArrayObject create2DIFor(float[] positions, short[] indices) {
    VertexArrayObject vertexArrayObject = new VertexArrayObject(generateIdentifier(), indices.length);
    vertexArrayObject.bind();

    VertexBufferObject indexBuffer = VertexBufferObject.createVertexBufferObject(BufferDataType.UNSIGNED_SHORT, BufferTarget.ELEMENT_ARRAY_BUFFER, BufferUsage.STATIC_DRAW);
    indexBuffer.storeShortData(VertexBufferUtils.store(indices));
    vertexArrayObject.appendIndices(indexBuffer);

    VertexBufferObject positionBuffer = VertexBufferObject.createVertexBufferObject(BufferDataType.FLOAT, BufferTarget.ARRAY_BUFFER, BufferUsage.STATIC_DRAW);
    positionBuffer.storeFloatData(VertexBufferUtils.store(positions));
    vertexArrayObject.appendVertexBufferObject(positionBuffer, 0, 2, false, 0, 0);

    vertexArrayObject.unbind();
    return vertexArrayObject;
  }

  public static VertexArrayObject create2DCIFor(float[] positions, float[] colors, short[] indices) {
    VertexArrayObject vertexArrayObject = new VertexArrayObject(generateIdentifier(), indices.length);
    vertexArrayObject.bind();

    VertexBufferObject indexBuffer = VertexBufferObject.createVertexBufferObject(BufferDataType.UNSIGNED_SHORT, BufferTarget.ELEMENT_ARRAY_BUFFER, BufferUsage.STATIC_DRAW);
    indexBuffer.storeShortData(VertexBufferUtils.store(indices));
    vertexArrayObject.appendIndices(indexBuffer);

    VertexBufferObject positionBuffer = VertexBufferObject.createVertexBufferObject(BufferDataType.FLOAT, BufferTarget.ARRAY_BUFFER, BufferUsage.STATIC_DRAW);
    positionBuffer.storeFloatData(VertexBufferUtils.store(positions));
    vertexArrayObject.appendVertexBufferObject(positionBuffer, 0, 2, false, 0, 0);

    VertexBufferObject colorBuffer = VertexBufferObject.createVertexBufferObject(BufferDataType.UNSIGNED_BYTE, BufferTarget.ARRAY_BUFFER, BufferUsage.STATIC_DRAW);
    colorBuffer.storeIntData(VertexBufferUtils.store4fAs1i(colors));
    vertexArrayObject.appendVertexBufferObject(colorBuffer, 1, 4, true, 0, 0);

    vertexArrayObject.unbind();
    return vertexArrayObject;
  }
}
