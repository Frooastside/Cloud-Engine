package de.frooastside.engine.model;

import de.frooastside.engine.texture.GLTexture;

public class TexturedModel {
	
	private RawModel rawModel;
	private GLTexture texture;
	
	public TexturedModel(RawModel rawModel, GLTexture texture) {
		this.rawModel = rawModel;
		this.texture = texture;
	}

	public RawModel getRawModel() {
		return rawModel;
	}

	public GLTexture getTexture() {
		return texture;
	}

}
