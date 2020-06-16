package de.frooastside.engine.gui;

public abstract class Constraint {
	
	public ElementConstraints constraints;
	
	public abstract float getRawXPosition();
	public abstract float getRawYPosition();
	public abstract float getRawWidth();
	public abstract float getRawHeight();
	
	public float getPixelWidth() {
		return 1f / GuiScreen.getWidth();
	}
	
	public float getPixelHeight() {
		return 1f / GuiScreen.getHeight();
	}

}
