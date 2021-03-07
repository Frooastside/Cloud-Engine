package net.frooastside.engine.userinterface;

import net.frooastside.engine.userinterface.elements.UiElement;

public abstract class UiConstraint {

  private UiConstraints constraints;
  private ConstraintType type;
  private UiElement current;
  private UiElement parent;

  public void initialize(UiConstraints constraints, UiElement current, UiElement parent) {
    this.constraints = constraints;
    this.current = current;
    this.parent = parent;
  }

  public abstract float rawValue();

  public abstract void setValue(float value);

  public abstract boolean relative();

  protected UiConstraint getCounterpart() {
    return constraints.getCounterpart(this);
  }

  protected UiConstraint getOpposite() {
    return constraints.getOpposite(this);
  }

  public UiConstraints constraints() {
    return constraints;
  }

  public ConstraintType type() {
    return type;
  }

  public void setConstraintType(ConstraintType type) {
    this.type = type;
  }

  public UiElement current() {
    return current;
  }

  public UiElement parent() {
    return parent;
  }

  public enum ConstraintType {

    X, Y, WIDTH, HEIGHT

  }
}
