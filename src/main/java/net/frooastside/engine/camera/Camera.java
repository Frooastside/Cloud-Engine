package net.frooastside.engine.camera;

import net.frooastside.engine.glfw.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

  public static final float Z_NEAR = 0.1f;
  public static final float Z_FAR = 1000.0f;
  public static final float HORIZONTAL_FOV = 90.0f;

  private final Window window;

  private final Matrix4f viewMatrix = new Matrix4f();
  private final Matrix4f perspectiveProjectionMatrix = new Matrix4f();
  private final Matrix4f orthographicProjectionMatrix = new Matrix4f();

  private final Vector3f position = new Vector3f(0, 15.0f, -15);
  private final Vector3f oldPosition = new Vector3f();

  private final Vector3f rotation = new Vector3f(0, 180, 0);
  private final Vector3f oldRotation = new Vector3f();

  private final Vector3f negativePosition = new Vector3f();

  private boolean moved = false;
  private boolean projectionMatrixChanged = false;

  private ICameraController oldController;
  private ICameraController currentController;

  private float progression = 0;
  private float speed;

  public Camera(Window window) {
    this.window = window;
    createOrthographicProjectionMatrix();
  }

  public void initialize() {
    createOrthographicProjectionMatrix();
    updateProjectionMatrix();
    updateViewMatrix();
  }

  public void updateProjectionMatrix() {
    int width = window.resolution().x;
    int height = window.resolution().y;
    float verticalFOV = (float) (2f * Math.atan(Math.tan(Math.toRadians(HORIZONTAL_FOV) / 2f) * ((float) height / (float) width)));
    perspectiveProjectionMatrix.identity().perspective(verticalFOV, (float) width / (float) height, Z_NEAR, Z_FAR);
    projectionMatrixChanged = true;
  }

  private void updateViewMatrix() {
    viewMatrix.identity();
    viewMatrix.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0));
    viewMatrix.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
    viewMatrix.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1));
    position.negate(negativePosition);
    viewMatrix.translate(negativePosition);
  }

  private void createOrthographicProjectionMatrix() {
    orthographicProjectionMatrix.identity();
    orthographicProjectionMatrix.m00(0.004f);
    orthographicProjectionMatrix.m01(0.0f);
    orthographicProjectionMatrix.m02(0.0f);
    orthographicProjectionMatrix.m03(0.0f);
    orthographicProjectionMatrix.m10(0.0f);
    orthographicProjectionMatrix.m11(0.004f);
    orthographicProjectionMatrix.m12(0.0f);
    orthographicProjectionMatrix.m13(0.0f);
    orthographicProjectionMatrix.m20(0.0f);
    orthographicProjectionMatrix.m21(0.0f);
    orthographicProjectionMatrix.m22((-2.0f) / (Z_FAR - Z_NEAR));
    orthographicProjectionMatrix.m23(0.0f);
    orthographicProjectionMatrix.m30(0.0f);
    orthographicProjectionMatrix.m31(0.0f);
    orthographicProjectionMatrix.m32(-((Z_FAR + Z_NEAR) / (Z_FAR - Z_NEAR)));
    orthographicProjectionMatrix.m33(1.0f);
  }

  public void update() {
    if(progression < 1) {
      progression += window.delta() * speed;
      if(oldController != null) {
        Vector3f oldControllerPosition = oldController.calculateCameraPosition();
        Vector3f oldControllerRotation = oldController.calculateCameraRotation();
        if(currentController != null) {
          Vector3f currentControllerPosition = currentController.calculateCameraPosition();
          Vector3f currentControllerRotation = currentController.calculateCameraRotation();
          oldControllerPosition.lerp(currentControllerPosition, progression, this.position);
          oldControllerRotation.lerp(currentControllerRotation, progression, this.rotation);
        }else {
          this.position.set(oldControllerPosition);
          this.rotation.set(oldControllerRotation);
        }
      }
    }else {
      progression = 1;
      if(currentController != null) {
        this.position.set(currentController.calculateCameraPosition());
        this.rotation.set(currentController.calculateCameraRotation());
      }else {
        if(oldController != null) {
          this.position.set(oldController.calculateCameraPosition());
          this.rotation.set(oldController.calculateCameraRotation());
        }
      }
    }
    if (position.x != oldPosition.x || position.y != oldPosition.y || position.z != oldPosition.z ||
      rotation.x != oldRotation.x || rotation.y != oldRotation.y || rotation.z != oldRotation.z) {
        moved = true;
        updateViewMatrix();
        oldPosition.set(position);
        oldRotation.set(rotation);
    } else {
      moved = false;
    }
  }

  public void setController(float speed, ICameraController controller) {
    this.speed = speed;
    this.progression = 0;
    if(currentController != null) {
      this.oldController = currentController;
    }
    this.currentController = controller;
  }

  public Matrix4f viewMatrix() {
    return viewMatrix;
  }

  public Matrix4f perspectiveProjectionMatrix() {
    return perspectiveProjectionMatrix;
  }

  public Matrix4f orthographicProjectionMatrix() {
    return orthographicProjectionMatrix;
  }

  public Vector3f position() {
    return position;
  }

  public Vector3f rotation() {
    return rotation;
  }

  public boolean moved() {
    return moved;
  }

  public boolean projectionMatrixChanged() {
    return projectionMatrixChanged;
  }
}
