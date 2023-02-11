/*
 * Copyright © 2022-2023 @Frooastside
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package love.polardivision.engine.camera;

import love.polardivision.engine.utils.SizedObject;

public class PerspectiveCamera extends Camera {

  private final float zNear;
  private final float zFar;
  private final float hFov;

  public PerspectiveCamera(SizedObject frame, float zNear, float zFar, float hFov) {
    super(frame);
    this.zNear = zNear;
    this.zFar = zFar;
    this.hFov = hFov;
  }

  @Override
  public void recalculateProjectionMatrix() {
    int width = frame().width();
    int height = frame().height();
    float verticalFOV = (float) (2f * Math.atan(Math.tan(Math.toRadians(hFov) / 2f) * ((float) height / (float) width)));
    projectionMatrix().identity().perspective(verticalFOV, (float) width / (float) height, zNear, zFar);
    setProjectionMatrixChanged(true);
  }

  public float zNear() {
    return zNear;
  }

  public float zFar() {
    return zFar;
  }

  public float hFov() {
    return hFov;
  }
}
