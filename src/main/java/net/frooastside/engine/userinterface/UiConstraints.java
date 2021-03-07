package net.frooastside.engine.userinterface;

import net.frooastside.engine.userinterface.constraints.RelativeConstraint;
import net.frooastside.engine.userinterface.elements.UiElement;
import org.joml.Vector2f;

public class UiConstraints {

  private final Vector2f pixelSize = new Vector2f();

  private UiConstraint x;
  private UiConstraint y;
  private UiConstraint width;
  private UiConstraint height;

  public UiConstraints(UiConstraint x, UiConstraint y, UiConstraint width, UiConstraint height) {
    setX(x);
    setY(y);
    setWidth(width);
    setHeight(height);
  }

  public UiConstraints() {
  }

  public void initialize(UiElement current, UiElement parent) {
    x.initialize(this, current, parent);
    y.initialize(this, current, parent);
    width.initialize(this, current, parent);
    height.initialize(this, current, parent);
  }

  public UiConstraint getConstraint(UiConstraint.ConstraintType constraintType) {
    if (constraintType == UiConstraint.ConstraintType.X) {
      return x;
    } else if (constraintType == UiConstraint.ConstraintType.Y) {
      return y;
    } else if (constraintType == UiConstraint.ConstraintType.WIDTH) {
      return width;
    } else if (constraintType == UiConstraint.ConstraintType.HEIGHT) {
      return height;
    }
    return null;
  }

  public UiConstraint getCounterpart(UiConstraint constraint) {
    if (constraint.type() == UiConstraint.ConstraintType.X) {
      return y;
    } else if (constraint.type() == UiConstraint.ConstraintType.Y) {
      return x;
    } else if (constraint.type() == UiConstraint.ConstraintType.WIDTH) {
      return height;
    } else if (constraint.type() == UiConstraint.ConstraintType.HEIGHT) {
      return width;
    }
    return null;
  }

  public UiConstraint getOpposite(UiConstraint constraint) {
    if (constraint.type() == UiConstraint.ConstraintType.X) {
      return width;
    } else if (constraint.type() == UiConstraint.ConstraintType.Y) {
      return height;
    } else if (constraint.type() == UiConstraint.ConstraintType.WIDTH) {
      return x;
    } else if (constraint.type() == UiConstraint.ConstraintType.HEIGHT) {
      return y;
    }
    return null;
  }

  public static UiConstraints getDefault() {
    return new UiConstraints(
      new RelativeConstraint(0),
      new RelativeConstraint(0),
      new RelativeConstraint(1),
      new RelativeConstraint(1));
  }

  public void setPixelSize(Vector2f pixelSize) {
    this.pixelSize.set(pixelSize);
  }

  public UiConstraint x() {
    return x;
  }

  public void setX(UiConstraint x) {
    this.x = x;
    this.x.setConstraintType(UiConstraint.ConstraintType.X);
  }

  public UiConstraint y() {
    return y;
  }

  public void setY(UiConstraint y) {
    this.y = y;
    this.y.setConstraintType(UiConstraint.ConstraintType.Y);
  }

  public UiConstraint width() {
    return width;
  }

  public void setWidth(UiConstraint width) {
    this.width = width;
    this.width.setConstraintType(UiConstraint.ConstraintType.WIDTH);
  }

  public UiConstraint height() {
    return height;
  }

  public void setHeight(UiConstraint height) {
    this.height = height;
    this.height.setConstraintType(UiConstraint.ConstraintType.HEIGHT);
  }

  public Vector2f pixelSize() {
    return pixelSize;
  }
}
