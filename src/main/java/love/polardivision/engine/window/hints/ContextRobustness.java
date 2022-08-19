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

public enum ContextRobustness {

  NO_ROBUSTNESS(GLFW.GLFW_NO_ROBUSTNESS),
  NO_RESET_NOTIFICATION(GLFW.GLFW_NO_RESET_NOTIFICATION),
  LOSE_CONTEXT_ON_RESET(GLFW.GLFW_LOSE_CONTEXT_ON_RESET);

  private final int value;

  ContextRobustness(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }

}
