package net.frooastside.engine.window.callbacks;

import net.frooastside.engine.window.Window;
import org.lwjgl.glfw.GLFW;

public interface CharModifiersCallback {

  void invokeCharModifiersCallback(Window window, char codepoint, int modifiers);

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

}
