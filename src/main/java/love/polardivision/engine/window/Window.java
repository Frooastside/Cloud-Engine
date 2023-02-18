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

import love.polardivision.engine.window.hints.CreationHint;
import love.polardivision.engine.wrappers.NativeObject;
import love.polardivision.engine.wrappers.SizedObject;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4i;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

public class Window implements SizedObject, NativeObject {

  private static final CreationHint.Visible VISIBLE_HINT = new CreationHint.Visible(false);

  private final long identifier;

  private final Input input = new Input(this);

  private final Vector2i position = new Vector2i();
  private final Vector2i resolution = new Vector2i();

  private final Vector2i windowedSize = new Vector2i();
  private final Vector2i fullscreenSize = new Vector2i();

  private final Vector4i sizeLimits = new Vector4i(-1, -1, -1, -1);
  private final Vector2i aspectRatio = new Vector2i(-1, -1);

  private final Vector2f contentScale = new Vector2f(1.0f);

  private Monitor monitor;

  private boolean visible;
  private boolean fullscreen;
  protected boolean iconified;
  protected boolean maximized;
  protected boolean focused;

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

  @Override
  public void initialize() {
    GLFW.glfwMakeContextCurrent(identifier);
    input.initialize();
    GL.createCapabilities();
    resetVSync();
  }

  @Override
  public void delete() {
    GLFW.glfwDestroyWindow(identifier);
  }

  public void updateWindow() {
    GLFW.glfwSwapBuffers(identifier);
    GLFW.glfwPollEvents();
    calculateDelta();
  }

  private void applySizeChanges() {
    if (fullscreen) {
      GLFW.glfwSetWindowSize(identifier, fullscreenSize.x, fullscreenSize.y);
    } else {
      GLFW.glfwSetWindowSize(identifier, windowedSize.x, windowedSize.y);
    }
  }

  private void resetWindowMode() {
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

  private void resetVSync() {
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

  public void setMonitor(Monitor monitor) {
    this.monitor = monitor;
    if (fullscreen) {
      resetWindowMode();
    }
  }

  public void setSizeLimits(int minWidth, int minHeight, int maxWidth, int maxHeight) {
    if (!sizeLimits.equals(minWidth, minHeight, maxWidth, maxHeight)) {
      GLFW.glfwSetWindowSizeLimits(identifier, minWidth, minHeight, maxWidth, maxHeight);
      sizeLimits.set(minWidth, minHeight, maxWidth, maxHeight);
    }
  }

  public void resetSizeLimits() {
    setSizeLimits(-1, -1, -1, -1);
  }

  public void setAspectRatio(int x, int y) {
    if (!aspectRatio.equals(x, y)) {
      GLFW.glfwSetWindowAspectRatio(identifier, x, y);
      aspectRatio.set(x, y);
    }
  }

  public void resetAspectRatio() {
    setAspectRatio(-1, -1);
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

  public void showWindow() {
    visible = true;
    GLFW.glfwShowWindow(identifier);
  }

  public void hideWindow() {
    visible = false;
    GLFW.glfwHideWindow(identifier);
  }

  public void restore() {
    GLFW.glfwRestoreWindow(identifier);
  }

  public void iconify() {
    GLFW.glfwIconifyWindow(identifier);
  }

  public void maximize() {
    GLFW.glfwMaximizeWindow(identifier);
  }

  public void focus() {
    GLFW.glfwFocusWindow(identifier);
  }

  public void requestAttention() {
    GLFW.glfwRequestWindowAttention(identifier);
  }

  public void setShouldWindowClose(boolean shouldClose) {
    GLFW.glfwSetWindowShouldClose(identifier, shouldClose);
  }

  public boolean shouldWindowClose() {
    return GLFW.glfwWindowShouldClose(identifier);
  }

  public void setWindowOpacity(float opacity) {
    GLFW.glfwSetWindowOpacity(identifier, opacity);
  }

  public static void shutdownWindowSystem() {
    GLFW.glfwTerminate();
  }

  public static Window createWindow(String title, boolean fullscreen, CreationHint... creationHints) {
    return createWindow(Monitor.DEFAULT, title, true, fullscreen, 0.5f, creationHints);
  }

  public static Window createWindow(Monitor monitor, String title, boolean visible, boolean fullscreen, float nonFullscreenSize, CreationHint... creationHints) {
    if (monitor == null) {
      monitor = Monitor.DEFAULT;
    }
    CreationHint.resetAll();
    VISIBLE_HINT.apply();
    for (CreationHint creationHint : creationHints) {
      creationHint.apply();
    }
    Vector4i workArea = monitor.workArea();
    int workAreaWidth = workArea.z;
    int workAreaHeight = workArea.w;
    int width = (int) (workAreaWidth * nonFullscreenSize);
    int height = (int) (workAreaHeight * nonFullscreenSize);
    Window window;
    if (fullscreen) {
      window = new Window(GLFW.glfwCreateWindow(workAreaWidth, workAreaHeight, title, monitor.address(), 0));
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

  @Override
  public int width() {
    return resolution().x;
  }

  @Override
  public int height() {
    return resolution().y;
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
}
