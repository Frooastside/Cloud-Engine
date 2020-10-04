package net.frooastside.engine.userinterface;

public abstract class Constraint {

  private ConstraintType type;
  private ElementConstraints constraints;
  private float rawValue;

  public abstract void recalculate();

  protected Constraint getCounterpart() {
    return constraints.getCounterpart(this);
  }

  protected Constraint getOpposite() {
    return constraints.getOpposite(this);
  }

  public void setConstraints(ElementConstraints constraints, ConstraintType type) {
    this.constraints = constraints;
    this.type = type;
  }

  public ConstraintType type() {
    return type;
  }

  public ElementConstraints constraints() {
    return constraints;
  }

  public void setConstraints(ElementConstraints constraints) {
    this.constraints = constraints;
  }

  public float rawValue() {
    return rawValue;
  }

  public void setRawValue(float rawValue) {
    this.rawValue = rawValue;
  }

  public enum ConstraintType {

    X, Y, WIDTH, HEIGHT

  }
}
