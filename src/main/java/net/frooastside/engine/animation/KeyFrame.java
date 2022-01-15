package net.frooastside.engine.animation;

import java.io.Serializable;
import java.util.Map;

public class KeyFrame implements Serializable {

  private static final long serialVersionUID = 6336433699991102404L;

  public final float timestamp;
  private final Map<String, JointTransform> pose;

  public KeyFrame(float timestamp, Map<String, JointTransform> pose) {
    this.timestamp = timestamp;
    this.pose = pose;
  }

  public void addJointTransform(String jointName, JointTransform jointTransform) {
    pose.put(jointName, jointTransform);
  }

  protected float timestamp() {
    return timestamp;
  }

  public Map<String, JointTransform> pose() {
    return pose;
  }

}
