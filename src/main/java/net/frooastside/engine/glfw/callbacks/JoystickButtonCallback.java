package net.frooastside.engine.glfw.callbacks;

import net.frooastside.engine.glfw.Controller;

public interface JoystickButtonCallback {

  void invokeButtonCallback(Controller controller, int button, boolean pressed);

}
