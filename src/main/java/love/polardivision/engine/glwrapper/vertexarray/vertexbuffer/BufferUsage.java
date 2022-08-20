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

public enum BufferUsage {

  STREAM_DRAW(GL15.GL_STREAM_DRAW),
  STREAM_READ(GL15.GL_STREAM_READ),
  STREAM_COPY(GL15.GL_STREAM_COPY),
  STATIC_DRAW(GL15.GL_STATIC_DRAW),
  STATIC_READ(GL15.GL_STATIC_READ),
  STATIC_COPY(GL15.GL_STATIC_COPY),
  DYNAMIC_DRAW(GL15.GL_DYNAMIC_DRAW),
  DYNAMIC_READ(GL15.GL_DYNAMIC_READ),
  DYNAMIC_COPY(GL15.GL_DYNAMIC_COPY);

  private final int value;

  BufferUsage(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }
}
