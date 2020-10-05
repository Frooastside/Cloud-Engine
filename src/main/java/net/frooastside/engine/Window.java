package net.frooastside.engine;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4i;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class Window {

  private final long identifier;

  private final Vector2i position = new Vector2i();
  private final Vector2i size = new Vector2i();
  private final Vector2i fullscreenResolution = new Vector2i();

  private boolean visible = true;
  private boolean fullscreen;
  private int refreshRate;

  private Monitor monitor;

  private long lastUpdateTime;
  private double delta;

  public Window(long identifier) {
    this.identifier = identifier;
  }

  public void setDefaults() {
    if(monitor == null) {
      monitor = Monitor.DEFAULT;
    }
    if(!visible) {
      hideWindow();
    }
    Vector4i workArea = monitor.workArea();
    int workAreaWidth = workArea.z;
    int workAreaHeight = workArea.w;
    int x = workAreaWidth / 2;
    int y = workAreaHeight / 2;
    setPosition(workAreaWidth / 2 - x / 2, workAreaHeight / 2 - y / 2);
    if(fullscreen) {
      setResolution(workAreaWidth, workAreaHeight);
    }else {
      setResolution(x, y);
    }
    resetWindowMode();
  }

  public void initialize() {
    GLFW.glfwMakeContextCurrent(identifier);
    GL.createCapabilities();
    resetVSync();
    GL11.glClearColor(0.5f, 0.5f, 0.5f, 1);
  }

  public static void clearBuffers() {
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);
  }

  public void updateWindow() {
    GLFW.glfwSwapBuffers(identifier);
    GLFW.glfwPollEvents();
    calculateDelta();
  }

  private void calculateDelta() {
    long currentTime = System.nanoTime();
    delta = (currentTime - lastUpdateTime) * 0.0000000001;
    if (delta >= 0.25f)
      delta = 0;
    lastUpdateTime = currentTime;
  }

  public void resetViewport() {
    if(fullscreen) {
      GL11.glViewport(0, 0, fullscreenResolution.x, fullscreenResolution.y);
    }else {
      GL11.glViewport(0, 0, size.x, size.y);
    }
  }

  public void applySizeChanges() {
    if(fullscreen) {
      GLFW.glfwSetWindowSize(identifier, fullscreenResolution.x, fullscreenResolution.y);
    }else {
      GLFW.glfwSetWindowSize(identifier, size.x, size.y);
    }
  }

  public void closeWindow() {
    GLFW.glfwDestroyWindow(identifier);
  }

  public void resetVSync() {
    if(refreshRate == 0) {
      GLFW.glfwSwapInterval(1);
    }
  }

  public void resetWindowMode() {
    if(fullscreen) {
      GLFW.glfwSetWindowMonitor(identifier, monitor.address(), 0, 0, fullscreenResolution.x, fullscreenResolution.y, refreshRate);
    }else {
      Vector4i workArea = monitor.workArea();
      if(position.x > workArea.x || position.y > workArea.y) {
        position.set(0, 0);
      }
      Vector2i monitorPosition = monitor.virtualPosition();
      GLFW.glfwSetWindowMonitor(identifier, 0, monitorPosition.x + position.x, monitorPosition.y +  position.y, size.x, size.y, refreshRate);
    }
  }

  public static Window createWindow(String title, boolean fullscreen) {
    return createWindow(Monitor.DEFAULT, title, fullscreen);
  }

  public static Window createWindow(Monitor monitor, String title, boolean fullscreen) {
    Vector4i workArea = monitor.workArea();
    int workAreaWidth = workArea.z;
    int workAreaHeight = workArea.w;
    long windowId = GLFW.glfwCreateWindow(workAreaWidth, workAreaHeight, title, 0, 0);
    Window window = new Window(windowId);
    window.fullscreen = fullscreen;
    window.setDefaults();
    return window;
  }

  public void setTitle(String title) {
    GLFW.glfwSetWindowTitle(identifier, title);
  }

  public void setPosition(int x, int y) {
    Vector4i workArea = monitor.workArea();
    if(x > workArea.x || y > workArea.y) {
      position.set(0, 0);
    }else {
      position.set(x, y);
    }
    if(!fullscreen) {
      Vector2i monitorPosition = monitor.virtualPosition();
      GLFW.glfwSetWindowPos(identifier, monitorPosition.x + position.x, monitorPosition.y + position.y);
    }
  }

  public void setResolution(int x, int y) {
    if(fullscreen) {
      if(x != fullscreenResolution.x || y != fullscreenResolution.y) {
        fullscreenResolution.set(x, y);
        applySizeChanges();
      }
    }else {
      if(x != size.x || y != size.y) {
        size.set(x, y);
        applySizeChanges();
      }
    }
  }

  public void hideWindow() {
    visible = false;
    GLFW.glfwHideWindow(identifier);
  }

  public void showWindow() {
    visible = true;
    GLFW.glfwShowWindow(identifier);
  }

  public void setFullscreen(boolean fullscreen) {
    if(this.fullscreen != fullscreen) {
      this.fullscreen = fullscreen;
      resetWindowMode();
    }
  }

  public void setRefreshRate(int refreshRate) {
    if(fullscreen) {
      if(refreshRate == 0) {
        resetVSync();
      }else {
        if(this.refreshRate != refreshRate) {
          this.refreshRate = refreshRate;
          resetWindowMode();
        }
      }
    }else {
      if(this.refreshRate != refreshRate) {
        this.refreshRate = refreshRate;
        resetVSync();
      }
    }
  }

  public boolean shouldWindowClose() {
    return GLFW.glfwWindowShouldClose(identifier);
  }

  public Vector2i position() {
    return position;
  }

  public Vector2i resolution() {
    return fullscreen ? fullscreenResolution : size;
  }

  public boolean visible() {
    return visible;
  }

  public boolean fullscreen() {
    return fullscreen;
  }

  public Monitor monitor() {
    return monitor;
  }

  public double delta() {
    return delta;
  }

  public static class Monitor {

    public static final Monitor DEFAULT = new Monitor(GLFW.glfwGetPrimaryMonitor());

    private static final List<Monitor> availableMonitors = new ArrayList<>();

    private long address;

    private Monitor(long address) {
      this.address = address;
    }

    public static List<Monitor> availableMonitors() {
      availableMonitors.clear();
      PointerBuffer monitorPointerBuffer = GLFW.glfwGetMonitors();
      if (monitorPointerBuffer == null)
        throw new IllegalStateException(new NullPointerException());
      long[] monitorPointer = new long[monitorPointerBuffer.remaining()];
      monitorPointerBuffer.get(monitorPointer);
      for (long monitorAddress : monitorPointer) {
        availableMonitors.add(new Monitor(monitorAddress));
      }
      return availableMonitors;
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
