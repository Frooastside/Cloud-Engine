package net.frooastside.engine.userinterface;

import net.frooastside.engine.userinterface.constraints.RelativeConstraint;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class ElementConstraints {

  private ElementConstraints parent;

  private Constraint x;
  private Constraint y;
  private Constraint width;
  private Constraint height;

  private final Vector4f bounds = new Vector4f(0, 0, 1, 1);

  private final Vector2f pixelSize = new Vector2f();

  public void recalculate(Vector2f pixelSize) {
    this.pixelSize.set(pixelSize);
    x.recalculate();
    y.recalculate();
    width.recalculate();
    height.recalculate();
    bounds.set(
      (x.relative() ? x.rawValue() * parent.bounds.z : x.rawValue()) + parent.bounds().x,
      (y.relative() ? y.rawValue() * parent.bounds.w : y.rawValue()) + parent.bounds().y,
      width.relative() ? (width.rawValue() * parent.bounds().z) : width.rawValue(),
      height.relative() ? (height.rawValue() * parent.bounds().w) : height.rawValue());
  }

  public Constraint getConstraint(Constraint.ConstraintType constraintType) {
    if (constraintType == Constraint.ConstraintType.X) {
      return x;
    } else if (constraintType == Constraint.ConstraintType.Y) {
      return y;
    } else if (constraintType == Constraint.ConstraintType.WIDTH) {
      return width;
    } else if (constraintType == Constraint.ConstraintType.HEIGHT) {
      return height;
    }
    return null;
  }

  public Constraint getCounterpart(Constraint constraint) {
    if (constraint.type() == Constraint.ConstraintType.X) {
      return y;
    } else if (constraint.type() == Constraint.ConstraintType.Y) {
      return x;
    } else if (constraint.type() == Constraint.ConstraintType.WIDTH) {
      return height;
    } else if (constraint.type() == Constraint.ConstraintType.HEIGHT) {
      return width;
    }
    return null;
  }

  public Constraint getOpposite(Constraint constraint) {
    if (constraint.type() == Constraint.ConstraintType.X) {
      return width;
    } else if (constraint.type() == Constraint.ConstraintType.Y) {
      return height;
    } else if (constraint.type() == Constraint.ConstraintType.WIDTH) {
      return x;
    } else if (constraint.type() == Constraint.ConstraintType.HEIGHT) {
      return y;
    }
    return null;
  }

  public static ElementConstraints getDefault() {
    ElementConstraints constraints = new ElementConstraints();
    constraints.setX(new RelativeConstraint(0));
    constraints.setY(new RelativeConstraint(0));
    constraints.setWidth(new RelativeConstraint(1));
    constraints.setHeight(new RelativeConstraint(1));
    return constraints;
  }

  public ElementConstraints parent() {
    return parent;
  }

  public void setParent(ElementConstraints parent) {
    this.parent = parent;
  }

  public Constraint x() {
    return x;
  }

  public void setX(Constraint x) {
    this.x = x;
    this.x.setConstraints(this, Constraint.ConstraintType.X);
  }

  public Constraint y() {
    return y;
  }

  public void setY(Constraint y) {
    this.y = y;
    this.y.setConstraints(this, Constraint.ConstraintType.Y);
  }

  public Constraint width() {
    return width;
  }

  public void setWidth(Constraint width) {
    this.width = width;
    this.width.setConstraints(this, Constraint.ConstraintType.WIDTH);
  }

  public Constraint height() {
    return height;
  }

  public void setHeight(Constraint height) {
    this.height = height;
    this.height.setConstraints(this, Constraint.ConstraintType.HEIGHT);
  }

  public Vector4f bounds() {
    return bounds;
  }

  public Vector2f pixelSize() {
    return pixelSize;
  }
}
