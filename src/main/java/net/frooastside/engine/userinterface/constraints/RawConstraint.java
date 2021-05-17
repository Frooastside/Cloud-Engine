package net.frooastside.engine.userinterface.constraints;

import org.joml.Vector4f;

public class RawConstraint extends Constraint {

  public RawConstraint(float value) {
    super(value);
  }

  @Override
  public float calculate(Vector4f parent) {
    return value();
  }
}
