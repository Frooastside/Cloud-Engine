package love.polardivision.engine.window.callbacks;

import love.polardivision.engine.window.Window;

public interface WindowSizeCallback {

  void invokeWindowSizeCallback(Window window, int newWidth, int newHeight);

}
