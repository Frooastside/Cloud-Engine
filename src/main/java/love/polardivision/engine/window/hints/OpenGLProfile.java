package love.polardivision.engine.window.hints;

import org.lwjgl.glfw.GLFW;

public enum OpenGLProfile {

  OPENGL_ANY_PROFILE(GLFW.GLFW_OPENGL_ANY_PROFILE),
  OPENGL_COMPAT_PROFILE(GLFW.GLFW_OPENGL_COMPAT_PROFILE),
  OPENGL_CORE_PROFILE(GLFW.GLFW_OPENGL_CORE_PROFILE);

  private final int value;

  OpenGLProfile(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }


}
