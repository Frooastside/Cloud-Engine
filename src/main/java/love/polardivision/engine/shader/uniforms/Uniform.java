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

package love.polardivision.engine.shader.uniforms;

import love.polardivision.engine.language.I18n;
import love.polardivision.engine.language.UsesTranslation;
import org.lwjgl.opengl.GL20;

public abstract class Uniform {

  private static final int NOT_FOUND = -1;

  private final String name;
  private int location;

  public Uniform(String name) {
    this.name = name;
  }

  @UsesTranslation("error.shader.uniformLocation")
  public void storeUniformLocation(int programId) {
    location = GL20.glGetUniformLocation(programId, name);
    if (location == NOT_FOUND) {
      throw new IllegalStateException(I18n.get("error.shader.uniformLocation", name, programId));
    }
  }

  public int location() {
    return location;
  }
}
