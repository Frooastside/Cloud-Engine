package net.frooastside.engine.userinterface.constraints;

import org.joml.Vector4f;

public class PixelConstraint extends Constraint {

  public PixelConstraint(int pixels) {
    super(pixels);
  }

  @Override
  public float calculate(Vector4f parent) {
    if (type() == ConstraintType.X || type() == ConstraintType.Z) {
      return value() * constraints().pixelSize().x;
    } else {
      return value() * constraints().pixelSize().y;
    }
  }
}
