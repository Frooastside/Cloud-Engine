package net.frooastside.engine.window.callbacks;

import net.frooastside.engine.window.Controller;

public interface JoystickConnectionCallback {

  void invokeConnectionCallback(Controller controller, boolean connected);

}
