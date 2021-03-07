package net.frooastside.engine.userinterface.constraints;

import net.frooastside.engine.userinterface.UiConstraint;

public class RelativeConstraint extends UiConstraint {

  private float relativeValue;

  public RelativeConstraint(float relativeValue) {
    this.relativeValue = relativeValue;
  }

  @Override
  public float rawValue() {
    return relativeValue;
  }

  @Override
  public void setValue(float value) {
    this.relativeValue = value;
  }

  @Override
  public boolean relative() {
    return true;
  }
}
