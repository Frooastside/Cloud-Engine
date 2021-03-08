package net.frooastside.engine.userinterface.animation.transitions;

import net.frooastside.engine.userinterface.animation.Transition;
import net.frooastside.engine.userinterface.animation.TransitionDriver;

public class SlideTransition implements Transition {

  private final float duration;
  private final float delay;
  private final float offset;

  public SlideTransition(float duration, float delay, float offset) {
    this.duration = duration;
    this.delay = delay;
    this.offset = offset;
  }

  @Override
  public TransitionDriver createDriver(float standardValue, float currentValue, boolean reverse, float totalDelay, float totalDuration) {
    float target = reverse ? standardValue : offset;
    float delay = reverse ? totalDuration - (this.delay + this.duration) : this.delay;
    return new SlideTransitionDriver(currentValue, target, duration, delay + totalDelay);
  }

  @Override
  public float duration() {
    return duration;
  }

  @Override
  public float targetValue() {
    return offset;
  }

  public static class SlideTransitionDriver extends TransitionDriver {

    private final float startValue;
    private final float endValue;

    public SlideTransitionDriver(float startValue, float endValue, float length, float delay) {
      super(delay, length);
      this.startValue = startValue;
      this.endValue = endValue;
    }

    @Override
    public float calculateValue(float time) {
      if (super.finishedFirstPeriod()) {
        return endValue;
      }
      return cosInterpolate(startValue, endValue, time);
    }

    private static float cosInterpolate(float a, float b, float blend) {
      double ft = blend * Math.PI;
      float f = (float) ((1f - Math.cos(ft)) * 0.5f);
      return a * (1 - f) + b * f;
    }
  }

}
