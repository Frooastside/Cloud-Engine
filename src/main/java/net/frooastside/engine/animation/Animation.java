package net.frooastside.engine.animation;

import java.io.Serializable;

public class Animation implements Serializable {

  private static final long serialVersionUID = 7519008570286479814L;

  private final String name;
  private final float duration;
  private final KeyFrame[] keyFrames;

  public Animation(String name, float duration, KeyFrame[] keyFrames) {
    this.name = name;
    this.keyFrames = keyFrames;
    this.duration = duration;
  }

  public String name() {
    return name;
  }

  public float duration() {
    return duration;
  }

  public KeyFrame[] getKeyFrames() {
    return keyFrames;
  }
}
