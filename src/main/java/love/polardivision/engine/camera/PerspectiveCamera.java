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
