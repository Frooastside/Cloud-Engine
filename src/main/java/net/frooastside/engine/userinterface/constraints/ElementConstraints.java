package net.frooastside.engine.userinterface.constraints;

import net.frooastside.engine.userinterface.elements.Element;
import org.joml.Vector2f;

public class ElementConstraints {

  private final Vector2f pixelSize = new Vector2f();

  private Constraint x;
  private Constraint y;
  private Constraint width;
  private Constraint height;

  public ElementConstraints(Constraint x, Constraint y, Constraint width, Constraint height) {
    setX(x);
    setY(y);
    setWidth(width);
    setHeight(height);
  }

  public ElementConstraints() {
  }

  public void initialize(Element current, Element parent) {
    x.initialize(this, current, parent);
    y.initialize(this, current, parent);
    width.initialize(this, current, parent);
    height.initialize(this, current, parent);
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
    return new ElementConstraints(
      new RelativeConstraint(0),
      new RelativeConstraint(0),
      new RelativeConstraint(1),
      new RelativeConstraint(1));
  }

  public void setPixelSize(Vector2f pixelSize) {
    this.pixelSize.set(pixelSize);
  }

  public Constraint x() {
    return x;
  }

  public void setX(Constraint x) {
    this.x = x;
    this.x.setConstraintType(Constraint.ConstraintType.X);
  }

  public Constraint y() {
    return y;
  }

  public void setY(Constraint y) {
    this.y = y;
    this.y.setConstraintType(Constraint.ConstraintType.Y);
  }

  public Constraint width() {
    return width;
  }

  public void setWidth(Constraint width) {
    this.width = width;
    this.width.setConstraintType(Constraint.ConstraintType.WIDTH);
  }

  public Constraint height() {
    return height;
  }

  public void setHeight(Constraint height) {
    this.height = height;
    this.height.setConstraintType(Constraint.ConstraintType.HEIGHT);
  }

  public Vector2f pixelSize() {
    return pixelSize;
  }
}
