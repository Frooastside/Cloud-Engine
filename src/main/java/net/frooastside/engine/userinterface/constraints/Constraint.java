package net.frooastside.engine.userinterface.constraints;

import org.joml.Vector4f;

public abstract class Constraint {

  private ElementConstraints constraints;
  private ConstraintType type;

  private float value;

  public Constraint(float value) {
    this.value = value;
  }

  public abstract float calculate(Vector4f parent);

  protected Constraint counterpart() {
    return constraints().constraint(constraints().counterpartType(type));
  }

  protected Constraint opposite() {
    return constraints().constraint(constraints().oppositeType(type));
  }

  public ElementConstraints constraints() {
    return constraints;
  }

  public void setConstraints(ElementConstraints constraints) {
    this.constraints = constraints;
  }

  public ConstraintType type() {
    return type;
  }

  public void setType(ConstraintType type) {
    this.type = type;
  }

  public float value() {
    return value;
  }

  public void setValue(float value) {
    this.value = value;
  }

  public enum ConstraintType {

    X, Y, Z, W

  }
}
