package de.frooastside.engine.animation;

import de.frooastside.engine.GLFWManager;

public class EntityAnimator {
	
	private static final int MOVEMENT_ANIMATION_LENGTH = 41;
	
//	private Animator animator;
	
	private GLFWManager glfwManager;

//	private Animator horizontalAnimator;
//	private Animator verticalAnimator;
	
	private float animationTime = 0;
	
//	private Matrix4f emptyMatrix = new Matrix4f();
	
//	private Map<String, Matrix4f> finalPose = new HashMap<String, Matrix4f>();
	
	public EntityAnimator() {
//		horizontalAnimator = new Animator();
//		verticalAnimator = new Animator();
	}
	
	public void updateMovementAnimations(float horizontal, float vertical) {
		increaseAnimationTime();
		if(vertical != 0 && horizontal != 0) {
			if(vertical > 0) {
				if(horizontal > 0) {
					
				}else {
					
				}
			}else {
				if(horizontal > 0) {
					
				}else {
					
				}
			}
		}else if(vertical != 0) {
			if(vertical > 0) {
				
			}else {
				
			}
		}else if(horizontal != 0) {
			if(horizontal > 0) {
				
			}else {
				
			}
		}else {
			
		}
	}
	
	private void increaseAnimationTime() {
		animationTime += glfwManager.getDelta();
		if(animationTime > MOVEMENT_ANIMATION_LENGTH) {
			this.animationTime %= MOVEMENT_ANIMATION_LENGTH;
		}
	}
	
//	private void interpolateAnimations(Map<String, JointTransform> firstAnimation, Map<String, JointTransform> secondAnimation, float factor) {
//		if(factor == 0) {
//			for (String jointName : firstAnimation.keySet()) {
//				finalPose.put(jointName, firstAnimation.get(jointName).getLocalTransform());
//			}
//		}else if(factor == 1) {
//			for (String jointName : secondAnimation.keySet()) {
//				finalPose.put(jointName, secondAnimation.get(jointName).getLocalTransform());
//			}
//		}else {
//			for (String jointName : firstAnimation.keySet()) {
//				finalPose.put(jointName, JointTransform.interpolate(firstAnimation.get(jointName), secondAnimation.get(jointName), factor).getLocalTransform());
//			}
//		}
//	}
//	
//	private void applyPoseToJoints(Map<String, Matrix4f> currentPose, Joint joint, Matrix4f parentTransform) {
//		Matrix4f currentLocalTransform = currentPose.get(joint.getName());
//		if(currentLocalTransform != null) {
//			Matrix4f currentTransform = parentTransform.mul(currentLocalTransform, new Matrix4f());
//			for (Joint childJoint : joint.getChildren()) {
//				applyPoseToJoints(currentPose, childJoint, currentTransform);
//			}
//			currentTransform.mul(joint.getOffsetMatrix());
//			joint.setAnimatedTransform(currentTransform);
//		}
//	}

}
