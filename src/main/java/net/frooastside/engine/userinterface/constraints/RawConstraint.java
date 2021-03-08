package net.frooastside.engine.userinterface.constraints;

public class RawConstraint extends Constraint {

  private float rawValue;

  public RawConstraint(float rawValue) {
    this.rawValue = rawValue;
  }

  @Override
  public float rawValue() {
    return rawValue;
  }

  @Override
  public void setValue(float value) {
    this.rawValue = value;
  }

  @Override
  public boolean relative() {
    return false;
  }
}
