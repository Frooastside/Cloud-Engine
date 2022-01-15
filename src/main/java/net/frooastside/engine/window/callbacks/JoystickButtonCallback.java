package net.frooastside.engine.window.callbacks;

import net.frooastside.engine.window.Controller;

public interface JoystickButtonCallback {

  void invokeButtonCallback(Controller controller, int button, boolean pressed);

}
