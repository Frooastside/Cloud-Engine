package net.frooastside.engine.userinterface.elements;

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
    if(animator != null) {
      animator.update(delta);
    }
  }

  public void updatePixelSize(Vector2f pixelSize) {
    this.pixelSize.set(pixelSize);
    constraints.setPixelSize(pixelSize);
  }

  public void recalculateBounds() {
    float x = constraints.x().rawValue() + (animator != null ? animator.offset().x : 0);
    float y = constraints.y().rawValue() + (animator != null ? animator.offset().y : 0);
    float width = constraints.width().rawValue() * (animator != null ? animator.offset().z : 1);
    float height = constraints.height().rawValue() * (animator != null ? animator.offset().w : 1);
    bounds.set(
      (constraints.x().relative() ? x * parent.bounds().z : x) + parent.bounds().x,
      (constraints.y().relative() ? y * parent.bounds().w : y) + parent.bounds().y,
      constraints.width().relative() ? (width * parent.bounds().z) : width,
      constraints.height().relative() ? (height * parent.bounds().w) : height);
  }

  public void display(boolean show, float parentDelay) {
    if(show != visible) {
      this.visible = show;
      if(displayAnimation != null) {
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
    if(animator == null) {
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
