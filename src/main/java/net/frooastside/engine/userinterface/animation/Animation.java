package net.frooastside.engine.userinterface.animation;

import java.util.HashMap;
import java.util.Map;

public class Animation {

  private final Map<TransitionType, Transition> transitions = new HashMap<>();
  private float duration = 0;

  public Animation add(TransitionType transitionType, Transition transition) {
    transitions.put(transitionType, transition);
    duration = Math.max(duration, transition.duration());
    return this;
  }

  public Instance createInstance(Instance oldInstance, boolean reverse, float delay) {
    float transitionDuration = duration + delay;
    Map<TransitionType, TransitionDriver> drivers = new HashMap<>();
    for (Map.Entry<TransitionType, Transition> transition : transitions.entrySet()) {
      TransitionDriver driver = createDriver(transition.getKey(), transition.getValue(), oldInstance, reverse, delay);
      drivers.put(transition.getKey(), driver);
    }
    return new Instance(drivers, reverse, transitionDuration);
  }

  private TransitionDriver createDriver(TransitionType transitionType, Transition transition, Instance oldInstance, boolean reverse, float delay) {
    float standardValue = transitionType.getStandardValue();
    float currentValue = reverse ? transition.targetValue() : standardValue;
    if (oldInstance != null) {
      currentValue = oldInstance.currentValue(transitionType);
    }
    return transition.createDriver(standardValue, currentValue, reverse, delay, duration);
  }


  public static class Instance {

    private final Map<TransitionType, TransitionDriver> drivers;
    private final boolean reverse;
    private final float duration;

    private double time;

    public Instance(Map<TransitionType, TransitionDriver> drivers, boolean reverse, float duration) {
      this.drivers = drivers;
      this.reverse = reverse;
      this.duration = duration;
    }

    public void update(Animator animator, double delta) {
      boolean valueChanging = !transitionFinished();
      time += delta;
      for (Map.Entry<TransitionType, TransitionDriver> driver : drivers.entrySet()) {
        float value = driver.getValue().update(delta);
        driver.getKey().applyValue(animator, value, valueChanging);
      }
    }

    public float currentValue(TransitionType transitionType) {
      TransitionDriver driver = drivers.get(transitionType);
      return driver.currentValue();
    }

    public boolean transitionFinished() {
      return time > duration;
    }

    public boolean isRedundant() {
      return reverse && transitionFinished();
    }
  }

}
