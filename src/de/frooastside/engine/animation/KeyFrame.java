package de.frooastside.engine.animation;

import java.io.Serializable;
import java.util.Map;

public class KeyFrame implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6336433699991102404L;
	
	public final float timeStamp;
	private final Map<String, JointTransform> pose;
	
	public KeyFrame(float timeStamp, Map<String, JointTransform> jointKeyFrames) {
		this.timeStamp = timeStamp;
		this.pose = jointKeyFrames;
	}
	
	@Override
	public String toString() {
		return "Timestamp: " + Math.round(timeStamp * 60);
	}
	
	public void addJointTransform(String jointName, JointTransform jointTransform) {
		pose.put(jointName, jointTransform);
	}
	
	protected float getTimeStamp() {
		return timeStamp;
	}
	
	protected Map<String, JointTransform> getJointKeyFrames() {
		return pose;
	}

}
