package net.frooastside.engine.animation;

import org.joml.Matrix4f;

public class Armature {

  private final Joint rootJoint;
  private final Matrix4f[] jointTransforms;

  public Armature(Joint rootJoint, int jointCount) {
    this.rootJoint = rootJoint;
    jointTransforms = new Matrix4f[jointCount];
  }

  public Matrix4f[] jointTransforms() {
    storeJointTransforms(rootJoint, jointTransforms);
    return jointTransforms;
  }

  private void storeJointTransforms(Joint joint, Matrix4f[] jointMatrices) {
    jointMatrices[joint.index()] = joint.animatedTransform();
    for (Joint childJoint : joint.children()) {
      storeJointTransforms(childJoint, jointMatrices);
    }
  }

  public Joint rootJoint() {
    return rootJoint;
  }

}
