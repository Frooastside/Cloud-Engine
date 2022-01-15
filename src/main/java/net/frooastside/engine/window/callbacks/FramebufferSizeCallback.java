package net.frooastside.engine.window.callbacks;

import net.frooastside.engine.window.Window;

public interface FramebufferSizeCallback {

  void invokeFramebufferSizeCallback(Window window, int newWidth, int newHeight);

}
