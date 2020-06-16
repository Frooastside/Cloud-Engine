package de.frooastside.engine.gui.constraints;

import de.frooastside.engine.gui.Constraint;

public class TextCenterConstraint extends Constraint {
	
	@Override
	public float getRawYPosition() {
		float localPosition = ((constraints != null ? (constraints.getParent() != null ? constraints.getParent().getHeight().getRawHeight() : 0) : 0) / 2f) - (((constraints != null ? constraints.getHeight().getRawHeight() : 1) * 0.03f) / 2f);
		return (constraints != null ? (constraints.getParent() != null ? constraints.getParent().getY().getRawYPosition() : 0) : 0) + localPosition + getPixelHeight();
	}
	
	@Override
	public float getRawXPosition() {
		return 0;
	}

	@Override
	public float getRawWidth() {
		return 0;
	}

	@Override
	public float getRawHeight() {
		return 0;
	}
	
}
