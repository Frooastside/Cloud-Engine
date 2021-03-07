package net.frooastside.engine.userinterface.animation;

import net.frooastside.engine.userinterface.elements.UiElement;
import org.joml.Vector4f;

public class UiAnimator {

  private final Vector4f offset = new Vector4f();
  private float alpha = 1.0f;

  private boolean recalculatePosition;
  private boolean recalculateSize;

  //private final UiElement uiElement;

  public UiAnimator(UiElement element) {

  }

  public void update(double delta) {
    reset();
    if (recalculatePosition) {

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
    this.offset.z += width;
    this.recalculateSize |= recalculate;
  }

  public void applyHeight(float height, boolean recalculate) {
    this.offset.w += height;
    this.recalculateSize |= recalculate;
  }

  public void applyAlpha(float alpha) {
    this.alpha = alpha;
  }

}
