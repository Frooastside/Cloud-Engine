package net.frooastside.engine.userinterface.animation;

public abstract class UiTransitionDriver {

  private final float startTime;
  private final float length;

  private double time = 0;

  private float currentValue;
  private boolean finishedFirstPeriod;

  public UiTransitionDriver(float length) {
    this.length = length;
    this.startTime = 0;
  }

  public UiTransitionDriver(float startTime, float length) {
    this.length = length;
    this.startTime = startTime;
  }

  public float update(double delta) {
    this.time += delta;
    if(time < startTime) {
      this.currentValue = calculateValue(0);
      return currentValue;
    }
    float endTime = startTime + length;
    if(time >= endTime) {
      time %= endTime;
      finishedFirstPeriod = true;
    }
    float relativeTime = (float) ((time - startTime) / length);
    this.currentValue = calculateValue(relativeTime);
    return currentValue;
  }

  public abstract float calculateValue(float time);

  public boolean finishedFirstPeriod() {
    return finishedFirstPeriod;
  }

  public float currentValue() {
    return currentValue;
  }
}
