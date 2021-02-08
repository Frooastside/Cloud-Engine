package net.frooastside.engine.glfw.callbacks;

import net.frooastside.engine.glfw.Window;

public interface KeyCallback {

  void invokeKeyCallback(Window window, int key, int scancode, Modifier modifier, Action buttonState);

  enum Modifier {

    SHIFT, CONTROL, ALT, SUPER, CAPS_LOCK, NUM_LOCK;

  }

  enum Action {

    PRESS, RELEASE, REPEAT;

  }

}
