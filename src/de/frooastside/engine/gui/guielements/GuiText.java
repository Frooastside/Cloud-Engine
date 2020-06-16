package de.frooastside.engine.gui.guielements;

import de.frooastside.engine.gui.GuiElement;
import de.frooastside.engine.gui.VaoData;
import de.frooastside.engine.gui.VaoLoader;
import de.frooastside.engine.gui.meshcreator.FontType;
import de.frooastside.engine.gui.meshcreator.TextMeshData;

public class GuiText extends GuiElement {
	
	private String text;
	private FontType font;
	private VaoData vaoData;
	private boolean centerText;
	private int numberOfLines;
	
	public GuiText(String text, FontType font, boolean centerText) {
		this.text = text;
		this.font = font;
		this.centerText = centerText;
	}
	
	@Override
	public void recalculateRenderPosition() {
		super.recalculateRenderPosition();
		reload();
	}

	public void reload() {
		TextMeshData data = font.loadText(this);
		VaoLoader.loadToExistingVao(data.getVertexPositions(), data.getTextureCoords(), getMesh());
		resetMeshInfo(data.getVertexCount());
	}
	
	public int getMesh() {
		return vaoData.getID();
	}
	
	public void setMeshInfo(int vao, int verticesCount) {
		this.vaoData = new VaoData(vao, verticesCount);
	}
	
	public void resetMeshInfo(int verticesCount) {
		this.vaoData.setVertexCount(verticesCount);
	}
	
	public int getVertexCount() {
		return this.vaoData.getVertexCount();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public FontType getFont() {
		return font;
	}

	public boolean isCentered() {
		return centerText;
	}

	public int getNumberOfLines() {
		return numberOfLines;
	}

	public void setNumberOfLines(int numberOfLines) {
		this.numberOfLines = numberOfLines;
	}

}
