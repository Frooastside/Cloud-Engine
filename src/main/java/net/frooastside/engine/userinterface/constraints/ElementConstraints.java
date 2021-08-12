package net.frooastside.engine.userinterface.constraints;

import net.frooastside.engine.language.I18n;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class ElementConstraints {

  private final Vector2f pixelSize = new Vector2f();

  private Constraint x;
  private Constraint y;
  private Constraint z;
  private Constraint w;

  public ElementConstraints(Constraint x, Constraint y, Constraint z, Constraint w) {
    setX(x);
    setY(y);
    setZ(z);
    setW(w);
  }

  public ElementConstraints(Constraint firstValue, Constraint secondValue, boolean setXY) {
    if(setXY) {
      setX(firstValue);
      setY(secondValue);
    }else {
      setZ(firstValue);
      setW(secondValue);
    }
  }

  public ElementConstraints() {
  }

  public Constraint constraint(Constraint.ConstraintType constraintType) {
    switch (constraintType) {
      case X:
        return x;
      case Y:
        return y;
      case Z:
        return z;
      case W:
        return w;
      default:
        throw new IllegalStateException(I18n.get("error.userinterface.unknownconstrainttype", constraintType));
    }
  }

  public Constraint.ConstraintType counterpartType(Constraint.ConstraintType constraintType) {
    switch (constraintType) {
      case X:
        return Constraint.ConstraintType.Y;
      case Y:
        return Constraint.ConstraintType.X;
      case Z:
        return Constraint.ConstraintType.W;
      case W:
        return Constraint.ConstraintType.Z;
      default:
        throw new IllegalStateException(I18n.get("error.userinterface.unknownconstrainttype", constraintType));
    }
  }

  public Constraint.ConstraintType oppositeType(Constraint.ConstraintType constraintType) {
    switch (constraintType) {
      case X:
        return Constraint.ConstraintType.Z;
      case Y:
        return Constraint.ConstraintType.W;
      case Z:
        return Constraint.ConstraintType.X;
      case W:
        return Constraint.ConstraintType.Y;
      default:
        throw new IllegalStateException(I18n.get("error.userinterface.unknownconstrainttype", constraintType));
    }
  }

  public float calculateValue(Constraint.ConstraintType constraintType, Vector4f parent) {
    return constraint(constraintType).calculate(parent);
  }

  public float calculateValue(Constraint constraint, Vector4f parent) {
    return constraint.calculate(parent);
  }

  public static ElementConstraints getDefault() {
    return new ElementConstraints(
      new RelativeConstraint(0),
      new RelativeConstraint(0),
      new RelativeConstraint(1),
      new RelativeConstraint(1));
  }

  public Vector2f pixelSize() {
    return pixelSize;
  }

  public void setPixelSize(Vector2f pixelSize) {
    this.pixelSize.set(pixelSize);
  }

  public Constraint x() {
    return x;
  }

  public void setX(Constraint x) {
    this.x = x;
    x.setConstraints(this);
    this.x.setType(Constraint.ConstraintType.X);
  }

  public Constraint y() {
    return y;
  }

  public void setY(Constraint y) {
    this.y = y;
    y.setConstraints(this);
    this.y.setType(Constraint.ConstraintType.Y);
  }

  public Constraint z() {
    return z;
  }

  public void setZ(Constraint z) {
    this.z = z;
    z.setConstraints(this);
    this.z.setType(Constraint.ConstraintType.Z);
  }

  public Constraint w() {
    return w;
  }

  public void setW(Constraint w) {
    this.w = w;
    w.setConstraints(this);
    this.w.setType(Constraint.ConstraintType.W);
  }
}
