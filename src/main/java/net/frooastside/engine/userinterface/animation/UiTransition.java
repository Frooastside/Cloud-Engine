package net.frooastside.engine.userinterface.animation;

public interface UiTransition {

  UiTransitionDriver createDriver(float standardValue, float currentValue, boolean reverse, float delay, float duration);

  float duration();

  float targetValue();
}
