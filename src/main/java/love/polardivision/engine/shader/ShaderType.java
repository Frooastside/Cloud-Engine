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

package love.polardivision.engine.shader;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;

public enum ShaderType {

  VERTEX_SHADER(GL20.GL_VERTEX_SHADER),
  TESSELATION_CONTROL_SHADER(GL40.GL_TESS_CONTROL_SHADER),
  TESSELATION_EVALUATION_SHADER(GL40.GL_TESS_EVALUATION_SHADER),
  GEOMETRY_SHADER(GL32.GL_GEOMETRY_SHADER),
  FRAGMENT_SHADER(GL20.GL_FRAGMENT_SHADER);

  private final int value;

  ShaderType(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }
}
