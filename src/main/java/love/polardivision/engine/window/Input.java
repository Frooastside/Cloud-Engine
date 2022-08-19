/*
 * Copyright 2022 @Frooastside
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package love.polardivision.engine.window;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import love.polardivision.engine.window.callbacks.CharCallback;
import love.polardivision.engine.window.callbacks.CharModifiersCallback;
import love.polardivision.engine.window.callbacks.CursorEnterCallback;
import love.polardivision.engine.window.callbacks.CursorPositionCallback;
import love.polardivision.engine.window.callbacks.FileDropCallback;
import love.polardivision.engine.window.callbacks.FramebufferSizeCallback;
import love.polardivision.engine.window.callbacks.KeyCallback;
import love.polardivision.engine.window.callbacks.MouseButtonCallback;
import love.polardivision.engine.window.callbacks.ScrollCallback;
import love.polardivision.engine.window.callbacks.WindowCloseCallback;
import love.polardivision.engine.window.callbacks.WindowContentScaleCallback;
import love.polardivision.engine.window.callbacks.WindowFocusCallback;
import love.polardivision.engine.window.callbacks.WindowIconifyCallback;
import love.polardivision.engine.window.callbacks.WindowMaximizeCallback;
import love.polardivision.engine.window.callbacks.WindowPositionCallback;
import love.polardivision.engine.window.callbacks.WindowRefreshCallback;
import love.polardivision.engine.window.callbacks.WindowSizeCallback;
import love.polardivision.engine.window.cursor.Cursor;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallbackI;
import org.lwjgl.glfw.GLFWCharModsCallbackI;
import org.lwjgl.glfw.GLFWCursorEnterCallbackI;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWDropCallback;
import org.lwjgl.glfw.GLFWDropCallbackI;
import org.lwjgl.glfw.GLFWFramebufferSizeCallbackI;
import org.lwjgl.glfw.GLFWJoystickCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMonitorCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWScrollCallbackI;
import org.lwjgl.glfw.GLFWWindowCloseCallbackI;
import org.lwjgl.glfw.GLFWWindowContentScaleCallbackI;
import org.lwjgl.glfw.GLFWWindowFocusCallbackI;
import org.lwjgl.glfw.GLFWWindowIconifyCallbackI;
import org.lwjgl.glfw.GLFWWindowMaximizeCallbackI;
import org.lwjgl.glfw.GLFWWindowPosCallbackI;
import org.lwjgl.glfw.GLFWWindowRefreshCallbackI;
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;

public class Input {

  private final boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST + 1];

  private final Vector2f mousePosition = new Vector2f();
  private final Window window;

  private boolean rawMouseMotion = GLFW.glfwRawMouseMotionSupported();

  private CharCallback charCallback;
  private CharModifiersCallback charModifiersCallback;
  private CursorEnterCallback cursorEnterCallback;
  private CursorPositionCallback cursorPositionCallback;
  private FileDropCallback fileDropCallback;
  private FramebufferSizeCallback framebufferSizeCallback;
  private KeyCallback keyCallback;
  private MouseButtonCallback mouseButtonCallback;
  private ScrollCallback scrollCallback;
  private WindowCloseCallback windowCloseCallback;
  private WindowContentScaleCallback windowContentScaleCallback;
  private WindowFocusCallback windowFocusCallback;
  private WindowIconifyCallback windowIconifyCallback;
  private WindowMaximizeCallback windowMaximizeCallback;
  private WindowPositionCallback windowPositionCallback;
  private WindowRefreshCallback windowRefreshCallback;
  private WindowSizeCallback windowSizeCallback;

  private GLFWCharCallbackI rawCharCallback;
  private GLFWCharModsCallbackI rawCharModsCallback;
  private GLFWCursorEnterCallbackI rawCursorEnterCallback;
  private GLFWCursorPosCallbackI rawCursorPositionCallback;
  private GLFWDropCallbackI rawFileDropCallback;
  private GLFWFramebufferSizeCallbackI rawFramebufferSizeCallback;
  private GLFWKeyCallbackI rawKeyCallback;
  private GLFWMouseButtonCallbackI rawMouseButtonCallback;
  private GLFWScrollCallbackI rawScrollCallback;
  private GLFWWindowCloseCallbackI rawWindowCloseCallback;
  private GLFWWindowContentScaleCallbackI rawWindowContentScaleCallback;
  private GLFWWindowFocusCallbackI rawWindowFocusCallback;
  private GLFWWindowIconifyCallbackI rawWindowIconifyCallback;
  private GLFWWindowMaximizeCallbackI rawWindowMaximizeCallback;
  private GLFWWindowPosCallbackI rawWindowPositionCallback;
  private GLFWWindowRefreshCallbackI rawWindowRefreshCallback;
  private GLFWWindowSizeCallbackI rawWindowSizeCallback;

  private static GLFWJoystickCallbackI rawJoystickCallback;
  private static GLFWMonitorCallbackI rawMonitorCallback;

  private static GLFWJoystickCallbackI controllerModuleCallback;

  public Input(Window window) {
    this.window = window;
  }

  static {
    GLFW.glfwSetJoystickCallback(Input::handleJoystick);
    GLFW.glfwSetMonitorCallback(Input::handleMonitor);
  }

  public void initialize() {
    GLFW.glfwSetCharCallback(window.identifier(), this::handleChar);
    GLFW.glfwSetCharModsCallback(window.identifier(), this::handleCharModifiers);
    GLFW.glfwSetCursorEnterCallback(window.identifier(), this::handleCursorEnter);
    GLFW.glfwSetCursorPosCallback(window.identifier(), this::handleCursorPosition);
    GLFW.glfwSetDropCallback(window.identifier(), this::handleFileDrop);
    GLFW.glfwSetFramebufferSizeCallback(window.identifier(), this::handleFramebufferSize);
    GLFW.glfwSetKeyCallback(window.identifier(), this::handleKey);
    GLFW.glfwSetMouseButtonCallback(window.identifier(), this::handleMouseButton);
    GLFW.glfwSetScrollCallback(window.identifier(), this::handleScroll);
    GLFW.glfwSetWindowCloseCallback(window.identifier(), this::handleWindowClose);
    GLFW.glfwSetWindowContentScaleCallback(window.identifier(), this::handleWindowContentScale);
    GLFW.glfwSetWindowFocusCallback(window.identifier(), this::handleWindowFocus);
    GLFW.glfwSetWindowIconifyCallback(window.identifier(), this::handleWindowIconify);
    GLFW.glfwSetWindowMaximizeCallback(window.identifier(), this::handleWindowMaximize);
    GLFW.glfwSetWindowPosCallback(window.identifier(), this::handleWindowPosition);
    GLFW.glfwSetWindowRefreshCallback(window.identifier(), this::handleWindowRefresh);
    GLFW.glfwSetWindowSizeCallback(window.identifier(), this::handleWindowSize);

    GLFW.glfwSetInputMode(window.identifier(), GLFW.GLFW_RAW_MOUSE_MOTION, rawMouseMotion ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
  }

  private void handleChar(long window, int codepoint) {
    if (rawCharCallback != null) {
      rawCharCallback.invoke(window, codepoint);
    }
    if (charCallback != null) {
      charCallback.invokeCharCallback(this.window, (char) codepoint);
    }
  }

  private void handleCharModifiers(long window, int codepoint, int modifiers) {
    if (rawCharModsCallback != null) {
      rawCharModsCallback.invoke(window, codepoint, modifiers);
    }
    if (charModifiersCallback != null) {
      charModifiersCallback.invokeCharModifiersCallback(this.window, (char) codepoint, modifiers);
    }
  }

  private void handleCursorEnter(long window, boolean entered) {
    if (rawCursorEnterCallback != null) {
      rawCursorEnterCallback.invoke(window, entered);
    }
    if (cursorEnterCallback != null) {
      cursorEnterCallback.invokeCursorEnterCallback(this.window, entered);
    }
  }

  private void handleCursorPosition(long window, double xPosition, double yPosition) {
    if (rawCursorPositionCallback != null) {
      rawCursorPositionCallback.invoke(window, xPosition, yPosition);
    }
    mousePosition.set((float) xPosition, (float) yPosition);
    if (cursorPositionCallback != null) {
      cursorPositionCallback.invokeCursorPositionCallback(this.window, (float) xPosition, (float) yPosition);
    }
  }

  private void handleFileDrop(long window, int count, long names) {
    if (rawFileDropCallback != null) {
      rawFileDropCallback.invoke(window, count, names);
    }
    if (fileDropCallback != null) {
      List<File> files = new ArrayList<>(count);
      for (int i = 0; i < count; i++) {
        String filename = GLFWDropCallback.getName(names, i);
        File file = new File(filename);
        if (file.exists()) {
          files.add(file);
        }
      }
      if (!files.isEmpty()) {
        fileDropCallback.invokeFileDropCallback(this.window, files.toArray(new File[0]));
      }
    }
  }

  private void handleFramebufferSize(long window, int width, int height) {
    if (rawFramebufferSizeCallback != null) {
      rawFramebufferSizeCallback.invoke(window, width, height);
    }
    this.window.resolution().set(width, height);
    if (framebufferSizeCallback != null) {
      framebufferSizeCallback.invokeFramebufferSizeCallback(this.window, width, height);
    }
  }

  private void handleKey(long window, int key, int scancode, int action, int modifiers) {
    if (rawKeyCallback != null) {
      rawKeyCallback.invoke(window, key, scancode, action, modifiers);
    }
    if (key != GLFW.GLFW_KEY_UNKNOWN) {
      switch (action) {
        case GLFW.GLFW_PRESS:
          keys[key] = true;
          break;
        case GLFW.GLFW_RELEASE:
          keys[key] = false;
          break;
        case GLFW.GLFW_REPEAT:
          break;
        default:
          throw new IllegalStateException();
      }
      if (keyCallback != null) {
        KeyCallback.Action keyAction =
          action == GLFW.GLFW_PRESS ? KeyCallback.Action.PRESS :
            action == GLFW.GLFW_RELEASE ? KeyCallback.Action.RELEASE :
              KeyCallback.Action.REPEAT;
        keyCallback.invokeKeyCallback(this.window, Key.of(key), scancode, modifiers, keyAction);
      }
    }
  }

  private void handleMouseButton(long window, int button, int action, int mods) {
    if (rawMouseButtonCallback != null) {
      rawMouseButtonCallback.invoke(window, button, action, mods);
    }
    switch (action) {
      case GLFW.GLFW_PRESS -> keys[button] = true;
      case GLFW.GLFW_RELEASE -> keys[button] = false;
      default -> throw new IllegalStateException();
    }
    if (mouseButtonCallback != null) {
      mouseButtonCallback.invokeMouseButtonCallback(this.window, MouseButton.of(button), keys[button]);
    }
  }

  private void handleScroll(long window, double xOffset, double yOffset) {
    if (rawScrollCallback != null) {
      rawScrollCallback.invoke(window, xOffset, yOffset);
    }
    if (scrollCallback != null) {
      scrollCallback.invokeScrollCallback(this.window, xOffset, yOffset);
    }
  }

  private void handleWindowClose(long window) {
    if (rawWindowCloseCallback != null) {
      rawWindowCloseCallback.invoke(window);
    }
    if (windowCloseCallback != null) {
      windowCloseCallback.invokeWindowCloseCallback(this.window);
    }
  }

  private void handleWindowContentScale(long window, float xScale, float yScale) {
    if (rawWindowContentScaleCallback != null) {
      rawWindowContentScaleCallback.invoke(window, xScale, yScale);
    }
    this.window.contentScale().set(xScale, yScale);
    if (windowContentScaleCallback != null) {
      windowContentScaleCallback.invokeWindowContentScaleCallback(this.window, xScale, yScale);
    }
  }

  private void handleWindowFocus(long window, boolean focused) {
    if (rawWindowFocusCallback != null) {
      rawWindowFocusCallback.invoke(window, focused);
    }
    this.window.focused = focused;
    if (windowFocusCallback != null) {
      windowFocusCallback.invokeWindowFocusCallback(this.window, focused);
    }
  }

  private void handleWindowIconify(long window, boolean iconified) {
    if (rawWindowIconifyCallback != null) {
      rawWindowIconifyCallback.invoke(window, iconified);
    }
    this.window.iconified = iconified;
    if (windowIconifyCallback != null) {
      windowIconifyCallback.invokeWindowIconifyCallback(this.window, iconified);
    }
  }

  private void handleWindowMaximize(long window, boolean maximized) {
    if (rawWindowMaximizeCallback != null) {
      rawWindowMaximizeCallback.invoke(window, maximized);
    }
    this.window.maximized = maximized;
    if (windowMaximizeCallback != null) {
      windowMaximizeCallback.invokeWindowMaximizeCallback(this.window, maximized);
    }
  }

  private void handleWindowPosition(long window, int xPosition, int yPosition) {
    if (rawWindowPositionCallback != null) {
      rawWindowPositionCallback.invoke(window, xPosition, yPosition);
    }
    this.window.position().set(xPosition, yPosition);
    if (windowPositionCallback != null) {
      windowPositionCallback.invokeWindowPositionCallback(this.window, xPosition, yPosition);
    }
  }

  private void handleWindowRefresh(long window) {
    if (rawWindowRefreshCallback != null) {
      rawWindowRefreshCallback.invoke(window);
    }
    if (windowRefreshCallback != null) {
      windowRefreshCallback.invokeRefreshCallback(this.window);
    }
  }

  private void handleWindowSize(long window, int width, int height) {
    if (rawWindowSizeCallback != null) {
      rawWindowSizeCallback.invoke(window, width, height);
    }
    (this.window.fullscreen() ? this.window.fullscreenSize() : this.window.windowedSize()).set(width, height);
    if (windowSizeCallback != null) {
      windowSizeCallback.invokeWindowSizeCallback(this.window, width, height);
    }
  }

  private static void handleJoystick(int jid, int event) {
    if (rawJoystickCallback != null) {
      rawJoystickCallback.invoke(jid, event);
    }
    if (controllerModuleCallback != null) {
      controllerModuleCallback.invoke(jid, event);
    }
  }

  private static void handleMonitor(long monitor, int event) {
    if (rawMonitorCallback != null) {
      rawMonitorCallback.invoke(monitor, event);
    }
    Monitor.updateAvailableMonitors();
  }

  public void enableRawMouseMotion() {
    if (GLFW.glfwRawMouseMotionSupported() && !rawMouseMotion) {
      this.rawMouseMotion = true;
      if (GLFW.glfwGetInputMode(window.identifier(), GLFW.GLFW_CURSOR) == GLFW.GLFW_CURSOR_DISABLED) {
        GLFW.glfwSetInputMode(window.identifier(), GLFW.GLFW_RAW_MOUSE_MOTION, GLFW.GLFW_TRUE);
      }
    }
  }

  public void disableRawMouseMotion() {
    if (rawMouseMotion) {
      this.rawMouseMotion = false;
      if (GLFW.glfwGetInputMode(window.identifier(), GLFW.GLFW_CURSOR) == GLFW.GLFW_CURSOR_DISABLED) {
        GLFW.glfwSetInputMode(window.identifier(), GLFW.GLFW_RAW_MOUSE_MOTION, GLFW.GLFW_FALSE);
      }
    }
  }

  public void hideCursor() {
    GLFW.glfwSetInputMode(window.identifier(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
  }

  public void disableCursor() {
    GLFW.glfwSetInputMode(window.identifier(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
  }

  public void showCursor() {
    GLFW.glfwSetInputMode(window.identifier(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
    GLFW.glfwSetCursorPos(window.identifier(), this.window.windowedSize().x / 2f, this.window.windowedSize().y / 2f);
  }

  public void setCursor(Cursor cursor) {
    GLFW.glfwSetCursor(window.identifier(), cursor.address());
  }

  public String getClipboard() {
    String clipboard = GLFW.glfwGetClipboardString(window.identifier());
    return Objects.requireNonNullElse(clipboard, "");
  }

  public void setClipboard(String text) {
    GLFW.glfwSetClipboardString(window.identifier(), text);
  }

  public void setCharCallback(CharCallback charCallback) {
    this.charCallback = charCallback;
  }

  public void setCharModifiersCallback(CharModifiersCallback charModifiersCallback) {
    this.charModifiersCallback = charModifiersCallback;
  }

  public void setCursorEnterCallback(CursorEnterCallback cursorEnterCallback) {
    this.cursorEnterCallback = cursorEnterCallback;
  }

  public void setCursorPositionCallback(CursorPositionCallback cursorPositionCallback) {
    this.cursorPositionCallback = cursorPositionCallback;
  }

  public void setFileDropCallback(FileDropCallback fileDropCallback) {
    this.fileDropCallback = fileDropCallback;
  }

  public void setFramebufferSizeCallback(FramebufferSizeCallback framebufferSizeCallback) {
    this.framebufferSizeCallback = framebufferSizeCallback;
  }

  public void setKeyCallback(KeyCallback keyCallback) {
    this.keyCallback = keyCallback;
  }

  public void setMouseButtonCallback(MouseButtonCallback mouseButtonCallback) {
    this.mouseButtonCallback = mouseButtonCallback;
  }

  public void setScrollCallback(ScrollCallback scrollCallback) {
    this.scrollCallback = scrollCallback;
  }

  public void setWindowCloseCallback(WindowCloseCallback windowCloseCallback) {
    this.windowCloseCallback = windowCloseCallback;
  }

  public void setWindowContentScaleCallback(WindowContentScaleCallback windowContentScaleCallback) {
    this.windowContentScaleCallback = windowContentScaleCallback;
  }

  public void setWindowFocusCallback(WindowFocusCallback windowFocusCallback) {
    this.windowFocusCallback = windowFocusCallback;
  }

  public void setWindowIconifyCallback(WindowIconifyCallback windowIconifyCallback) {
    this.windowIconifyCallback = windowIconifyCallback;
  }

  public void setWindowMaximizeCallback(WindowMaximizeCallback windowMaximizeCallback) {
    this.windowMaximizeCallback = windowMaximizeCallback;
  }

  public void setWindowPositionCallback(WindowPositionCallback windowPositionCallback) {
    this.windowPositionCallback = windowPositionCallback;
  }

  public void setWindowRefreshCallback(WindowRefreshCallback windowRefreshCallback) {
    this.windowRefreshCallback = windowRefreshCallback;
  }

  public void setWindowSizeCallback(WindowSizeCallback windowSizeCallback) {
    this.windowSizeCallback = windowSizeCallback;
  }

  public void setRawCharCallback(GLFWCharCallbackI rawCharCallback) {
    this.rawCharCallback = rawCharCallback;
  }

  public void setRawCharModsCallback(GLFWCharModsCallbackI rawCharModsCallback) {
    this.rawCharModsCallback = rawCharModsCallback;
  }

  public void setRawCursorEnterCallback(GLFWCursorEnterCallbackI rawCursorEnterCallback) {
    this.rawCursorEnterCallback = rawCursorEnterCallback;
  }

  public void setRawCursorPositionCallback(GLFWCursorPosCallbackI rawCursorPositionCallback) {
    this.rawCursorPositionCallback = rawCursorPositionCallback;
  }

  public void setRawFileDropCallback(GLFWDropCallbackI rawFileDropCallback) {
    this.rawFileDropCallback = rawFileDropCallback;
  }

  public void setRawFramebufferSizeCallback(GLFWFramebufferSizeCallbackI rawFramebufferSizeCallback) {
    this.rawFramebufferSizeCallback = rawFramebufferSizeCallback;
  }

  public void setRawKeyCallback(GLFWKeyCallbackI rawKeyCallback) {
    this.rawKeyCallback = rawKeyCallback;
  }

  public void setRawMouseButtonCallback(GLFWMouseButtonCallbackI rawMouseButtonCallback) {
    this.rawMouseButtonCallback = rawMouseButtonCallback;
  }

  public void setRawScrollCallback(GLFWScrollCallbackI rawScrollCallback) {
    this.rawScrollCallback = rawScrollCallback;
  }

  public void setRawWindowCloseCallback(GLFWWindowCloseCallbackI rawWindowCloseCallback) {
    this.rawWindowCloseCallback = rawWindowCloseCallback;
  }

  public void setRawWindowContentScaleCallback(GLFWWindowContentScaleCallbackI rawWindowContentScaleCallback) {
    this.rawWindowContentScaleCallback = rawWindowContentScaleCallback;
  }

  public void setRawWindowFocusCallback(GLFWWindowFocusCallbackI rawWindowFocusCallback) {
    this.rawWindowFocusCallback = rawWindowFocusCallback;
  }

  public void setRawWindowIconifyCallback(GLFWWindowIconifyCallbackI rawWindowIconifyCallback) {
    this.rawWindowIconifyCallback = rawWindowIconifyCallback;
  }

  public void setRawWindowMaximizeCallback(GLFWWindowMaximizeCallbackI rawWindowMaximizeCallback) {
    this.rawWindowMaximizeCallback = rawWindowMaximizeCallback;
  }

  public void setRawWindowPositionCallback(GLFWWindowPosCallbackI rawWindowPositionCallback) {
    this.rawWindowPositionCallback = rawWindowPositionCallback;
  }

  public void setRawWindowRefreshCallback(GLFWWindowRefreshCallbackI rawWindowRefreshCallback) {
    this.rawWindowRefreshCallback = rawWindowRefreshCallback;
  }

  public void setRawWindowSizeCallback(GLFWWindowSizeCallbackI rawWindowSizeCallback) {
    this.rawWindowSizeCallback = rawWindowSizeCallback;
  }

  public static void setRawJoystickCallback(GLFWJoystickCallbackI rawJoystickCallback) {
    Input.rawJoystickCallback = rawJoystickCallback;
  }

  public static void setRawMonitorCallback(GLFWMonitorCallbackI rawMonitorCallback) {
    Input.rawMonitorCallback = rawMonitorCallback;
  }

  protected static void setControllerModuleCallback(GLFWJoystickCallbackI controllerModuleCallback) {
    Input.controllerModuleCallback = controllerModuleCallback;
  }

  public Vector2f mousePosition() {
    return mousePosition;
  }

  public boolean mouseButtonPressed(MouseButton button) {
    return keys[button.value()];
  }

  public boolean keyPressed(Key key) {
    return keys[key.value()];
  }
}
