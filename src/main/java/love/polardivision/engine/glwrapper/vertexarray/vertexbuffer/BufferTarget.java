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

package love.polardivision.engine.glwrapper.vertexarray.vertexbuffer;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL21;

public enum BufferTarget {

  ARRAY_BUFFER(GL15.GL_ARRAY_BUFFER),
  ELEMENT_ARRAY_BUFFER(GL15.GL_ELEMENT_ARRAY_BUFFER),
  PIXEL_PACK_BUFFER(GL21.GL_PIXEL_PACK_BUFFER),
  PIXEL_UNPACK_BUFFER(GL21.GL_PIXEL_PACK_BUFFER);

  private final int value;

  BufferTarget(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }
}
