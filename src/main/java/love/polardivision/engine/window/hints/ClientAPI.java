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

package love.polardivision.engine.window.hints;

import org.lwjgl.glfw.GLFW;

public enum ClientAPI {

  NO_API(GLFW.GLFW_NO_API),
  OPENGL(GLFW.GLFW_OPENGL_API),
  OPENGL_ES(GLFW.GLFW_OPENGL_ES_API);

  private final int value;

  ClientAPI(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }

}
