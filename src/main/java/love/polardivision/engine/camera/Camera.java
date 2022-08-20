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
import org.joml.Matrix4f;
import org.joml.Vector3f;

public abstract class Camera {

  private final SizedObject frame;

  private final Matrix4f viewMatrix = new Matrix4f();
  private boolean moved = false;

  private final Matrix4f projectionMatrix = new Matrix4f();
  private boolean projectionMatrixChanged = false;

  private final Vector3f position = new Vector3f(0, 0, 0);
  private final Vector3f oldPosition = new Vector3f();

  private final Vector3f rotation = new Vector3f(0, 0, 0);
  private final Vector3f oldRotation = new Vector3f();

  private final Vector3f negativePosition = new Vector3f();


  public Camera(SizedObject frame) {
    this.frame = frame;
  }

  {
    recalculateProjectionMatrix();
    recalculateViewMatrix();
  }

  public void update() {
    setProjectionMatrixChanged(false);
    if (position.x != oldPosition.x || position.y != oldPosition.y || position.z != oldPosition.z ||
        rotation.x != oldRotation.x || rotation.y != oldRotation.y || rotation.z != oldRotation.z) {
      moved = true;
      recalculateViewMatrix();
      oldPosition.set(position);
      oldRotation.set(rotation);
    } else {
      moved = false;
    }
  }

  public abstract void recalculateProjectionMatrix();

  private void recalculateViewMatrix() {
    viewMatrix.identity();
    viewMatrix.rotateYXZ((float) Math.toRadians(rotation.x), (float) Math.toRadians(rotation.y - 180), (float) Math.toRadians(rotation.z));
    position.negate(negativePosition);
    viewMatrix.translate(negativePosition);
  }

  public SizedObject frame() {
    return frame;
  }

  public Matrix4f viewMatrix() {
    return viewMatrix;
  }

  public boolean moved() {
    return moved;
  }

  public Matrix4f projectionMatrix() {
    return projectionMatrix;
  }

  protected void setProjectionMatrixChanged(boolean projectionMatrixChanged) {
    this.projectionMatrixChanged = projectionMatrixChanged;
  }

  public boolean projectionMatrixChanged() {
    return projectionMatrixChanged;
  }

  public Vector3f position() {
    return position;
  }

  public Vector3f rotation() {
    return rotation;
  }
}
