package net.frooastside.engine.userinterface.animation;

import net.frooastside.engine.userinterface.elements.Element;
import org.joml.Vector4f;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Animator {

  private final Map<Animation, Animation.Instance> animations = new HashMap<>();

  private final Vector4f offset = new Vector4f(0, 0, 1, 1);
  private float alpha = 1.0f;

  private boolean recalculatePosition;
  private boolean recalculateSize;

  private final Element element;

  public Animator(Element element) {
    this.element = element;
  }

  public void applyAnimation(Animation animation, boolean reverse, float delay) {
    Animation.Instance oldInstance = animations.remove(animation);
    Animation.Instance newInstance = animation.createInstance(oldInstance, reverse, delay);
    animations.put(animation, newInstance);
  }

  public void update(double delta) {
    reset();

    Iterator<Map.Entry<Animation, Animation.Instance>> iterator = animations.entrySet().iterator();
    while (iterator.hasNext()) {
      Animation.Instance animationInstance = iterator.next().getValue();
      animationInstance.update(this, delta);
      if (animationInstance.isRedundant()) {
        iterator.remove();
      }
    }

    if (recalculatePosition) {
      element.recalculateBounds();
    }
  }

  private void reset() {
    offset.set(0, 0, 1, 1);
    alpha = 1.0f;
    recalculatePosition = false;
    recalculateSize = false;
  }

  public void applyX(float x, boolean recalculate) {
    this.offset.x += x;
    this.recalculatePosition |= recalculate;
  }

  public void applyY(float y, boolean recalculate) {
    this.offset.y += y;
    this.recalculatePosition |= recalculate;
  }

  public void applyWidth(float width, boolean recalculate) {
    this.offset.z *= width;
    this.recalculatePosition |= recalculate;
    this.recalculateSize |= recalculate;
  }

  public void applyHeight(float height, boolean recalculate) {
    this.offset.w *= height;
    this.recalculatePosition |= recalculate;
    this.recalculateSize |= recalculate;
  }

  public Vector4f offset() {
    return offset;
  }

  public void applyAlpha(float alpha) {
    this.alpha = alpha;
  }

  public float alpha() {
    return alpha;
  }
}
