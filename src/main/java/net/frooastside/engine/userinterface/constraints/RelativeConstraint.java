package net.frooastside.engine.userinterface.constraints;

import net.frooastside.engine.userinterface.Constraint;

public class RelativeConstraint extends Constraint {

  private float relativeValue;

  public RelativeConstraint(float relativeValue) {
    this.relativeValue = relativeValue;
  }

  @Override
  public void recalculate() {
    if (type() == ConstraintType.X) {
      setRawValue(relativeValue);
    } else if (type() == ConstraintType.Y) {
      setRawValue(relativeValue);
    } else if (type() == ConstraintType.WIDTH) {
      setRawValue(relativeValue * constraints().parent().bounds().z);
    } else if (type() == ConstraintType.HEIGHT) {
      setRawValue(relativeValue * constraints().parent().bounds().w);
    }
  }

  public float relativeValue() {
    return relativeValue;
  }

  public void setRelativeValue(float relativeValue) {
    this.relativeValue = relativeValue;
  }
}
