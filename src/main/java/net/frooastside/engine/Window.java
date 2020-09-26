package net.frooastside.engine;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class Window {

  private String title;
  private boolean fullscreen;
  private final long windowId;
  private int windowWidth;
  private int windowHeight;

  private long lastUpdateTime;
  private double delta;

  public Window(String title, boolean fullscreen, long windowId, int windowWidth, int windowHeight) {
    this.title = title;
    this.fullscreen = fullscreen;
    this.windowId = windowId;
    this.windowWidth = windowWidth;
    this.windowHeight = windowHeight;
  }

  public void initGLContext(boolean vSync) {
    GLFW.glfwMakeContextCurrent(windowId);
    GLFW.glfwSwapInterval(vSync ? 1 : 0);
    GL.createCapabilities();
    GL11.glEnable(GL13.GL_MULTISAMPLE);
    GL11.glClearColor(0.5f, 0.5f, 0.5f, 1);
  }

  public void updateWindow() {
    GLFW.glfwSwapBuffers(windowId);
    GLFW.glfwPollEvents();
    calculateDelta();
  }

  public void resetViewport() {
    GL11.glViewport(0, 0, windowWidth, windowHeight);
  }

  private void calculateDelta() {
    long currentTime = System.nanoTime();
    delta = (currentTime - lastUpdateTime) * 0.0000000001;
    if (delta >= 0.25f)
      delta = 0;
    lastUpdateTime = currentTime;
  }

  public boolean shouldWindowClose() {
    return GLFW.glfwWindowShouldClose(windowId);
  }

  public void setTitle(String title) {
    this.title = title;
    GLFW.glfwSetWindowTitle(windowId, title);
  }

  public void setWindowWidth(int windowWidth) {
    this.windowWidth = windowWidth;
    applySizeChanges();
  }

  public void setWindowHeight(int windowHeight) {
    this.windowHeight = windowHeight;
    applySizeChanges();
  }

  public void setWindowSize(int windowWidth, int windowHeight) {
    this.windowWidth = windowWidth;
    this.windowHeight = windowHeight;
    applySizeChanges();
  }

  public void applySizeChanges() {
    GLFW.glfwSetWindowSize(windowId, windowWidth, windowHeight);
  }

  public void hideWindow() {
    GLFW.glfwHideWindow(windowId);
  }

  public void showWindow() {
    GLFW.glfwShowWindow(windowId);
  }

  public void closeWindow() {
    GLFW.glfwDestroyWindow(windowId);
  }

  public static void clearBuffers() {
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
  }

  public static Window createWindow(String title, boolean fullscreen, int windowWidth, int windowHeight) {
    long monitorPointer = GLFW.glfwGetPrimaryMonitor();
    GLFWVidMode videoMode = GLFW.glfwGetVideoMode(monitorPointer);
    if (videoMode == null)
      throw new IllegalStateException(new NullPointerException());
    GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 4);
    long windowId = GLFW.glfwCreateWindow(fullscreen ? videoMode.width() : windowWidth, fullscreen ? videoMode.height() : windowHeight, title, fullscreen ? monitorPointer : 0, 0);
    int xPosition = videoMode.width() / 2 - windowWidth / 2;
    int yPosition = videoMode.height() / 2 - windowHeight / 2;
    if (!fullscreen)
      GLFW.glfwSetWindowPos(windowId, xPosition, yPosition);
    return new Window(title, fullscreen, windowId, fullscreen ? videoMode.width() : windowWidth, fullscreen ? videoMode.height() : windowHeight);
  }

  public long windowId() {
    return windowId;
  }

  public double delta() {
    return delta;
  }

  public int windowWidth() {
    return windowWidth;
  }

  public int windowHeight() {
    return windowHeight;
  }
}
