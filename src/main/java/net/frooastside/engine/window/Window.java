package net.frooastside.engine.window;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.frooastside.engine.window.callbacks.CharCallback;
import net.frooastside.engine.window.callbacks.CharModifiersCallback;
import net.frooastside.engine.window.callbacks.CursorEnterCallback;
import net.frooastside.engine.window.callbacks.CursorPositionCallback;
import net.frooastside.engine.window.callbacks.FileDropCallback;
import net.frooastside.engine.window.callbacks.FramebufferSizeCallback;
import net.frooastside.engine.window.callbacks.KeyCallback;
import net.frooastside.engine.window.callbacks.MouseButtonCallback;
import net.frooastside.engine.window.callbacks.ScrollCallback;
import net.frooastside.engine.window.callbacks.WindowCloseCallback;
import net.frooastside.engine.window.callbacks.WindowContentScaleCallback;
import net.frooastside.engine.window.callbacks.WindowFocusCallback;
import net.frooastside.engine.window.callbacks.WindowIconifyCallback;
import net.frooastside.engine.window.callbacks.WindowMaximizeCallback;
import net.frooastside.engine.window.callbacks.WindowPositionCallback;
import net.frooastside.engine.window.callbacks.WindowRefreshCallback;
import net.frooastside.engine.window.callbacks.WindowSizeCallback;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4i;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallbackI;
import org.lwjgl.glfw.GLFWCharModsCallbackI;
import org.lwjgl.glfw.GLFWCursorEnterCallbackI;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWDropCallback;
import org.lwjgl.glfw.GLFWDropCallbackI;
import org.lwjgl.glfw.GLFWErrorCallback;
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
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class Window {

  private final long identifier;

  private final Input input = new Input(this);

  private final Vector2i position = new Vector2i();
  private final Vector2i resolution = new Vector2i();

  private final Vector2i windowedSize = new Vector2i();
  private final Vector2i fullscreenSize = new Vector2i();

  private final Vector2f contentScale = new Vector2f(1.0f);

  private Monitor monitor;

  private boolean visible;
  private boolean fullscreen;
  private boolean iconified;
  private boolean maximized;
  private boolean focused;

  private long lastUpdateTime;
  private double delta;
  private int refreshRate;

  public Window(long identifier) {
    this.identifier = identifier;
  }

  static {
    GLFWErrorCallback.createPrint(System.err).set();
    GLFW.glfwInit();
  }

  public void initialize() {
    GLFW.glfwMakeContextCurrent(identifier);
    input.initialize();
    GL.createCapabilities();
    resetVSync();
    GL11.glClearColor(0.2f, 0.2f, 0.2f, 1);
  }

  public void updateWindow() {
    GLFW.glfwSwapBuffers(identifier);
    GLFW.glfwPollEvents();
    calculateDelta();
  }

  public void applySizeChanges() {
    if (fullscreen) {
      GLFW.glfwSetWindowSize(identifier, fullscreenSize.x, fullscreenSize.y);
    } else {
      GLFW.glfwSetWindowSize(identifier, windowedSize.x, windowedSize.y);
    }
  }

  public void resetViewport() {
    GL11.glViewport(0, 0, resolution.x, resolution.y);
  }

  public void resetWindowMode() {
    if (fullscreen) {
      GLFW.glfwSetWindowMonitor(identifier, monitor.address(), 0, 0, fullscreenSize.x, fullscreenSize.y, refreshRate);
    } else {
      Vector4i workArea = monitor.workArea();
      if (position.x > workArea.z || position.y > workArea.w) {
        position.set(0, 0);
      }
      Vector2i monitorPosition = monitor.virtualPosition();
      GLFW.glfwSetWindowMonitor(identifier, 0, monitorPosition.x + position.x, monitorPosition.y + position.y, windowedSize.x, windowedSize.y, refreshRate);
    }
  }

  public void resetVSync() {
    if (refreshRate == 0) {
      GLFW.glfwSwapInterval(1);
    } else {
      GLFW.glfwSwapInterval(0);
    }
  }

  private void calculateDelta() {
    long currentTime = System.nanoTime();
    delta = (currentTime - lastUpdateTime) * 0.000000001;
    if (delta >= 0.25f)
      delta = 0;
    lastUpdateTime = currentTime;
  }

  public void setTitle(String title) {
    GLFW.glfwSetWindowTitle(identifier, title);
  }

  public void setPosition(int x, int y) {
    Vector4i workArea = monitor.workArea();
    if (x > workArea.z || y > workArea.w) {
      position.set(0, 0);
    } else {
      position.set(x, y);
    }
    if (!fullscreen) {
      Vector2i monitorPosition = monitor.virtualPosition();
      GLFW.glfwSetWindowPos(identifier, monitorPosition.x + position.x, monitorPosition.y + position.y);
    }
  }

  public void setSize(int x, int y) {
    if (fullscreen) {
      if (x != fullscreenSize.x || y != fullscreenSize.y) {
        fullscreenSize.set(x, y);
        applySizeChanges();
      }
    } else {
      if (x != windowedSize.x || y != windowedSize.y) {
        windowedSize.set(x, y);
        applySizeChanges();
      }
    }
  }

  public void setFullscreen(boolean fullscreen) {
    if (this.fullscreen != fullscreen) {
      this.fullscreen = fullscreen;
      resetWindowMode();
    }
  }

  public void setRefreshRate(int refreshRate) {
    if (fullscreen) {
      if (refreshRate == 0) {
        resetVSync();
      } else {
        if (this.refreshRate != refreshRate) {
          this.refreshRate = refreshRate;
          resetWindowMode();
        }
      }
    } else {
      if (this.refreshRate != refreshRate) {
        this.refreshRate = refreshRate;
        resetVSync();
      }
    }
  }

  public void close() {
    GLFW.glfwDestroyWindow(identifier);
  }

  public void showWindow() {
    visible = true;
    GLFW.glfwShowWindow(identifier);
  }

  public void hideWindow() {
    visible = false;
    GLFW.glfwHideWindow(identifier);
  }

  public void iconify() {
    GLFW.glfwIconifyWindow(identifier);
  }

  public void maximize() {
    GLFW.glfwMaximizeWindow(identifier);
  }

  public void restore() {
    GLFW.glfwRestoreWindow(identifier);
  }

  public void focus() {
    GLFW.glfwFocusWindow(identifier);
  }

  public void requestAttention() {
    GLFW.glfwRequestWindowAttention(identifier);
  }

  public boolean shouldWindowClose() {
    return GLFW.glfwWindowShouldClose(identifier);
  }

  public static void shutdownWindowSystem() {
    GLFW.glfwTerminate();
  }

  public static void clearBuffers() {
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);
  }

  public static Window createWindow(String title, boolean fullscreen, CreationHint... creationHints) {
    return createWindow(Monitor.DEFAULT, title, true, fullscreen, 0.5f, creationHints);
  }

  public static Window createWindow(Monitor monitor, String title, boolean visible, boolean fullscreen, float nonFullscreenSize, CreationHint... creationHints) {
    if (monitor == null) {
      monitor = Monitor.DEFAULT;
    }
    GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
    for (CreationHint creationHint : creationHints) {
      GLFW.glfwWindowHint(creationHint.hint(), creationHint.value());
    }
    Vector4i workArea = monitor.workArea();
    int workAreaWidth = workArea.z;
    int workAreaHeight = workArea.w;
    int width = (int) (workAreaWidth * nonFullscreenSize);
    int height = (int) (workAreaHeight * nonFullscreenSize);
    Window window;
    if (fullscreen) {
      window = new Window(GLFW.glfwCreateWindow(workAreaWidth, workAreaHeight, title, monitor.address, 0));
      window.resolution.set(workAreaWidth, workAreaHeight);

    } else {
      window = new Window(GLFW.glfwCreateWindow(width, height, title, 0, 0));
      window.resolution.set(width, height);
    }
    window.monitor = monitor;
    window.fullscreen = fullscreen;
    window.visible = visible;
    window.fullscreenSize.set(workAreaWidth, workAreaHeight);
    window.windowedSize.set(width, height);
    window.setPosition(workAreaWidth / 2 - width / 2, workAreaHeight / 2 - height / 2);

    if (visible) {
      window.showWindow();
    }
    return window;
  }

  public long identifier() {
    return identifier;
  }

  public Input input() {
    return input;
  }

  public Vector2i position() {
    return position;
  }

  public Vector2i resolution() {
    return resolution;
  }

  public Vector2i currentSize() {
    return fullscreen ? fullscreenSize : windowedSize;
  }

  public Vector2i windowedSize() {
    return windowedSize;
  }

  public Vector2i fullscreenSize() {
    return fullscreenSize;
  }

  public Vector2f contentScale() {
    return contentScale;
  }

  public Monitor monitor() {
    return monitor;
  }

  public boolean visible() {
    return visible;
  }

  public boolean fullscreen() {
    return fullscreen;
  }

  public boolean iconified() {
    return iconified;
  }

  public boolean maximized() {
    return maximized;
  }

  public boolean focused() {
    return focused;
  }

  public double delta() {
    return delta;
  }


  public static class CreationHint {

    private int hint;
    private int value;

    public CreationHint(int hint, int value) {
      this.hint = hint;
      this.value = value;
    }

    public int hint() {
      return hint;
    }

    public void setHint(int hint) {
      this.hint = hint;
    }

    public int value() {
      return value;
    }

    public void setValue(int value) {
      this.value = value;
    }
  }

  public static class Input {

    private final boolean[] keyboardButtons = new boolean[GLFW.GLFW_KEY_LAST + 1];
    private final boolean[] mouseButtons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST + 1];

    private final Vector2f mousePosition = new Vector2f();
    private final Window window;

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
      this.window.resolution.set(width, height);
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
            keyboardButtons[key] = true;
            break;
          case GLFW.GLFW_RELEASE:
            keyboardButtons[key] = false;
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
          keyCallback.invokeKeyCallback(this.window, key, scancode, modifiers, keyAction);
        }
      }
    }

    private void handleMouseButton(long window, int button, int action, int mods) {
      if (rawMouseButtonCallback != null) {
        rawMouseButtonCallback.invoke(window, button, action, mods);
      }
      switch (action) {
        case GLFW.GLFW_PRESS:
          mouseButtons[button] = true;
          break;
        case GLFW.GLFW_RELEASE:
          mouseButtons[button] = false;
          break;
        default:
          throw new IllegalStateException();
      }
      if (mouseButtonCallback != null) {
        mouseButtonCallback.invokeMouseButtonCallback(this.window, button, mouseButtons[button]);
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
      this.window.contentScale.set(xScale, yScale);
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
      this.window.position.set(xPosition, yPosition);
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
      (this.window.fullscreen ? this.window.fullscreenSize : this.window.windowedSize).set(width, height);
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
      Monitor.availableMonitors();
    }

    public void hideCursor() {
      GLFW.glfwSetInputMode(window.identifier(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
    }

    public void disableCursor() {
      GLFW.glfwSetInputMode(window.identifier(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
    }

    public void showCursor() {
      GLFW.glfwSetInputMode(window.identifier(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
      GLFW.glfwSetCursorPos(window.identifier(), this.window.windowedSize.x / 2f, this.window.windowedSize.y / 2f); //TODO CHECK
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

    public boolean mouseButtonPressed(int button) {
      return mouseButtons[button];
    }

    public boolean keyPressed(int key) {
      return keyboardButtons[key];
    }
  }

  public static class Monitor {

    public static final Monitor DEFAULT = new Monitor(GLFW.glfwGetPrimaryMonitor());

    private static final List<Monitor> AVAILABLE_MONITORS = new ArrayList<>();

    private long address;

    private Monitor(long address) {
      this.address = address;
    }

    public static List<Monitor> availableMonitors() {
      AVAILABLE_MONITORS.clear();
      PointerBuffer monitorPointerBuffer = GLFW.glfwGetMonitors();
      if (monitorPointerBuffer == null)
        throw new IllegalStateException(new NullPointerException());
      long[] monitorPointer = new long[monitorPointerBuffer.remaining()];
      monitorPointerBuffer.get(monitorPointer);
      for (long monitorAddress : monitorPointer) {
        AVAILABLE_MONITORS.add(new Monitor(monitorAddress));
      }
      return AVAILABLE_MONITORS;
    }

    public String name() {
      return GLFW.glfwGetMonitorName(address);
    }

    public Vector2i virtualPosition() {
      int[] x = new int[1];
      int[] y = new int[1];
      GLFW.glfwGetMonitorPos(address, x, y);
      return new Vector2i(x[0], y[0]);
    }

    public Vector2f contentScale() {
      float[] scaleX = new float[1];
      float[] scaleY = new float[1];
      GLFW.glfwGetMonitorContentScale(address, scaleX, scaleY);
      return new Vector2f(scaleX[0], scaleY[0]);
    }

    public Vector4i workArea() {
      int[] x = new int[1];
      int[] y = new int[1];
      int[] width = new int[1];
      int[] height = new int[1];
      GLFW.glfwGetMonitorWorkarea(address, x, y, width, height);
      return new Vector4i(x[0], y[0], width[0], height[0]);
    }

    public long address() {
      return address;
    }

    public void setAddress(long address) {
      this.address = address;
    }
  }

}
