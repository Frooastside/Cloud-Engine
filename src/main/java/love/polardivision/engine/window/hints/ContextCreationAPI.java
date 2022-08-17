package love.polardivision.engine.window.hints;

import org.lwjgl.glfw.GLFW;

public enum ContextCreationAPI {

  NATIVE_CONTEXT_API(GLFW.GLFW_NATIVE_CONTEXT_API),
  EGL_CONTEXT_API(GLFW.GLFW_EGL_CONTEXT_API),
  OS_MESA_CONTEXT_API(GLFW.GLFW_OSMESA_CONTEXT_API);

  private final int value;

  ContextCreationAPI(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }

}
