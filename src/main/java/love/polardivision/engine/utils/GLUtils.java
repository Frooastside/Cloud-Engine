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

package love.polardivision.engine.utils;

import org.lwjgl.opengl.GL11;

public class GLUtils {

  public static void adjustViewport(SizedObject sizedObject) {
    GL11.glViewport(0, 0, sizedObject.width(), sizedObject.height());
  }

  public static void clearDefault(){
    clearColorBuffer();
    clearDepthBuffer();
  }

  public static void clearAll() {
    clearColorBuffer();
    clearDepthBuffer();
    clearStencilBuffer();
  }

  public static void clearColorBuffer() {
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
  }

  public static void clearDepthBuffer() {
    GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
  }

  public static void clearStencilBuffer() {
    GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
  }

}
