package de.frooastside.engine.gui.constraints;

import de.frooastside.engine.gui.Constraint;

public class PixelConstraint extends Constraint {
	
	private int pixel;
	
	public PixelConstraint(int pixel) {
		this.pixel = pixel;
	}

	@Override
	public float getRawXPosition() {
		return (getPixelWidth() * pixel) + (constraints != null ? (constraints.getParent() != null ? constraints.getParent().getX().getRawXPosition() : 0) : 0);
	}

	@Override
	public float getRawYPosition() {
		return (getPixelHeight() * pixel) + (constraints != null ? (constraints.getParent() != null ? constraints.getParent().getY().getRawYPosition() : 0) : 0);
	}

	@Override
	public float getRawWidth() {
		return getPixelWidth() * pixel;
	}

	@Override
	public float getRawHeight() {
		return getPixelHeight() * pixel;
	}

}
