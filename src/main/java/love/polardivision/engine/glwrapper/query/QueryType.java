/*
 * Copyright © 2022-2023 @Frooastside
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
