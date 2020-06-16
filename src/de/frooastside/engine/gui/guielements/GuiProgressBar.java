package de.frooastside.engine.gui.guielements;

import org.joml.Vector4f;

import de.frooastside.engine.gui.ElementConstraints;
import de.frooastside.engine.gui.GuiElement;
import de.frooastside.engine.gui.constraints.PixelConstraint;
import de.frooastside.engine.gui.constraints.RawConstraint;
import de.frooastside.engine.gui.constraints.RawPixelSubstractConstraint;
import de.frooastside.engine.gui.constraints.TextCenterConstraint;
import de.frooastside.engine.gui.meshcreator.FontType;

public class GuiProgressBar extends GuiElement {
	
	private GuiRenderElement background;
	private GuiRenderElement progressBar;
	private GuiText text;
	
	public GuiProgressBar(ElementConstraints constraints, Vector4f backgroundColor, Vector4f progressBarColor, int space) {
		this.constraints = constraints;
		background = new GuiRenderElement(backgroundColor);
		background.getConstraints().setParent(constraints);
		background.getConstraints().setX(new RawConstraint(1));
		background.getConstraints().setY(new RawConstraint(1));
		background.getConstraints().setWidth(new RawConstraint(1));
		background.getConstraints().setHeight(new RawConstraint(1));
		progressBar = new GuiRenderElement(progressBarColor);
		progressBar.getConstraints().setParent(constraints);
		progressBar.getConstraints().setX(new PixelConstraint(space));
		progressBar.getConstraints().setY(new PixelConstraint(space));
		progressBar.getConstraints().setWidth(new RawPixelSubstractConstraint(1, space * 2));
		progressBar.getConstraints().setHeight(new RawPixelSubstractConstraint(1, space * 2));
	}
	
	public GuiProgressBar(ElementConstraints constraints, FontType font, String text, int textSize, Vector4f backgroundColor, Vector4f progressBarColor, int space) {
		this.constraints = constraints;
		background = new GuiRenderElement(backgroundColor);
		background.getConstraints().setParent(constraints);
		background.getConstraints().setX(new RawConstraint(0));
		background.getConstraints().setY(new RawConstraint(0));
		background.getConstraints().setWidth(new RawConstraint(1));
		background.getConstraints().setHeight(new RawConstraint(1));
		progressBar = new GuiRenderElement(progressBarColor);
		progressBar.getConstraints().setParent(constraints);
		progressBar.getConstraints().setX(new PixelConstraint(space));
		progressBar.getConstraints().setY(new PixelConstraint(space));
		progressBar.getConstraints().setWidth(new RawPixelSubstractConstraint(1, space * 2));
		progressBar.getConstraints().setHeight(new RawPixelSubstractConstraint(1, space * 2));
		this.text = new GuiText(text, font, true);
		this.text.getConstraints().setParent(constraints);
		this.text.getConstraints().setX(new RawConstraint(0));
		this.text.getConstraints().setY(new TextCenterConstraint());
		this.text.getConstraints().setWidth(new RawConstraint(1));
		this.text.getConstraints().setHeight(new RawConstraint(8 * textSize));
	}
	
	public void setText(String text) {
		this.text.setText(text);
	}
	
	public void setProgress(float progress) {
		((RawPixelSubstractConstraint) progressBar.getConstraints().getWidth()).setRawValue(progress);
		progressBar.recalculateRenderPosition();
	}

	public GuiRenderElement getBackground() {
		return background;
	}

	public GuiRenderElement getProgressBar() {
		return progressBar;
	}

	public GuiText getText() {
		return text;
	}

}
