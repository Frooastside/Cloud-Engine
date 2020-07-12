package de.frooastside.engine.gui.constraints;

import de.frooastside.engine.Engine;
import de.frooastside.engine.gui.Constraint;

public class AspectRatioRawSubstractConstraint extends Constraint {
	
	private float aspectRatio;
	private float rawValue;
	private float rawSubstract;
	
	public AspectRatioRawSubstractConstraint(float aspectRatio, float rawValue, float rawSubstract) {
		this.aspectRatio = aspectRatio;
		this.rawValue = rawValue;
		this.rawSubstract = rawSubstract;
	}

	@Override
	public float getRawXPosition() {
		if(constraints != null && constraints.getY() != null) {
			return rawValue * (constraints != null ? (constraints.getParent() != null ? constraints.getParent().getWidth().getRawWidth() : 1) : 1) + (constraints != null ? (constraints.getParent() != null ? constraints.getParent().getX().getRawXPosition() : 0) : 0) - (getPixelWidth() * (rawSubstract * Engine.getEngine().getGlfwManager().getCurrentWindowHeight() * aspectRatio)) * (constraints != null ? (constraints.getParent() != null ? constraints.getParent().getWidth().getRawWidth() : 1) : 1);
		}else {
			return 0;
		}
	}

	@Override
	public float getRawYPosition() {
		if(constraints != null && constraints.getX() != null) {
			return rawValue * (constraints != null ? (constraints.getParent() != null ? constraints.getParent().getHeight().getRawHeight() : 1) : 1) + (constraints != null ? (constraints.getParent() != null ? constraints.getParent().getY().getRawYPosition() : 0) : 0) - (getPixelHeight() * (rawSubstract * Engine.getEngine().getGlfwManager().getCurrentWindowWidth()) * aspectRatio) * (constraints != null ? (constraints.getParent() != null ? constraints.getParent().getHeight().getRawHeight() : 1) : 1);
		}else {
			return 0;
		}
	}

	@Override
	public float getRawWidth() {
		if(constraints != null && constraints.getHeight() != null) {
			return getPixelWidth() * ((constraints.getHeight().getRawHeight() * Engine.getEngine().getGlfwManager().getCurrentWindowHeight()) * aspectRatio);
		}else {
			return 0;
		}
	}

	@Override
	public float getRawHeight() {
		if(constraints != null && constraints.getWidth() != null) {
			return getPixelHeight() * ((constraints.getWidth().getRawWidth() * Engine.getEngine().getGlfwManager().getCurrentWindowWidth()) * aspectRatio);
		}else {
			return 0;
		}
	}

}
