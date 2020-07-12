package de.frooastside.engine.gui;

import org.joml.Vector2f;
import org.joml.Vector4f;

public abstract class GuiElement extends ElementConstraints {
	
	protected Vector2f renderPosition = new Vector2f();
	protected Vector2f renderScale = new Vector2f();
	protected Vector4f renderColor = new Vector4f();
	protected boolean visible = true;
	protected boolean highlighted = false;
	protected boolean selected = false;
	
	public void update(float delta) {}
	
	public void recalculateRenderPosition() {
		this.renderPosition.x = getX().getRawXPosition();
		this.renderPosition.y = getY().getRawYPosition();
		this.renderScale.x = getWidth().getRawWidth();
		this.renderScale.y = getHeight().getRawHeight();
	}
	
	public void invokeButtonPress(int key, int scancode, int action, int mods) {}
	
	public void invokeText(int codepoint) {}

	public Vector4f getRenderColor() {
		return renderColor;
	}

	public void setRenderColor(Vector4f renderColor) {
		this.renderColor = renderColor;
	}

	public Vector2f getRenderPosition() {
		return renderPosition;
	}

	public Vector2f getRenderScale() {
		return renderScale;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isHighlighted() {
		return highlighted;
	}

	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
