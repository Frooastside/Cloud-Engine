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

package love.polardivision.engine.glwrapper.vertexarray;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;

public enum Primitive {

  POINTS(GL11.GL_POINTS),
  LINE_STRIP(GL11.GL_LINE_STRIP),
  LINE_LOOP(GL11.GL_LINE_LOOP),
  LINES(GL11.GL_LINES),
  LINE_STRIP_ADJACENCY(GL32.GL_LINE_STRIP_ADJACENCY),
  LINES_ADJACENCY(GL32.GL_LINES_ADJACENCY),
  TRIANGLE_STRIP(GL11.GL_TRIANGLE_STRIP),
  TRIANGLE_FAN(GL11.GL_TRIANGLE_FAN),
  TRIANGLES(GL11.GL_TRIANGLES),
  TRIANGLE_STRIP_ADJACENCY(GL32.GL_TRIANGLE_STRIP_ADJACENCY),
  TRIANGLES_ADJACENCY(GL32.GL_TRIANGLES_ADJACENCY),
  PATCHES(GL40.GL_PATCHES);

  private final int value;

  Primitive(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }
}
