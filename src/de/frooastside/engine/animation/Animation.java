package de.frooastside.engine.animation;

import java.io.Serializable;

public class Animation implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7519008570286479814L;
	
	private final String name;
	private final float length;
	private final KeyFrame[] keyFrames;
	
	public Animation(String name, float length, KeyFrame[] keyFrames) {
		this.name = name;
		this.keyFrames = keyFrames;
		this.length = length;
	}
	
	@Override
	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public float getLength() {
		return length;
	}

	public KeyFrame[] getKeyFrames() {
		return keyFrames;
	}

}
