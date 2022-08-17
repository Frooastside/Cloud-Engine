package love.polardivision.engine.glwrapper.vertexarray;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;

public enum Primitive {

  POINTS(GL11.GL_POINTS),
  LINE_STRIP(GL11.GL_LINE_STRIP),
  LINE_LOOP(GL11.GL_LINE_LOOP),
  LINES(GL11.GL_LINES),
  LINE_STRIP_ADJACENCY(GL32.GL_LINE_STRIP_ADJACENCY),
  LINES_ADJACENCY(GL32.GL_LINES_ADJACENCY),
  TRIANGLE_STRIP(GL11.GL_TRIANGLE_STRIP),
  TRIANGLE_FAN(GL11.GL_TRIANGLE_FAN),
  TRIANGLES(GL11.GL_TRIANGLES),
  TRIANGLE_STRIP_ADJACENCY(GL32.GL_TRIANGLE_STRIP_ADJACENCY),
  TRIANGLES_ADJACENCY(GL32.GL_TRIANGLES_ADJACENCY),
  PATCHES(GL40.GL_PATCHES);

  private final int value;

  Primitive(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }
}
