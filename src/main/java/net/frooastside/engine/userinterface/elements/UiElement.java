package net.frooastside.engine.userinterface.elements;

import net.frooastside.engine.userinterface.UiConstraint;
import net.frooastside.engine.userinterface.UiConstraints;
import net.frooastside.engine.userinterface.animation.UiAnimation;
import net.frooastside.engine.userinterface.animation.UiAnimator;
import org.joml.Vector2f;
import org.joml.Vector4f;

public abstract class UiElement {

  private final Vector4f bounds = new Vector4f(0, 0, 1, 1);
  private final Vector2f pixelSize = new Vector2f();

  private UiConstraints constraints;
  private UiAnimator animator;
  private UiElement parent;

  private float alpha = 1.0f;
  private boolean visible;

  private UiAnimation displayAnimation;
  private float displayAnimationDelay;

  public void update(double delta) {
    if (animator != null) {
      animator.update(delta);
    }
  }

  public void updatePixelSize(Vector2f pixelSize) {
    this.pixelSize.set(pixelSize);
    constraints.setPixelSize(pixelSize);
  }

  public void recalculateBounds() {
    bounds.set(
      rawValueOf(UiConstraint.ConstraintType.X) + parent.bounds().x,
      rawValueOf(UiConstraint.ConstraintType.Y) + parent.bounds().y,
      rawValueOf(UiConstraint.ConstraintType.WIDTH),
      rawValueOf(UiConstraint.ConstraintType.HEIGHT));
  }

  public float rawValueOf(UiConstraint.ConstraintType constraintType) {
    return rawValueOf(constraints.getConstraint(constraintType), constraintType);
  }

  public float rawValueOf(UiConstraint constraint) {
    return rawValueOf(constraint, constraint.type());
  }

  public float rawValueOf(UiConstraint constraint, UiConstraint.ConstraintType constraintType) {
    float rawValue = constraint.relative() ?
      constraint.rawValue() *
        (constraintType == UiConstraint.ConstraintType.X || constraintType == UiConstraint.ConstraintType.WIDTH ?
          parent().bounds().z
          : parent().bounds().w)
      : constraint.rawValue();
    if(animator != null) {
      if(constraintType == UiConstraint.ConstraintType.X) {
        rawValue += animator.offset().x;
      }else if(constraintType == UiConstraint.ConstraintType.Y) {
        rawValue += animator.offset().y;
      }else if(constraintType == UiConstraint.ConstraintType.WIDTH) {
        rawValue *= animator.offset().z;
      }else {
        rawValue *= animator.offset().w;
      }
    }
    return rawValue;
  }

  public void display(boolean show, float parentDelay) {
    if (show != visible) {
      this.visible = show;
      if (displayAnimation != null) {
        animator().applyAnimation(displayAnimation, show, parentDelay + displayAnimationDelay);
      }
    }
  }

  public void initialize() {
  }

  public boolean isPixelInside(float x, float y) {
    float rawX = x * pixelSize.x;
    float rawY = y * pixelSize.y;
    return isInside(rawX, rawY);
  }

  public boolean isInside(float rawX, float rawY) {
    Vector4f bounds = bounds();
    float xMin = bounds.x;
    float yMin = bounds.y;
    float xMax = xMin + bounds.z;
    float yMax = yMin + bounds.w;
    return rawX <= xMax && rawX >= xMin && rawY <= yMax && rawY >= yMin;
  }

  public Vector2f pixelSize() {
    return pixelSize;
  }

  public UiElement parent() {
    return parent;
  }

  public void setParent(UiElement parent) {
    this.parent = parent;
  }

  public UiConstraints constraints() {
    return constraints;
  }

  public void setConstraints(UiConstraints constraints) {
    this.constraints = constraints;
  }

  public UiAnimator animator() {
    if (animator == null) {
      animator = new UiAnimator(this);
    }
    return animator;
  }

  public Vector4f bounds() {
    return bounds;
  }

  public float alpha() {
    return alpha * (animator != null ? animator.alpha() : 1);
  }

  public void setAlpha(float alpha) {
    this.alpha = alpha;
  }

  public UiAnimation displayAnimation() {
    return displayAnimation;
  }

  public float displayAnimationDelay() {
    return displayAnimationDelay;
  }

  public void setDisplayAnimation(UiAnimation displayAnimation, float displayAnimationDelay) {
    this.displayAnimation = displayAnimation;
    this.displayAnimationDelay = displayAnimationDelay;
  }
}
