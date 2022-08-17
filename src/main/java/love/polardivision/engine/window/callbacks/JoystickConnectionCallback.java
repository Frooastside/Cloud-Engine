package love.polardivision.engine.window.callbacks;

import love.polardivision.engine.window.Controller;

public interface JoystickConnectionCallback {

  void invokeConnectionCallback(Controller controller, boolean connected);

}
