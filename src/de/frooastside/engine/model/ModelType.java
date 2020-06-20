package de.frooastside.engine.model;

import de.frooastside.engine.resource.FileType;

public enum ModelType {
	
	PLANT(FileType.MODELS + "/plant/"), ANIMAL(FileType.MODELS + "/animal/"), CHARACTER(FileType.MODELS + "/character/");
	
	public String path;
	
	ModelType(String path) {
		this.path = path;
	}
	
	@Override
	public String toString() {
		return this.name();
	}

}
