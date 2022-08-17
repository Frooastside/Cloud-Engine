package love.polardivision.engine.window.hints;

import org.lwjgl.glfw.GLFW;

public enum ContextRobustness {

  NO_ROBUSTNESS(GLFW.GLFW_NO_ROBUSTNESS),
  NO_RESET_NOTIFICATION(GLFW.GLFW_NO_RESET_NOTIFICATION),
  LOSE_CONTEXT_ON_RESET(GLFW.GLFW_LOSE_CONTEXT_ON_RESET);

  private final int value;

  ContextRobustness(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }

}
