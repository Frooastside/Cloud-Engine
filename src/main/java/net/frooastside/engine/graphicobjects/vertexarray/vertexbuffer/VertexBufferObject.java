package net.frooastside.engine.graphicobjects.vertexarray.vertexbuffer;

import net.frooastside.engine.graphicobjects.GraphicObject;
import org.lwjgl.opengl.GL15;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class VertexBufferObject extends GraphicObject {

  private final int dataType;
  private final int target;
  private final int usage;

  private VertexBufferObject(int dataType, int target, int usage) {
    this.dataType = dataType;
    this.target = target;
    this.usage = usage;
  }

  public void storeFloatData(FloatBuffer data) {
    bind();
    GL15.glBufferData(target, data, usage);
    unbind();
  }

  public void storeIntData(IntBuffer data) {
    bind();
    GL15.glBufferData(target, data, usage);
    unbind();
  }

  public void storeShortData(ShortBuffer data) {
    bind();
    GL15.glBufferData(target, data, usage);
    unbind();
  }

  public void storeByteData(ByteBuffer data) {
    bind();
    GL15.glBufferData(target, data, usage);
    unbind();
  }

  public void storeFloatSubData(FloatBuffer data, long offset) {
    bind();
    GL15.glBufferSubData(target, offset, data);
    unbind();
  }

  public void storeIntSubData(IntBuffer data, long offset) {
    bind();
    GL15.glBufferSubData(target, offset, data);
    unbind();
  }

  public void storeShortSubData(ShortBuffer data, long offset) {
    bind();
    GL15.glBufferSubData(target, offset, data);
    unbind();
  }

  public void storeByteSubData(ByteBuffer data, long offset) {
    bind();
    GL15.glBufferSubData(target, offset, data);
    unbind();
  }

  @Override
  public void generateIdentifier() {
    identifier = GL15.glGenBuffers();
  }

  @Override
  public void bind() {
    GL15.glBindBuffer(target, identifier);
  }

  @Override
  public void unbind() {
    GL15.glBindBuffer(target, 0);
  }

  @Override
  public void delete() {
    GL15.glDeleteBuffers(identifier);
  }

  public static VertexBufferObject createVertexBufferObject(BufferDataType dataType, BufferTarget target, BufferUsage usage) {
    return new VertexBufferObject(dataType.value(), target.value(), usage.value());
  }

  public int dataType() {
    return dataType;
  }

}
