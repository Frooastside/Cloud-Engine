package de.frooastside.engine.gui;

import org.joml.Vector2f;
import org.joml.Vector4f;

public abstract class GuiElement {
	
	protected ElementConstraints constraints = new ElementConstraints();
	protected Vector2f renderPosition = new Vector2f();
	protected Vector2f renderScale = new Vector2f();
	protected Vector4f renderColor = new Vector4f();
	protected boolean visible = true;
	
	public void update(float delta) {}
	
	public void recalculateRenderPosition() {
		this.renderPosition.x = constraints.getX().getRawXPosition();
		this.renderPosition.y = constraints.getY().getRawYPosition();
		this.renderScale.x = constraints.getWidth().getRawWidth();
		this.renderScale.y = constraints.getHeight().getRawHeight();
	}

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

	public ElementConstraints getConstraints() {
		return constraints;
	}

}
