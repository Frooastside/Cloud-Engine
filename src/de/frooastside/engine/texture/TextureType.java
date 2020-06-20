package de.frooastside.engine.texture;

import de.frooastside.engine.resource.FileType;

public enum TextureType {
	
	TEXTURE_FONT(FileType.TEXTURES + "/font/"), TEXTURE_GUI(FileType.TEXTURES + "/gui/"), TEXTURE_MODEL_PLANT(FileType.TEXTURES + "/model/plant/"), TEXTURE_MODEL_ANIMAL(FileType.TEXTURES + "/model/animal/"), TEXTURE_MODEL_CHARACTER(FileType.TEXTURES + "/model/character/"), TEXTURE_TERRAIN(FileType.TEXTURES + "/terrain/");
	
	public String path;
	
	TextureType(String path) {
		this.path = path;
	}
	
	@Override
	public String toString() {
		return this.name();
	}

}
