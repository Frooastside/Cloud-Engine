package love.polardivision.engine.window;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.glfw.GLFW;

public enum MouseButton {

  MOUSE_BUTTON_LEFT(GLFW.GLFW_MOUSE_BUTTON_1),
  MOUSE_BUTTON_RIGHT(GLFW.GLFW_MOUSE_BUTTON_2),
  MOUSE_BUTTON_MIDDLE(GLFW.GLFW_MOUSE_BUTTON_3),
  KEY_4(GLFW.GLFW_MOUSE_BUTTON_4),
  KEY_5(GLFW.GLFW_MOUSE_BUTTON_5),
  KEY_6(GLFW.GLFW_MOUSE_BUTTON_6),
  KEY_7(GLFW.GLFW_MOUSE_BUTTON_7),
  KEY_8(GLFW.GLFW_MOUSE_BUTTON_8),
  KEY_UNKNOWN(-1);

  private static final Map<Integer, MouseButton> MOUSE_BUTTONS = new HashMap<>();
  private final int value;

  MouseButton(int value) {
    this.value = value;
  }

  static {
    Arrays.stream(values()).forEach(button -> MOUSE_BUTTONS.put(button.value, button));
  }

  public static MouseButton of(int button) {
    return MOUSE_BUTTONS.getOrDefault(button, KEY_UNKNOWN);
  }

  public int value() {
    return value;
  }
}
