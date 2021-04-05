package net.frooastside.engine.userinterface.constraints;

import net.frooastside.engine.language.I18n;
import org.joml.Vector4f;

public class CenterConstraint extends Constraint {

  @Override
  public float rawValue() {
    ConstraintType oppositeConstraintType = getOpposite().type();
    float oppositeRawValue = current().rawValueOf(getOpposite())
      + ((oppositeConstraintType == ConstraintType.X || oppositeConstraintType == ConstraintType.Y)
        ? (oppositeConstraintType == ConstraintType.X
          ? parent().bounds().x
          : parent().bounds().y)
        : 0);
    Vector4f parentBounds = parent().bounds();
    if (type() == ConstraintType.X || type() == ConstraintType.Y) {
      return (type() == ConstraintType.X ? parentBounds.z - oppositeRawValue : parentBounds.w - oppositeRawValue) / 2;
    } else {
      float offset = oppositeRawValue - (type() == ConstraintType.WIDTH ? parentBounds.x : parentBounds.y);
      return ((type() == ConstraintType.WIDTH) ? parentBounds.z : parentBounds.w) - (offset * 2);
    }
    //throw new IllegalStateException(I18n.get("error.userinterface.doubleCenter"));
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
