package de.frooastside.engine.animation;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;

public class Joint {
	
	private int index;
	private String name;
	private final List<Joint> children = new ArrayList<Joint>();

	private Matrix4f animatedTransform = new Matrix4f();
	
	private final Matrix4f offsetMatrix;
	
	public Joint(int index, String name, Matrix4f offsetMatrix) {
		this.index = index;
		this.name = name;
		this.offsetMatrix = offsetMatrix;
	}
	
	public void addChild(Joint child) {
		this.children.add(child);
	}
	
	public Matrix4f getAnimatedTransform() {
		return animatedTransform;
	}
	
	public void setAnimatedTransform(Matrix4f animatedTransform) {
		this.animatedTransform = animatedTransform;
	}
	
	public Matrix4f getOffsetMatrix() {
		return offsetMatrix;
	}

	public int getIndex() {
		return index;
	}

	public String getName() {
		return name;
	}

	public List<Joint> getChildren() {
		return children;
	}
	
}
