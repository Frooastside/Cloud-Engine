package net.frooastside.engine.userinterface.constraints;

import net.frooastside.engine.userinterface.elements.Element;

public abstract class Constraint {

  private ElementConstraints constraints;
  private ConstraintType type;
  private Element current;
  private Element parent;

  public void initialize(ElementConstraints constraints, Element current, Element parent) {
    this.constraints = constraints;
    this.current = current;
    this.parent = parent;
  }

  public abstract float rawValue();

  public abstract void setValue(float value);

  public abstract boolean relative();

  protected Constraint getCounterpart() {
    return constraints.getCounterpart(this);
  }

  protected Constraint getOpposite() {
    return constraints.getOpposite(this);
  }

  public ElementConstraints constraints() {
    return constraints;
  }

  public ConstraintType type() {
    return type;
  }

  public void setConstraintType(ConstraintType type) {
    this.type = type;
  }

  public Element current() {
    return current;
  }

  public Element parent() {
    return parent;
  }

  public enum ConstraintType {

    X, Y, WIDTH, HEIGHT

  }
}
