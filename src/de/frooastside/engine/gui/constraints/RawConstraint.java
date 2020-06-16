package de.frooastside.engine.gui.constraints;

import de.frooastside.engine.gui.Constraint;

public class RawConstraint extends Constraint {
	
	private float rawValue;
	
	public RawConstraint(float rawValue) {
		this.rawValue = rawValue;
	}

	@Override
	public float getRawXPosition() {
		return rawValue + (constraints != null ? (constraints.getParent() != null ? constraints.getParent().getX().getRawXPosition() : 0) : 0);
	}

	@Override
	public float getRawYPosition() {
		return rawValue + (constraints != null ? (constraints.getParent() != null ? constraints.getParent().getY().getRawYPosition() : 0) : 0);
	}

	@Override
	public float getRawWidth() {
		return rawValue * (constraints != null ? (constraints.getParent() != null ? constraints.getParent().getWidth().getRawWidth() : 1) : 1);
	}

	@Override
	public float getRawHeight() {
		return rawValue * (constraints != null ? (constraints.getParent() != null ? constraints.getParent().getHeight().getRawHeight() : 1) : 1);
	}

}
