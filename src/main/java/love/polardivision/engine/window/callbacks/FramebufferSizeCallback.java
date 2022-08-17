package love.polardivision.engine.window.callbacks;

import love.polardivision.engine.window.Window;

public interface FramebufferSizeCallback {

  void invokeFramebufferSizeCallback(Window window, int newWidth, int newHeight);

}
