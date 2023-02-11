/*
 * Copyright © 2023 @Frooastside
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package love.polardivision.engine.shader;

public class UniformLocationError extends Error {

  private final String uniform;
  private final int programId;

  public UniformLocationError(String uniform, int programId) {
    this.uniform = uniform;
    this.programId = programId;
  }

  public UniformLocationError(String message, String uniform, int programId) {
    super(message);
    this.uniform = uniform;
    this.programId = programId;
  }

  public UniformLocationError(String message, Throwable cause, String uniform, int programId) {
    super(message, cause);
    this.uniform = uniform;
    this.programId = programId;
  }

  public UniformLocationError(Throwable cause, String uniform, int programId) {
    super(cause);
    this.uniform = uniform;
    this.programId = programId;
  }

  public String getUniform() {
    return uniform;
  }

  public int getProgramId() {
    return programId;
  }
}
