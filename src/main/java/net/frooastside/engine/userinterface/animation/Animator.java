package net.frooastside.engine.userinterface.animation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.joml.Vector4f;

public class Animator {

  private final Map<Animation, Animation.Instance> animations = new HashMap<>();

  private final Vector4f offset = new Vector4f(0, 0, 1, 1);
  private float alpha = 1.0f;

  public void applyAnimation(Animation animation, boolean reverse, float delay) {
    Animation.Instance oldInstance = animations.remove(animation);
    Animation.Instance newInstance = animation.createInstance(oldInstance, reverse, delay);
    animations.put(animation, newInstance);
  }

  public void cancelAnimation(Animation animation) {
    animations.remove(animation);
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
  }

  private void reset() {
    offset.set(0, 0, 1, 1);
    alpha = 1.0f;
  }

  public boolean doingAnimation(Animation animation) {
    Animation.Instance animationInstance = animations.get(animation);
    return animationInstance != null && !animationInstance.transitionFinished();
  }

  public void applyX(float x) {
    this.offset.x += x;
  }

  public void applyY(float y) {
    this.offset.y += y;
  }

  public void applyWidth(float width) {
    this.offset.z *= width;
  }

  public void applyHeight(float height) {
    this.offset.w *= height;
  }

  public void applyAlpha(float alpha) {
    this.alpha = alpha;
  }

  public Vector4f offset() {
    return offset;
  }

  public float alpha() {
    return alpha;
  }
}
