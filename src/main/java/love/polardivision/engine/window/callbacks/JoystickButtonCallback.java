package love.polardivision.engine.window.callbacks;

import love.polardivision.engine.window.Controller;
import love.polardivision.engine.window.GamePadButton;

public interface JoystickButtonCallback {

  void invokeButtonCallback(Controller controller, GamePadButton button, boolean pressed);

}
