package love.polardivision.engine.window.cursor;

import org.lwjgl.glfw.GLFW;

public enum CursorShape {

  ARROW(GLFW.GLFW_ARROW_CURSOR),
  I_BEAM(GLFW.GLFW_IBEAM_CURSOR),
  CROSS_HAIR(GLFW.GLFW_CROSSHAIR_CURSOR),
  POINTING_HAND(GLFW.GLFW_POINTING_HAND_CURSOR),
  H_RESIZE(GLFW.GLFW_RESIZE_EW_CURSOR),
  V_RESIZE(GLFW.GLFW_RESIZE_NS_CURSOR),
  RESIZE_TL_BR(GLFW.GLFW_RESIZE_NWSE_CURSOR),
  RESIZE_TR_BL(GLFW.GLFW_RESIZE_NESW_CURSOR),
  RESIZE_ALL(GLFW.GLFW_RESIZE_ALL_CURSOR),
  NOT_ALLOWED(GLFW.GLFW_NOT_ALLOWED_CURSOR);

  private final int value;

  CursorShape(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }
}
