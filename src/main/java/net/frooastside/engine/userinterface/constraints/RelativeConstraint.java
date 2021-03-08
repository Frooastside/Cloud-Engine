package net.frooastside.engine.userinterface.constraints;

public class RelativeConstraint extends Constraint {

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
