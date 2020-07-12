package de.frooastside.engine.animation;

import java.util.HashMap;
import java.util.Map;

import de.frooastside.engine.Engine;
import de.frooastside.engine.GLFWManager;

public class Animator {
	
	private GLFWManager glfwManager;
	
	private Animation currentAnimation;
	private KeyFrame lastAnimationPose;
	private float animationTime = 0;
	
	private Map<String, JointTransform> pose = new HashMap<String, JointTransform>();
	
	public Animator() {
		glfwManager = Engine.getEngine().getGlfwManager();
	}
	
	public void doAnimation(Animation animation, float transitionTime) {
		if(currentAnimation != null) {
			setLastAnimationPose(transitionTime);
			this.animationTime = -transitionTime;
		}else {
			this.animationTime = 0;
		}
		this.currentAnimation = animation;
	}
	
	public void updateAnimations(float animationTime) {
		this.animationTime = animationTime;
		calculateAnimationPose();
	}
	
	public void updateAnimations() {
		if(currentAnimation != null) {
			increaseAnimationTime();
			calculateAnimationPose();
		}
	}
	
	private void setLastAnimationPose(float transitionTime) {
		calculateAnimationPose();
		lastAnimationPose = new KeyFrame(-transitionTime, new HashMap<String, JointTransform>(pose));
	}
	
	private void increaseAnimationTime() {
		animationTime += glfwManager.getDelta();
		if(animationTime > currentAnimation.getLength()) {
			this.animationTime %= currentAnimation.getLength();
		}
	}
	
	private void calculateAnimationPose() {
		KeyFrame[] frames = getPreviousAndNextKeyFrames();
		float progression = calculateProgression(frames[0], frames[1]);
		interpolatePoses(frames[0], frames[1], progression, pose);
	}
	
	private float calculateProgression(KeyFrame previousFrame, KeyFrame nextFrame) {
		float totalTime = nextFrame.getTimeStamp() - previousFrame.getTimeStamp();
		float currentTime = animationTime - previousFrame.getTimeStamp();
		return currentTime / totalTime;
	}
	
	private KeyFrame[] getPreviousAndNextKeyFrames() {
		if(animationTime >= 0) {
			KeyFrame[] allFrames = currentAnimation.getKeyFrames();
			KeyFrame previousFrame = allFrames[0];
			KeyFrame nextFrame = allFrames[0];
			for (int i = 1; i < allFrames.length; i++) {
				nextFrame = allFrames[i];
				if(nextFrame.getTimeStamp() > animationTime) {
					break;
				}
				previousFrame = allFrames[i];
			}
			return new KeyFrame[] {previousFrame, nextFrame};
		}else {
			return new KeyFrame[] {lastAnimationPose, currentAnimation.getKeyFrames()[0]};
		}
	}
	
	private void interpolatePoses(KeyFrame previousFrame, KeyFrame nextFrame, float progression, Map<String, JointTransform> pose) {
		if(progression < 0)
			progression = 0;
		if(progression > 1)
			progression = 1;
		pose.clear();
		for (String jointName : previousFrame.getJointKeyFrames().keySet()) {
			JointTransform previousTransform = previousFrame.getJointKeyFrames().get(jointName);
			JointTransform nextTransform = nextFrame.getJointKeyFrames().get(jointName);
			JointTransform currentTransform = JointTransform.interpolate(previousTransform, nextTransform, progression);
			pose.put(jointName, currentTransform);
		}
	}

	public Map<String, JointTransform> getPose() {
		return pose;
	}

}
