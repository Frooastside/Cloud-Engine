package net.frooastside.engine.userinterface.constraints;

import net.frooastside.engine.language.I18n;
import net.frooastside.engine.userinterface.UiConstraint;
import org.joml.Vector4f;

public class CenterConstraint extends UiConstraint {

  @Override
  public float rawValue() {
    UiConstraint oppositeConstraint = getOpposite();
    if (!(oppositeConstraint instanceof CenterConstraint)) {
      float oppositeRawValue = oppositeConstraint.rawValue();
      Vector4f parentBounds = parent().bounds();
      if (type() == ConstraintType.X || type() == ConstraintType.Y) {
        return (type() == ConstraintType.X ? parentBounds.z - oppositeRawValue : parentBounds.w - oppositeRawValue) / 2;
      } else {
        float offset = oppositeRawValue - (type() == ConstraintType.WIDTH ? parentBounds.x : parentBounds.y);
        return ((type() == ConstraintType.WIDTH) ? parentBounds.z : parentBounds.w) - (offset * 2);
      }
    } else {
      throw new IllegalStateException(I18n.get("error.userinterface.doubleCenter"));
    }
  }

  @Override
  public void setValue(float value) {
    throw new IllegalStateException(I18n.get("error.userinterface.centerValue"));
  }

  @Override
  public boolean relative() {
    return false;
  }
}
