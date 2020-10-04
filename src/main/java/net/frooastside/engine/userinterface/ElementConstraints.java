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
      x.rawValue() + parent.bounds().x,
      y.rawValue() + parent.bounds().y,
      width.rawValue(),
      height.rawValue());
  }

  public Constraint getCounterpart(Constraint constraint) {
    if (constraint.equals(x)) {
      return y;
    } else if (constraint.equals(y)) {
      return x;
    } else if (constraint.equals(width)) {
      return height;
    } else if (constraint.equals(height)) {
      return width;
    }
    return null;
  }

  public Constraint getOpposite(Constraint constraint) {
    if (constraint.equals(x)) {
      return width;
    } else if (constraint.equals(y)) {
      return height;
    } else if (constraint.equals(width)) {
      return x;
    } else if (constraint.equals(height)) {
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
