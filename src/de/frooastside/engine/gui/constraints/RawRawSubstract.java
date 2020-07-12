package de.frooastside.engine.gui.constraints;

import de.frooastside.engine.gui.Constraint;

public class RawRawSubstract extends Constraint {
	
	private float rawValue;
	private float rawSubstract;
	
	public RawRawSubstract(float rawValue, float rawSubstract) {
		this.rawValue = rawValue;
		this.rawSubstract = rawSubstract;
	}

	@Override
	public float getRawXPosition() {
		return rawValue * (constraints != null ? (constraints.getParent() != null ? constraints.getParent().getWidth().getRawWidth() : 1) : 1) + (constraints != null ? (constraints.getParent() != null ? constraints.getParent().getX().getRawXPosition() : 0) : 0) - rawSubstract * (constraints != null ? (constraints.getParent() != null ? constraints.getParent().getWidth().getRawWidth() : 1) : 1);
	}

	@Override
	public float getRawYPosition() {
		return rawValue * (constraints != null ? (constraints.getParent() != null ? constraints.getParent().getHeight().getRawHeight() : 1) : 1) + (constraints != null ? (constraints.getParent() != null ? constraints.getParent().getY().getRawYPosition() : 0) : 0) - rawSubstract * (constraints != null ? (constraints.getParent() != null ? constraints.getParent().getHeight().getRawHeight() : 1) : 1);
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
