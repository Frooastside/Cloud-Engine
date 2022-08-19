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

package love.polardivision.engine.glwrapper.query;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GL43;

public enum QueryType {

  SAMPLES_PASSED(GL15.GL_SAMPLES_PASSED),
  ANY_SAMPLES_PASSED(GL33.GL_ANY_SAMPLES_PASSED),
  ANY_SAMPLES_PASSED_CONSERVATIVE(GL43.GL_ANY_SAMPLES_PASSED_CONSERVATIVE),
  PRIMITIVES_GENERATED(GL30.GL_PRIMITIVES_GENERATED),
  TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN(GL30.GL_TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN),
  TIME_ELAPSED(GL33.GL_TIME_ELAPSED);

  private final int value;

  QueryType(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }
}
