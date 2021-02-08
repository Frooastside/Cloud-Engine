package net.frooastside.engine.camera;

import org.joml.Vector3f;

public interface ICameraController {
	
	public Vector3f calculateCameraPosition();
	
	public Vector3f calculateCameraRotation();

}
