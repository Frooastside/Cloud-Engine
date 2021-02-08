package net.frooastside.engine.glfw.callbacks;

import net.frooastside.engine.glfw.Window;

public interface CharCallback {

  void invokeCharCallback(Window window, char codepoint);

}
