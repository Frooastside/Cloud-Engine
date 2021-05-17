package net.frooastside.engine.userinterface.constraints;

import org.joml.Vector4f;

public class RelativeConstraint extends Constraint {

  public RelativeConstraint(float relativeValue) {
    super(relativeValue);
  }

  @Override
  public float calculate(Vector4f parent) {
    return value() *
      (type() == Constraint.ConstraintType.X || type() == Constraint.ConstraintType.Z
        ? parent.z
        : parent.w);
  }
}
