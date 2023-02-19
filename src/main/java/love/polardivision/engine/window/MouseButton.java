/*
 * Copyright © 2022-2023 @Frooastside
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
