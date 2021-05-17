package net.frooastside.engine.userinterface.constraints;

import org.joml.Vector4f;

public class MaxValueConstraint extends Constraint {

  public MaxValueConstraint() {
    super(Float.NaN);
  }

  @Override
  public float calculate(Vector4f parent) {
    return Float.NaN;
  }
}
