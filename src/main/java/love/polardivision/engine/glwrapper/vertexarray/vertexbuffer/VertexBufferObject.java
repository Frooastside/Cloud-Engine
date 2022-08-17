package love.polardivision.engine.glwrapper.vertexarray.vertexbuffer;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import love.polardivision.engine.glwrapper.DataType;
import love.polardivision.engine.glwrapper.GraphicalObject;
import org.lwjgl.opengl.GL15;

public class VertexBufferObject extends GraphicalObject {

  private final int dataType;
  private final int target;
  private final int usage;

  public VertexBufferObject(DataType dataType, BufferTarget target, BufferUsage usage) {
    this(dataType.value(), target.value(), usage.value());
  }

  public VertexBufferObject(int dataType, int target, int usage) {
    this.dataType = dataType;
    this.target = target;
    this.usage = usage;
  }

  @Override
  public void initialize() {
    setIdentifier(GL15.glGenBuffers());
  }

  @Override
  public void bind() {
    GL15.glBindBuffer(target, identifier());
  }

  @Override
  public void unbind() {
    GL15.glBindBuffer(target, 0);
  }

  @Override
  public void delete() {
    GL15.glDeleteBuffers(identifier());
  }

  public void storeByteData(ByteBuffer data) {
    bind();
    GL15.glBufferData(target, data, usage);
    unbind();
  }

  public void storeShortData(ShortBuffer data) {
    bind();
    GL15.glBufferData(target, data, usage);
    unbind();
  }

  public void storeIntData(IntBuffer data) {
    bind();
    GL15.glBufferData(target, data, usage);
    unbind();
  }

  public void storeFloatData(FloatBuffer data) {
    bind();
    GL15.glBufferData(target, data, usage);
    unbind();
  }

  public void storeDoubleData(DoubleBuffer data) {
    bind();
    GL15.glBufferData(target, data, usage);
    unbind();
  }

  public void storeByteSubData(ByteBuffer data, long offset) {
    bind();
    GL15.glBufferSubData(target, offset, data);
    unbind();
  }

  public void storeShortSubData(ShortBuffer data, long offset) {
    bind();
    GL15.glBufferSubData(target, offset, data);
    unbind();
  }

  public void storeIntSubData(IntBuffer data, long offset) {
    bind();
    GL15.glBufferSubData(target, offset, data);
    unbind();
  }

  public void storeFloatSubData(FloatBuffer data, long offset) {
    bind();
    GL15.glBufferSubData(target, offset, data);
    unbind();
  }

  public void storeDoubleSubData(DoubleBuffer data, long offset) {
    bind();
    GL15.glBufferSubData(target, offset, data);
    unbind();
  }

  public int dataType() {
    return dataType;
  }

  public int target() {
    return target;
  }

  public int usage() {
    return usage;
  }
}
