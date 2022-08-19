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

import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;

public record Cursor(long address) {

  public static Cursor createCursor(GLFWImage image, Vector2i hotSpot) {
    return new Cursor(GLFW.glfwCreateCursor(image, hotSpot.x, hotSpot.y));
  }

  public static Cursor createStandardCursor(CursorShape shape) {
    return new Cursor(GLFW.glfwCreateStandardCursor(shape.value()));
  }

  public void destroy() {
    GLFW.glfwDestroyCursor(address());
  }

}
