package net.frooastside.engine.userinterface.constraints;

import net.frooastside.engine.userinterface.Constraint;

public class RawConstraint extends Constraint {

  public RawConstraint(float rawValue) {
    setRawValue(rawValue);
  }

  @Override
  public void recalculate() {
  }

  @Override
  public boolean relative() {
    return false;
  }
}
