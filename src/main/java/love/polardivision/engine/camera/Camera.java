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

  public void initialize() {
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
    viewMatrix.rotateXYZ((float) Math.toRadians(rotation.x), (float) Math.toRadians(rotation.y - 180), (float) Math.toRadians(rotation.z));
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
