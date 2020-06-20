package de.frooastside.engine.resource;

public enum FileExtension {
	
	TEXTURE_PNG(".png"), MODEL_OBJ(".obj"), MODEL_DAE(".dae"), ANIMATION_DAE(".dae"), ANIMATION_FBX(".fbx"), ANIMATION_LIBRARY(".al");
	
	public String extension;
	
	FileExtension(String extension) {
		this.extension = extension;
	}
	
	@Override
	public String toString() {
		return this.name();
	}

}
