package net.frooastside.engine.animation;

import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class Joint {

  private final int index;
  private final String name;

  private final List<Joint> children = new ArrayList<>();

  private final Matrix4f offsetMatrix;
  private final Matrix4f animatedTransform = new Matrix4f();

  public Joint(int index, String name, Matrix4f offsetMatrix) {
    this.index = index;
    this.name = name;
    this.offsetMatrix = offsetMatrix;
  }

  public void addChild(Joint child) {
    this.children.add(child);
  }

  public int index() {
    return index;
  }

  public String name() {
    return name;
  }

  public List<Joint> children() {
    return children;
  }

  public Matrix4f offsetMatrix() {
    return offsetMatrix;
  }

  public Matrix4f animatedTransform() {
    return animatedTransform;
  }

}
