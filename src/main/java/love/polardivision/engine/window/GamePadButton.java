/*
 * Copyright 2022 @Frooastside
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package love.polardivision.engine.window;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.glfw.GLFW;

public enum GamePadButton {

  BUTTON_CROSS(GLFW.GLFW_GAMEPAD_BUTTON_A),
  BUTTON_CIRCLE(GLFW.GLFW_GAMEPAD_BUTTON_B),
  BUTTON_SQUARE(GLFW.GLFW_GAMEPAD_BUTTON_X),
  BUTTON_TRIANGLE(GLFW.GLFW_GAMEPAD_BUTTON_Y),
  BUTTON_LEFT_BUMPER(GLFW.GLFW_GAMEPAD_BUTTON_LEFT_BUMPER),
  BUTTON_RIGHT_BUMPER(GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER),
  BUTTON_BACK(GLFW.GLFW_GAMEPAD_BUTTON_BACK),
  BUTTON_START(GLFW.GLFW_GAMEPAD_BUTTON_START),
  BUTTON_GUIDE(GLFW.GLFW_GAMEPAD_BUTTON_GUIDE),
  BUTTON_LEFT_THUMB(GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB),
  BUTTON_RIGHT_THUMB(GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_THUMB),
  BUTTON_DPAD_UP(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_UP),
  BUTTON_DPAD_RIGHT(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_RIGHT),
  BUTTON_DPAD_DOWN(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_DOWN),
  BUTTON_DPAD_LEFT(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_LEFT),
  BUTTON_UNKNOWN(-1);

  private static final Map<Integer, GamePadButton> GAME_PAD_BUTTONS = new HashMap<>();
  private final int value;

  GamePadButton(int value) {
    this.value = value;
  }

  static {
    Arrays.stream(values()).forEach(button -> GAME_PAD_BUTTONS.put(button.value, button));
  }

  public static GamePadButton of(int button) {
    return GAME_PAD_BUTTONS.getOrDefault(button, BUTTON_UNKNOWN);
  }

  public int value() {
    return value;
  }
}
