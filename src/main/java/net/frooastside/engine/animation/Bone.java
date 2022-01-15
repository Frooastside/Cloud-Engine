package net.frooastside.engine.animation;

import org.joml.Matrix4f;

public class Bone {

  private final int index;
  private final Matrix4f bindTransformation;

  public Bone(int index, Matrix4f bindTransformation) {
    this.index = index;
    this.bindTransformation = bindTransformation;
  }

  public int index() {
    return index;
  }

  public Matrix4f bindTransformation() {
    return bindTransformation;
  }

}
