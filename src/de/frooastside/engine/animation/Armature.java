package de.frooastside.engine.animation;

import org.joml.Matrix4f;

public class Armature {
	
	private final Joint rootJoint;
	private Matrix4f[] jointMatrices;
	
	public Armature(Joint rootJoint, int jointCount) {
		this.rootJoint = rootJoint;
		jointMatrices = new Matrix4f[jointCount];
	}
	
	public Matrix4f[] getJointTransforms() {
		addJointsToArray(rootJoint, jointMatrices);
		return jointMatrices;
	}
	
	private void addJointsToArray(Joint joint, Matrix4f[] jointMatrices) {
		jointMatrices[joint.getIndex()] = joint.getAnimatedTransform();
		for (Joint childJoint : joint.getChildren()) {
			addJointsToArray(childJoint, jointMatrices);
		}
	}

	public Joint getRootJoint() {
		return rootJoint;
	}

}
