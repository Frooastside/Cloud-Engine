package love.polardivision.engine.window.hints;

import org.lwjgl.glfw.GLFW;

public enum ClientAPI {

  NO_API(GLFW.GLFW_NO_API),
  OPENGL(GLFW.GLFW_OPENGL_API),
  OPENGL_ES(GLFW.GLFW_OPENGL_ES_API);

  private final int value;

  ClientAPI(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }

}
