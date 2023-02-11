/*
 * Copyright © 2022-2023 @Frooastside
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package love.polardivision.engine.window;

import love.polardivision.engine.window.callbacks.JoystickButtonCallback;
import love.polardivision.engine.window.callbacks.JoystickConnectionCallback;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGamepadState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller {

  private static final Map<Integer, Controller> AVAILABLE_CONTROLLERS = new HashMap<>();
  private static final Map<Integer, Controller> AVAILABLE_GAME_PADS = new HashMap<>();
  private static final List<JoystickConnectionCallback> CONNECTION_CALLBACKS = new ArrayList<>(1);

  private final boolean[] gamePadButtons = new boolean[GLFW.GLFW_GAMEPAD_BUTTON_LAST + 1];
  private final GLFWGamepadState gamePadState = GLFWGamepadState.create();

  private final int identifier;

  private JoystickButtonCallback joystickButtonCallback;

  private final Vector2f leftAxis = new Vector2f();
  private final Vector2f rightAxis = new Vector2f();
  private float leftTrigger;
  private float rightTrigger;

  private float deadZone = 0.15f;

  private boolean inputChanged;

  private Controller(int identifier) {
    this.identifier = identifier;
  }

  static {
    Input.setControllerModuleCallback((jid, event) -> {
      if (event == GLFW.GLFW_CONNECTED) {
        if (!AVAILABLE_CONTROLLERS.containsKey(jid)) {
          Controller controller = new Controller(jid);
          if (controller.isGamePad()) {
            AVAILABLE_GAME_PADS.put(jid, controller);
          }
          AVAILABLE_CONTROLLERS.put(jid, controller);
          CONNECTION_CALLBACKS.forEach(callback -> callback.invokeConnectionCallback(controller, true));
        }
      } else {
        Controller controller = AVAILABLE_CONTROLLERS.remove(jid);
        AVAILABLE_GAME_PADS.remove(jid);
        CONNECTION_CALLBACKS.forEach(callback -> callback.invokeConnectionCallback(controller, false));
      }

    });
    for (int i = 0; i <= GLFW.GLFW_JOYSTICK_LAST; i++) {
      if (GLFW.glfwJoystickPresent(i)) {
        Controller controller = new Controller(i);
        if (controller.isGamePad()) {
          AVAILABLE_GAME_PADS.put(i, controller);
        }
        AVAILABLE_CONTROLLERS.put(i, controller);
      }
    }
  }

  public void update() {
    inputChanged = false;
    boolean state = GLFW.glfwGetGamepadState(identifier, gamePadState);
    if (state) {
      updateButtons();
      updateAxes();
    }
  }

  private void updateButtons() {
    for (int i = 0; i <= GLFW.GLFW_GAMEPAD_BUTTON_LAST; i++) {
      boolean pressed = gamePadState.buttons(i) == GLFW.GLFW_PRESS;
      if (gamePadButtons[i] != pressed) {
        gamePadButtons[i] = pressed;
        inputChanged = true;
        if (joystickButtonCallback != null) {
          joystickButtonCallback.invokeButtonCallback(this, GamePadButton.of(i), pressed);
        }
      }
    }
  }

  private void updateAxes() {
    float leftX = gamePadState.axes(GLFW.GLFW_GAMEPAD_AXIS_LEFT_X);
    float leftY = gamePadState.axes(GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y);
    float rightX = gamePadState.axes(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X);
    float rightY = gamePadState.axes(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y);
    float leftTrigger = gamePadState.axes(GLFW.GLFW_GAMEPAD_AXIS_LEFT_TRIGGER);
    float rightTrigger = gamePadState.axes(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER);

    if (leftX >= deadZone || leftX <= -deadZone) {
      inputChanged = true;
      leftAxis.x = leftX;
    } else {
      leftAxis.x = 0;
    }
    if (leftY >= deadZone || leftY <= -deadZone) {
      inputChanged = true;
      leftAxis.y = leftY;
    } else {
      leftAxis.y = 0;
    }
    if (rightX >= deadZone || rightX <= -deadZone) {
      inputChanged = true;
      rightAxis.x = rightX;
    } else {
      rightAxis.x = 0;
    }
    if (rightY >= deadZone || rightY <= -deadZone) {
      inputChanged = true;
      rightAxis.y = rightY;
    } else {
      rightAxis.y = 0;
    }
    if (leftTrigger >= deadZone) {
      inputChanged = true;
      this.leftTrigger = leftTrigger;
    } else {
      this.leftTrigger = 0;
    }
    if (rightTrigger >= deadZone) {
      inputChanged = true;
      this.rightTrigger = rightTrigger;
    } else {
      this.rightTrigger = 0;
    }
  }

  public static Map<Integer, Controller> availableGamePads() {
    return AVAILABLE_GAME_PADS;
  }

  public static Map<Integer, Controller> availableControllers() {
    return AVAILABLE_CONTROLLERS;
  }

  public static void addConnectionCallback(JoystickConnectionCallback callback) {
    CONNECTION_CALLBACKS.add(callback);
  }

  public static void removeConnectionCallback(JoystickConnectionCallback callback) {
    CONNECTION_CALLBACKS.remove(callback);
  }

  public float deadZone() {
    return deadZone;
  }

  public void setDeadZone(float deadZone) {
    this.deadZone = deadZone;
  }

  public String name() {
    return GLFW.glfwGetJoystickName(identifier);
  }

  public boolean isGamePad() {
    return GLFW.glfwJoystickIsGamepad(identifier);
  }

  public void setJoystickButtonCallback(JoystickButtonCallback joystickButtonCallback) {
    this.joystickButtonCallback = joystickButtonCallback;
  }

  public boolean buttonPressed(int button) {
    return gamePadButtons[button];
  }

  public boolean inputChanged() {
    return inputChanged;
  }

  public Vector2f leftAxis() {
    return leftAxis;
  }

  public Vector2f rightAxis() {
    return rightAxis;
  }

  public float leftTrigger() {
    return leftTrigger;
  }

  public float rightTrigger() {
    return rightTrigger;
  }

}
