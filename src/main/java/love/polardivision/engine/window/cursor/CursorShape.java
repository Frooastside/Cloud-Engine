/*
 * Copyright © 2022-2023 @Frooastside
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package love.polardivision.engine.window.cursor;

import org.lwjgl.glfw.GLFW;

public enum CursorShape {

  ARROW(GLFW.GLFW_ARROW_CURSOR),
  I_BEAM(GLFW.GLFW_IBEAM_CURSOR),
  CROSS_HAIR(GLFW.GLFW_CROSSHAIR_CURSOR),
  POINTING_HAND(GLFW.GLFW_POINTING_HAND_CURSOR),
  H_RESIZE(GLFW.GLFW_RESIZE_EW_CURSOR),
  V_RESIZE(GLFW.GLFW_RESIZE_NS_CURSOR),
  RESIZE_TL_BR(GLFW.GLFW_RESIZE_NWSE_CURSOR),
  RESIZE_TR_BL(GLFW.GLFW_RESIZE_NESW_CURSOR),
  RESIZE_ALL(GLFW.GLFW_RESIZE_ALL_CURSOR),
  NOT_ALLOWED(GLFW.GLFW_NOT_ALLOWED_CURSOR);

  private final int value;

  CursorShape(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }
}
