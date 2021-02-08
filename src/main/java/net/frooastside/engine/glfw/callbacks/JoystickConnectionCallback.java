package net.frooastside.engine.glfw.callbacks;

import net.frooastside.engine.glfw.Controller;

public interface JoystickConnectionCallback {

  void invokeConnectionCallback(Controller controller, boolean connected);

}
