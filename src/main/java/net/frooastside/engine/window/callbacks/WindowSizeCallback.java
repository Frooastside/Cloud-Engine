package net.frooastside.engine.window.callbacks;

import net.frooastside.engine.window.Window;

public interface WindowSizeCallback {

  void invokeWindowSizeCallback(Window window, int newWidth, int newHeight);

}
