package net.frooastside.engine.userinterface.constraints;

import net.frooastside.engine.language.I18n;
import org.joml.Vector4f;

public class RatioConstraint extends Constraint {

  public RatioConstraint(float value) {
    super(value);
  }

  @Override
  public float calculate(Vector4f parent) {
    if (counterpart() instanceof RatioConstraint) {
      throw new IllegalStateException(I18n.get("error.userinterface.ratioofratio"));
    }
    float aspectRatio = ((type() == ConstraintType.X || type() == ConstraintType.Z)
      ? constraints().pixelSize().x / constraints().pixelSize().y
      : constraints().pixelSize().y / constraints().pixelSize().x);
    return counterpart().calculate(parent) * value() * aspectRatio;
  }
}
