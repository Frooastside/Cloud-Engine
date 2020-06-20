package de.frooastside.engine.camera;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import de.frooastside.engine.Engine;
import de.frooastside.engine.GLFWManager;
import de.frooastside.engine.util.Maths;

public class Camera {
	
	public static final float ZNEAR = 0.1f;
	public static final float ZFAR = 1000.0f;
//	public static final float HORIZONTAL_FOV = 90.0f;
	public static final float VERTICAL_FOV = 59.0f;
	public static Camera instance;
	
	private GLFWManager glfwManager;
	
	private Matrix4f viewMatrix;
	private Matrix4f perspectiveProjectionMatrix;
	private Matrix4f orthographicProjectionMatrix;
	
	private Vector3f position = new Vector3f(0, 15.0f, -15);
	private Vector3f negativePosition = new Vector3f();
	private float pitch = 45;
	private float yaw = 180;
	
	private Vector3f oldPosition = new Vector3f();
	private float oldPitch = 0;
	private float oldYaw = 0;
	
	private int renderDistance;
	
	private boolean moved = false;
	private boolean projectionMatrixReloadingRequestet;
	
	private ICameraController oldController;
	private ICameraController currentController;
	
	private float progression = 0;
	private float speed;
	
	public Camera(int renderDistance) {
		glfwManager = Engine.getEngine().getGlfwManager();
		this.renderDistance = renderDistance;
		createProjectionMatrix();
		createViewMatrix();
	}
	
	private void updateViewMatrix() {
		viewMatrix.identity();
		viewMatrix.rotate((float) Math.toRadians(pitch), new Vector3f(1, 0, 0));
		viewMatrix.rotate((float) Math.toRadians(yaw), new Vector3f(0, 1, 0));
		position.negate(negativePosition);
		viewMatrix.translate(negativePosition);
	}
	
	private void createViewMatrix() {
		viewMatrix = new Matrix4f();
    	viewMatrix.identity();
    	viewMatrix.rotate((float) Math.toRadians(90), new Vector3f(1, 0, 0));
    	viewMatrix.rotate((float) Math.toRadians(180), new Vector3f(0, 1, 0));
		position.negate(negativePosition);
		viewMatrix.translate(negativePosition);
	}
	
	private void createProjectionMatrix() {
		int width = glfwManager.getMainWindowWidth();
		int height = glfwManager.getMainWindowHeight();
		float aspectRatio = ((float) width) / ((float) height);
		perspectiveProjectionMatrix = new Matrix4f().perspective((float) Math.toRadians(VERTICAL_FOV), aspectRatio, ZNEAR, ZFAR);
		orthographicProjectionMatrix = new Matrix4f();
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
    	orthographicProjectionMatrix.m22((-2.0f) / (ZFAR - ZNEAR));
    	orthographicProjectionMatrix.m23(0.0f);
    	orthographicProjectionMatrix.m30(0.0f);
    	orthographicProjectionMatrix.m31(0.0f);
    	orthographicProjectionMatrix.m32(-((ZFAR + ZNEAR) / (ZFAR - ZNEAR)));
    	orthographicProjectionMatrix.m33(1.0f);
	}

	public void recalculateProjectionMatrix() {
		int width = glfwManager.getMainWindowWidth();
		int height = glfwManager.getMainWindowHeight();
		float aspectRatio = ((float) width) / ((float) height);
		perspectiveProjectionMatrix.identity();
		perspectiveProjectionMatrix.perspective((float) Math.toRadians(VERTICAL_FOV), aspectRatio, ZNEAR, ZFAR);
		updateViewMatrix();
		projectionMatrixReloadingRequestet = true;
	}
	
	public void update() {
		if(progression < 1) {
			progression += glfwManager.getDelta() * speed;
			if(oldController != null) {
				if(currentController != null) {
					this.position = Maths.mix(oldController.getCameraPosition(), currentController.getCameraPosition(), progression);
					Vector2f rotation = Maths.mix(oldController.getCameraRotation(), currentController.getCameraRotation(), progression);
					this.pitch = rotation.x;
					this.yaw = rotation.y;
				}else {
					this.position = oldController.getCameraPosition();
					Vector2f rotation = oldController.getCameraRotation();
					this.pitch = rotation.x;
					this.yaw = rotation.y;
				}
			}
		}else {
			progression = 1;
			if(currentController != null) {
				this.position = currentController.getCameraPosition();
				Vector2f rotation = currentController.getCameraRotation();
				this.pitch = rotation.x;
				this.yaw = rotation.y;
			}else {
				if(oldController != null) {
					this.position = oldController.getCameraPosition();
					Vector2f rotation = oldController.getCameraRotation();
					this.pitch = rotation.x;
					this.yaw = rotation.y;
				}
			}
		}
		if(position.x == oldPosition.x && position.y == oldPosition.y && position.z == oldPosition.z && pitch == oldPitch && yaw == oldYaw) {
			moved = false;
		}else {
			moved = true;
			updateViewMatrix();
			oldPosition = position;
			oldPitch = pitch;
			oldYaw = yaw;
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
	
	public static Camera createInstance() {
		instance = new Camera(5);
		return instance;
	}
	
	public static Camera getInstance() {
		return instance;
	}

	public Matrix4f getPerspectiveProjectionMatrix() {
		return perspectiveProjectionMatrix;
	}

	public boolean isProjectionMatrixReloadingRequestet() {
		return projectionMatrixReloadingRequestet;
	}

	public boolean isMoved() {
		return moved;
	}

	public Matrix4f getViewMatrix() {
		return viewMatrix;
	}

	public int getRenderDistance() {
		return renderDistance;
	}

}
