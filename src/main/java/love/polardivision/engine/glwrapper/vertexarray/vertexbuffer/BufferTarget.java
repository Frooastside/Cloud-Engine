package love.polardivision.engine.glwrapper.vertexarray.vertexbuffer;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL21;

public enum BufferTarget {

  ARRAY_BUFFER(GL15.GL_ARRAY_BUFFER),
  ELEMENT_ARRAY_BUFFER(GL15.GL_ELEMENT_ARRAY_BUFFER),
  PIXEL_PACK_BUFFER(GL21.GL_PIXEL_PACK_BUFFER),
  PIXEL_UNPACK_BUFFER(GL21.GL_PIXEL_PACK_BUFFER);

  private final int value;

  BufferTarget(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }
}
