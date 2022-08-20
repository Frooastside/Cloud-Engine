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
