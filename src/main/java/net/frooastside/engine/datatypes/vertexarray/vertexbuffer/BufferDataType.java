package net.frooastside.engine.datatypes.vertexarray.vertexbuffer;

import org.lwjgl.opengl.GL11;

public enum BufferDataType {

  UNSIGNED_BYTE(GL11.GL_UNSIGNED_BYTE),
  UNSIGNED_SHORT(GL11.GL_UNSIGNED_SHORT),
  UNSIGNED_INT(GL11.GL_UNSIGNED_INT),
  FLOAT(GL11.GL_FLOAT),
  DOUBLE(GL11.GL_DOUBLE);

  private final int value;

  BufferDataType(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }

}
