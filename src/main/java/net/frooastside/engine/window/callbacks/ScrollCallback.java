package net.frooastside.engine.window.callbacks;

import net.frooastside.engine.window.Window;

public interface ScrollCallback {

  void invokeScrollCallback(Window window, double xOffset, double yOffset);

}
