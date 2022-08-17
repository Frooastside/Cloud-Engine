package love.polardivision.engine.window.callbacks;

import love.polardivision.engine.window.MouseButton;
import love.polardivision.engine.window.Window;

public interface MouseButtonCallback {

  void invokeMouseButtonCallback(Window window, MouseButton key, boolean pressed);

}
