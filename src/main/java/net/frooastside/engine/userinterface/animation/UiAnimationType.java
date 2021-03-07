package net.frooastside.engine.userinterface.animation;

public enum UiAnimationType {

  X(0, UiAnimator::applyX),
  Y(0, UiAnimator::applyY),
  WIDTH(1, UiAnimator::applyWidth),
  HEIGHT(1, UiAnimator::applyHeight),
  ALPHA(1, (animator, value, recalculate) -> animator.applyAlpha(value));

  private final float standardValue;
  private final ValueSetter valueSetter;

  UiAnimationType(float standardValue, ValueSetter valueSetter) {
    this.standardValue = standardValue;
    this.valueSetter = valueSetter;
  }

  public float getStandardValue() {
    return standardValue;
  }

  public void applyValue(UiAnimator animator, float value, boolean recalculate) {
    valueSetter.applyValue(animator, value, recalculate);
  }

  private interface ValueSetter {

    void applyValue(UiAnimator animator, float value, boolean recalculate);

  }

}
