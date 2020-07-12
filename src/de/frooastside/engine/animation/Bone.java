package de.frooastside.engine.animation;

import org.joml.Matrix4f;

public class Bone {
	
	private int index;
	private Matrix4f bindTransformation;
	
	public Bone(int index, Matrix4f bindTransformation) {
		this.index = index;
		this.bindTransformation = bindTransformation;
	}

	public int getIndex() {
		return index;
	}

	public Matrix4f getBindTransformation() {
		return bindTransformation;
	}

}
