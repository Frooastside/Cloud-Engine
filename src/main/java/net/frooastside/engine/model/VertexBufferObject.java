package net.frooastside.engine.model;

import org.lwjgl.opengl.GL15;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class VertexBufferObject {

  private final int identifier;

  private final int dataType;
  private final int target;
  private final int usage;

  public VertexBufferObject(int identifier, int dataType, int target, int usage) {
    this.identifier = identifier;
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

  public void store4xCompressedIntData(IntBuffer data) {
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

  public void bind() {
    GL15.glBindBuffer(target, identifier);
  }

  public void unbind() {
    GL15.glBindBuffer(target, 0);
  }

  public void delete() {
    GL15.glDeleteBuffers(identifier);
  }

  public static VertexBufferObject createVertexBufferObject(BufferDataType dataType, BufferTarget target, BufferUsage usage) {
    return new VertexBufferObject(GL15.glGenBuffers(), dataType.value(), target.value(), usage.value());
  }

  public int dataType() {
    return dataType;
  }

}
