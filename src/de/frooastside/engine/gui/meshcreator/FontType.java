package de.frooastside.engine.gui.meshcreator;

import java.io.File;

import de.frooastside.engine.Engine;
import de.frooastside.engine.gui.guielements.GuiText;
import de.frooastside.engine.resource.FileExtension;
import de.frooastside.engine.resource.FileType;
import de.frooastside.engine.texture.GLTexture;
import de.frooastside.engine.texture.TextureType;

public class FontType {

	private int textureAtlas;
	private TextMeshCreator loader;
	private File fontFile;
	
	public FontType(String fontFile) {
		GLTexture texture = new GLTexture(fontFile, TextureType.TEXTURE_FONT, FileExtension.TEXTURE_PNG, true, GLTexture.NO_FILTER);
		this.textureAtlas = texture.getTextureID();
		this.fontFile = new File(Engine.getEngine().getFilePath() + FileType.FONTS + "/" + fontFile + ".fnt");
		this.loader = new TextMeshCreator(this.fontFile);
	}
	
	public int getTextureAtlas() {
		return textureAtlas;
	}
	
	public TextMeshData loadText(GuiText text) {
		return loader.createTextMesh(text);
	}
	
	public void recalculateAspectRatio() {
		if(loader != null)
			loader.reload(fontFile);
	}

}
