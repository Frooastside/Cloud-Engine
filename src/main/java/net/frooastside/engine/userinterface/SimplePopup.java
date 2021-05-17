package net.frooastside.engine.userinterface;

import net.frooastside.engine.glfw.Window;
import net.frooastside.engine.graphicobjects.Font;
import net.frooastside.engine.resource.ResourceFont;
import net.frooastside.engine.userinterface.elements.container.Screen;
import net.frooastside.engine.userinterface.renderer.BasicBoxShader;
import net.frooastside.engine.userinterface.renderer.BasicFontShader;
import net.frooastside.engine.userinterface.renderer.UserInterfaceRenderer;
import org.lwjgl.glfw.GLFW;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimplePopup {

  public static final Window.CreationHint UNDECORATED = new Window.CreationHint(GLFW.GLFW_DECORATED, 0);
  public static final Window.CreationHint FLOATING = new Window.CreationHint(GLFW.GLFW_FLOATING, 1);
  public static final Window.CreationHint MULTISAMPLING = new Window.CreationHint(GLFW.GLFW_SAMPLES, 4);

  private final ExecutorService executorService = Executors.newSingleThreadExecutor();
  private final Queue<Runnable> threadSpecificQueue = new ConcurrentLinkedQueue<>();

  private final ScreenCreator screenCreator;
  private final FontCreator fontCreator;
  private final String title;
  private final float windowScale;
  private final Window.CreationHint[] creationHints;

  private Thread thread;

  public SimplePopup(ScreenCreator screenCreator, FontCreator fontCreator, String title, float windowScale, Window.CreationHint... creationHints) {
    this.screenCreator = screenCreator;
    this.fontCreator = fontCreator;
    this.title = title;
    this.windowScale = windowScale;
    this.creationHints = creationHints;
  }

  public void display() {
    if (!running()) {
      thread = new Thread(this::open);
      thread.start();
    }
  }

  public void join() {
    try {
      thread.join();
    } catch (InterruptedException ignored) {
    }
  }

  public void destroy() {
    thread.interrupt();
  }

  public boolean running() {
    return thread != null && thread.isAlive();
  }

  public ExecutorService executorService() {
    return executorService;
  }

  public Queue<Runnable> threadSpecificQueue() {
    return threadSpecificQueue;
  }

  private void open() {
    ResourceFont resourceFont = fontCreator.createFont();
    resourceFont.addQueueTasks(executorService, threadSpecificQueue);
    Window window = Window.createWindow(Window.Monitor.DEFAULT, title, true, false, windowScale, creationHints);
    window.initialize();
    Screen screen = screenCreator.createScreen(window, resourceFont);
    window.input().setMouseButtonCallback(screen);
    window.input().setKeyCallback(screen);
    window.input().setCharCallback(screen);
    window.input().setFramebufferSizeCallback((eventWindow, newWidth, newHeight) -> {
      Window.resizeViewport(0, 0, newWidth, newHeight);
      screen.recalculateScreen();
    });
    screen.initialize();
    screen.recalculateScreen();

    UserInterfaceRenderer userInterfaceRenderer = new UserInterfaceRenderer(new BasicBoxShader(), new BasicFontShader());
    userInterfaceRenderer.initialize();

    screen.display(true, 0);

    while (!window.shouldWindowClose()) {
      screen.update();

      Runnable runnable = threadSpecificQueue.poll();
      if (runnable != null) {
        runnable.run();
      }
      Window.clearBuffers();
      userInterfaceRenderer.render(screen);
      window.updateWindow();
    }

    executorService.shutdown();

    screen.display(false, 0);

    while (screen.doingDisplayAnimation()) {
      screen.update();
      Window.clearBuffers();
      userInterfaceRenderer.render(screen);
      window.updateWindow();
    }
    userInterfaceRenderer.delete();
    window.closeWindow();
  }

  public interface ScreenCreator {

    Screen createScreen(Window window, Font font);

  }

  public interface FontCreator {

    ResourceFont createFont();

  }

}
