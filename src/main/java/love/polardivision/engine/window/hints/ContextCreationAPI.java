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

public enum ContextCreationAPI {

  NATIVE_CONTEXT_API(GLFW.GLFW_NATIVE_CONTEXT_API),
  EGL_CONTEXT_API(GLFW.GLFW_EGL_CONTEXT_API),
  OS_MESA_CONTEXT_API(GLFW.GLFW_OSMESA_CONTEXT_API);

  private final int value;

  ContextCreationAPI(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }

}
