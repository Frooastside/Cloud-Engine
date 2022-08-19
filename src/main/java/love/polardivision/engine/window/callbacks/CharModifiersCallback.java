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

package love.polardivision.engine.window.callbacks;

import love.polardivision.engine.window.Window;
import org.lwjgl.glfw.GLFW;

public interface CharModifiersCallback {

  void invokeCharModifiersCallback(Window window, char codepoint, int modifiers);

  enum Modifier {

    SHIFT(GLFW.GLFW_MOD_SHIFT),
    CONTROL(GLFW.GLFW_MOD_CONTROL),
    ALT(GLFW.GLFW_MOD_ALT),
    SUPER(GLFW.GLFW_MOD_SUPER),
    CAPS_LOCK(GLFW.GLFW_MOD_CAPS_LOCK),
    NUM_LOCK(GLFW.GLFW_MOD_NUM_LOCK);

    private final int flag;

    Modifier(int flag) {
      this.flag = flag;
    }

    public static boolean checkModifier(int modifiers, Modifier modifier) {
      return (modifiers & modifier.flag) == modifier.flag;
    }

  }

}
