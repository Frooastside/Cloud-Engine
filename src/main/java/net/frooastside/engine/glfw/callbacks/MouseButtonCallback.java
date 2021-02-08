package net.frooastside.engine.glfw.callbacks;

import net.frooastside.engine.glfw.Window;

public interface MouseButtonCallback {

  void invokeMouseButtonCallback(Window window, int key, boolean pressed);

}
