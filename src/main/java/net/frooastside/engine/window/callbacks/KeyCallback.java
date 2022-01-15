package net.frooastside.engine.window.callbacks;

import net.frooastside.engine.window.Window;
import org.lwjgl.glfw.GLFW;

public interface KeyCallback {

  void invokeKeyCallback(Window window, int key, int scancode, int modifiers, Action action);

  enum Modifier {

    SHIFT(GLFW.GLFW_MOD_SHIFT),
    CONTROL(GLFW.GLFW_MOD_CONTROL),
    ALT(GLFW.GLFW_MOD_ALT),
    SUPER(GLFW.GLFW_MOD_SUPER),
    CAPS_LOCK(GLFW.GLFW_MOD_CAPS_LOCK),
    NUM_LOCK(GLFW.GLFW_MOD_NUM_LOCK);

    private final int flag;

    Modifier(int flag) {
      this.flag = flag;
    }

    public static boolean checkModifier(int modifiers, Modifier modifier) {
      return (modifiers & modifier.flag) == modifier.flag;
    }

  }

  enum Action {

    PRESS, RELEASE, REPEAT;

  }

}
