package love.polardivision.engine.window.callbacks;

import love.polardivision.engine.window.Window;

public interface ScrollCallback {

  void invokeScrollCallback(Window window, double xOffset, double yOffset);

}
