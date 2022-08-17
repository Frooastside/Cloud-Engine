package love.polardivision.engine.window.hints;

import org.lwjgl.glfw.GLFW;

public enum ContextReleaseBehavior {

  ANY_RELEASE_BEHAVIOR(GLFW.GLFW_ANY_RELEASE_BEHAVIOR),
  RELEASE_BEHAVIOR_FLUSH(GLFW.GLFW_RELEASE_BEHAVIOR_FLUSH),
  RELEASE_BEHAVIOR_NONE(GLFW.GLFW_RELEASE_BEHAVIOR_NONE);

  private final int value;

  ContextReleaseBehavior(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }

}
