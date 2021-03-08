package net.frooastside.engine.userinterface.elements;

import net.frooastside.engine.userinterface.constraints.Constraint;
import net.frooastside.engine.userinterface.constraints.ElementConstraints;
import net.frooastside.engine.userinterface.animation.Animation;
import net.frooastside.engine.userinterface.animation.Animator;
import org.joml.Vector2f;
import org.joml.Vector4f;

public abstract class Element {

  private final Vector4f bounds = new Vector4f(0, 0, 1, 1);
  private final Vector2f pixelSize = new Vector2f();

  private ElementConstraints constraints;
  private Element parent;
  private Animator animator;

  private float alpha = 1.0f;
  private boolean visible;

  private Animation displayAnimation;
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
      rawValueOf(Constraint.ConstraintType.X) + parent.bounds().x,
      rawValueOf(Constraint.ConstraintType.Y) + parent.bounds().y,
      rawValueOf(Constraint.ConstraintType.WIDTH),
      rawValueOf(Constraint.ConstraintType.HEIGHT));
  }

  public float rawValueOf(Constraint.ConstraintType constraintType) {
    return rawValueOf(constraints.getConstraint(constraintType), constraintType);
  }

  public float rawValueOf(Constraint constraint) {
    return rawValueOf(constraint, constraint.type());
  }

  public float rawValueOf(Constraint constraint, Constraint.ConstraintType constraintType) {
    float rawValue = constraint.relative() ?
      constraint.rawValue() *
        (constraintType == Constraint.ConstraintType.X || constraintType == Constraint.ConstraintType.WIDTH ?
          parent().bounds().z
          : parent().bounds().w)
      : constraint.rawValue();
    if(animator != null) {
      if(constraintType == Constraint.ConstraintType.X) {
        rawValue += animator.offset().x;
      }else if(constraintType == Constraint.ConstraintType.Y) {
        rawValue += animator.offset().y;
      }else if(constraintType == Constraint.ConstraintType.WIDTH) {
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

  public Vector4f bounds() {
    return bounds;
  }

  public Vector2f pixelSize() {
    return pixelSize;
  }

  public ElementConstraints constraints() {
    return constraints;
  }

  public void setConstraints(ElementConstraints constraints) {
    this.constraints = constraints;
  }

  public Element parent() {
    return parent;
  }

  public void setParent(Element parent) {
    this.parent = parent;
  }

  public Animator animator() {
    if (animator == null) {
      animator = new Animator(this);
    }
    return animator;
  }

  public float alpha() {
    return alpha * (animator != null ? animator.alpha() : 1);
  }

  public void setAlpha(float alpha) {
    this.alpha = alpha;
  }

  public boolean visible() {
    return visible;
  }

  public Animation displayAnimation() {
    return displayAnimation;
  }

  public float displayAnimationDelay() {
    return displayAnimationDelay;
  }

  public void setDisplayAnimation(Animation displayAnimation, float displayAnimationDelay) {
    this.displayAnimation = displayAnimation;
    this.displayAnimationDelay = displayAnimationDelay;
  }
}
