package net.frooastside.engine.userinterface.animation;

public enum TransitionType {

  X(0, Animator::applyX),
  Y(0, Animator::applyY),
  WIDTH(1, Animator::applyWidth),
  HEIGHT(1, Animator::applyHeight),
  ALPHA(1, (animator, value, recalculate) -> animator.applyAlpha(value));

  private final float standardValue;
  private final ValueSetter valueSetter;

  TransitionType(float standardValue, ValueSetter valueSetter) {
    this.standardValue = standardValue;
    this.valueSetter = valueSetter;
  }

  public float getStandardValue() {
    return standardValue;
  }

  public void applyValue(Animator animator, float value, boolean recalculate) {
    valueSetter.applyValue(animator, value, recalculate);
  }

  private interface ValueSetter {

    void applyValue(Animator animator, float value, boolean recalculate);

  }

}
