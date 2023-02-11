/*
 * Copyright © 2022-2023 @Frooastside
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package love.polardivision.engine.window.callbacks;

import love.polardivision.engine.window.Key;
import love.polardivision.engine.window.Window;
import org.lwjgl.glfw.GLFW;

public interface KeyCallback {

  void invokeKeyCallback(Window window, Key key, int scancode, int modifiers, Action action);

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

  enum Action {

    PRESS, RELEASE, REPEAT

  }

}
