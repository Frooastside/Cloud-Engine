package love.polardivision.engine.glwrapper.vertexarray;

import java.util.Arrays;
import java.util.Objects;
import love.polardivision.engine.glwrapper.GraphicalObject;
import love.polardivision.engine.glwrapper.vertexarray.vertexbuffer.VertexBufferObject;
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
