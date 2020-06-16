package de.frooastside.engine.gui.constraints;

import de.frooastside.engine.gui.Constraint;

public class RawPixelSubstractConstraint extends Constraint {
	
	private float rawValue;
	private int pixel;
	
	public RawPixelSubstractConstraint(float rawValue, int pixel) {
		this.rawValue = rawValue;
		this.pixel = pixel;
	}

	@Override
	public float getRawXPosition() {
		return (constraints != null ? (constraints.getParent() != null ? constraints.getParent().getX().getRawXPosition() : 0) : 0) + (constraints != null ? (constraints.getParent() != null ? constraints.getParent().getWidth().getRawWidth() : 1) : 1) - (getPixelWidth() * pixel);
	}

	@Override
	public float getRawYPosition() {
		return (constraints != null ? (constraints.getParent() != null ? constraints.getParent().getY().getRawYPosition() : 0) : 0) + (constraints != null ? (constraints.getParent() != null ? constraints.getParent().getHeight().getRawHeight() : 1) : 1) - (getPixelHeight() * pixel);
	}

	@Override
	public float getRawWidth() {
		return (rawValue * (constraints != null ? (constraints.getParent() != null ? constraints.getParent().getWidth().getRawWidth() : 1) : 1)) - getPixelWidth() * pixel;
	}

	@Override
	public float getRawHeight() {
		return (rawValue * (constraints != null ? (constraints.getParent() != null ? constraints.getParent().getHeight().getRawHeight() : 1) : 1)) - getPixelHeight() * pixel;
	}

	public void setRawValue(float rawValue) {
		this.rawValue = rawValue;
	}

}