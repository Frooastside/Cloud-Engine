package net.frooastside.engine.userinterface.constraints;

import net.frooastside.engine.userinterface.Constraint;

public class RelativeConstraint extends Constraint {

  private float relativeValue;

  public RelativeConstraint(float relativeValue) {
    this.relativeValue = relativeValue;
  }

  @Override
  public void recalculate() {
    setRawValue(relativeValue);
  }

  @Override
  public boolean relative() {
    return true;
  }

  public float relativeValue() {
    return relativeValue;
  }

  public void setRelativeValue(float relativeValue) {
    this.relativeValue = relativeValue;
  }
}
