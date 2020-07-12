package de.frooastside.engine.animation;

import de.frooastside.engine.resource.FileType;

public enum AnimationType {
	
	ANIMATION_CHARACTER(FileType.ANIMATIONS + "/character/"), ANIMATION_ANIMAL(FileType.ANIMATIONS + "/animal/");
	
	public String path;
	
	AnimationType(String path) {
		this.path = path;
	}
	
	@Override
	public String toString() {
		return this.name();
	}

}
