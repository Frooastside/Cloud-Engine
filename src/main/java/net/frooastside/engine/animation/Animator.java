package net.frooastside.engine.animation;

import net.frooastside.engine.window.Window;

import java.util.HashMap;
import java.util.Map;

public class Animator {

  private final Map<String, JointTransform> pose = new HashMap<>();
  private Animation currentAnimation;
  private KeyFrame lastAnimationPose;
  private float animationTime = 0;

  private final Window window;

  public Animator(Window window) {
    this.window = window;
  }

  public void doAnimation(Animation animation, float transitionTime) {
    if (currentAnimation != null) {
      setLastAnimationPose(transitionTime);
      this.animationTime = -transitionTime;
    } else {
      this.animationTime = 0;
    }
    this.currentAnimation = animation;
  }

  public void updateAnimations(float animationTime) {
    if (currentAnimation != null) {
      this.animationTime = animationTime;
      calculateAnimationPose();
    }
  }

  public void updateAnimations() {
    if (currentAnimation != null) {
      increaseAnimationTime();
      calculateAnimationPose();
    }
  }

  private void setLastAnimationPose(float transitionTime) {
    calculateAnimationPose();
    lastAnimationPose = new KeyFrame(-transitionTime, new HashMap<>(pose));
  }

  private void increaseAnimationTime() {
    animationTime += window.delta();
    if (animationTime > currentAnimation.duration()) {
      this.animationTime %= currentAnimation.duration();
    }
  }

  private void calculateAnimationPose() {
    KeyFrame[] frames = previousAndNextKeyFrame();
    float progression = calculateProgression(frames[0], frames[1]);
    interpolatePoses(frames[0], frames[1], progression, pose);
  }

  private float calculateProgression(KeyFrame previousFrame, KeyFrame nextFrame) {
    float totalTime = nextFrame.timestamp() - previousFrame.timestamp();
    float currentTime = animationTime - previousFrame.timestamp();
    return currentTime / totalTime;
  }

  private KeyFrame[] previousAndNextKeyFrame() {
    if (animationTime >= 0) {
      KeyFrame[] allFrames = currentAnimation.getKeyFrames();
      KeyFrame previousFrame = allFrames[0];
      KeyFrame nextFrame = allFrames[0];
      for (int i = 1; i < allFrames.length; i++) {
        nextFrame = allFrames[i];
        if (nextFrame.timestamp() > animationTime) {
          break;
        }
        previousFrame = allFrames[i];
      }
      return new KeyFrame[]{previousFrame, nextFrame};
    } else {
      return new KeyFrame[]{lastAnimationPose, currentAnimation.getKeyFrames()[0]};
    }
  }

  private void interpolatePoses(KeyFrame previousFrame, KeyFrame nextFrame, float progression, Map<String, JointTransform> pose) {
    if (progression < 0)
      progression = 0;
    if (progression > 1)
      progression = 1;
    pose.clear();
    for (String jointName : previousFrame.pose().keySet()) {
      JointTransform previousTransform = previousFrame.pose().get(jointName);
      JointTransform nextTransform = nextFrame.pose().get(jointName);
      JointTransform currentTransform = JointTransform.interpolate(previousTransform, nextTransform, progression);
      pose.put(jointName, currentTransform);
    }
  }

  public Map<String, JointTransform> pose() {
    return pose;
  }

}
