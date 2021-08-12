package net.frooastside.engine.userinterface.elements;

import net.frooastside.engine.userinterface.Color;
import net.frooastside.engine.userinterface.animation.Animation;
import net.frooastside.engine.userinterface.animation.Animator;
import org.joml.Vector2f;
import org.joml.Vector4f;

public abstract class Element {

  private final Vector4f bounds = new Vector4f(0, 0, 1, 1);
  private final Vector2f pixelSize = new Vector2f();

  private Animator animator;

  private Animation displayAnimation;
  private float displayAnimationDelay;

  private Color color;
  private float alpha = 1.0f;
  private boolean visible;

  public void update(double delta) {
    if (hasAnimator()) {
      animator.update(delta);
    }
  }

  public void updatePixelSize(Vector2f pixelSize) {
    this.pixelSize.set(pixelSize);
  }

  public void display(boolean show, float parentDelay) {
    if (show != visible) {
      this.visible = show;
      if (displayAnimation != null) {
        animator().applyAnimation(displayAnimation, show, parentDelay + displayAnimationDelay);
      }
    }
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

  public boolean doingDisplayAnimation() {
    return animator != null && animator.doingAnimation(displayAnimation);
  }

  public Vector4f bounds() {
    return bounds;
  }

  public Vector2f pixelSize() {
    return pixelSize;
  }

  public boolean hasAnimator() {
    return animator != null;
  }

  public Animator animator() {
    if (animator == null) {
      animator = new Animator(this);
    }
    return animator;
  }

  public Animation displayAnimation() {
    return displayAnimation;
  }

  public void setDisplayAnimation(Animation displayAnimation, float displayAnimationDelay) {
    this.displayAnimation = displayAnimation;
    this.displayAnimationDelay = displayAnimationDelay;
  }

  public float displayAnimationDelay() {
    return displayAnimationDelay;
  }

  public void setDisplayAnimationDelay(float displayAnimationDelay) {
    this.displayAnimationDelay = displayAnimationDelay;
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

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  public Color color() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

}
