package de.frooastside.engine.gui.guielements;

import org.joml.Vector4f;

import de.frooastside.engine.gui.GuiElement;

public class GuiRenderElement extends GuiElement {
	
	private boolean useColor;
	private int texture;
	
	public GuiRenderElement(int texture) {
		this.texture = texture;
		this.useColor = false;
	}
	
	public GuiRenderElement(Vector4f color) {
		this.setRenderColor(color);
		this.useColor = true;
	}

	public int getTexture() {
		return texture;
	}

	public boolean isUseColor() {
		return useColor;
	}
	
}
