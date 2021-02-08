package net.frooastside.engine.glfw.callbacks;

import net.frooastside.engine.glfw.Window;

public interface FramebufferSizeCallback {

  void invokeFramebufferSizeCallback(Window window, int newWidth, int newHeight);

}
