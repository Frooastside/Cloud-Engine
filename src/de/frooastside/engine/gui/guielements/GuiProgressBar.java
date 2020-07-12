package de.frooastside.engine.gui.guielements;

import org.joml.Vector4f;

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
	
	public GuiProgressBar(Vector4f backgroundColor, Vector4f progressBarColor, int space) {
		background = new GuiRenderElement(backgroundColor);
		background.setParent(this);
		background.setX(new RawConstraint(0));
		background.setY(new RawConstraint(0));
		background.setWidth(new RawConstraint(1));
		background.setHeight(new RawConstraint(1));
		progressBar = new GuiRenderElement(progressBarColor);
		progressBar.setParent(this);
		progressBar.setX(new PixelConstraint(space));
		progressBar.setY(new PixelConstraint(space));
		progressBar.setWidth(new RawPixelSubstractConstraint(1, space * 2));
		progressBar.setHeight(new RawPixelSubstractConstraint(1, space * 2));
	}
	
	public GuiProgressBar(FontType font, String text, int textSize, Vector4f backgroundColor, Vector4f progressBarColor, int space) {
		background = new GuiRenderElement(backgroundColor);
		background.setParent(this);
		background.setX(new RawConstraint(0));
		background.setY(new RawConstraint(0));
		background.setWidth(new RawConstraint(1));
		background.setHeight(new RawConstraint(1));
		progressBar = new GuiRenderElement(progressBarColor);
		progressBar.setParent(this);
		progressBar.setX(new PixelConstraint(space));
		progressBar.setY(new PixelConstraint(space));
		progressBar.setWidth(new RawPixelSubstractConstraint(1, space * 2));
		progressBar.setHeight(new RawPixelSubstractConstraint(1, space * 2));
		this.text = new GuiText(text, font, true);
		this.text.setParent(this);
		this.text.setX(new RawConstraint(0));
		this.text.setY(new TextCenterConstraint());
		this.text.setWidth(new RawConstraint(1));
		this.text.setHeight(new RawConstraint(8 * textSize));
	}
	
	public void setText(String text) {
		this.text.setText(text);
	}
	
	public void setProgress(float progress) {
		((RawPixelSubstractConstraint) progressBar.getWidth()).setRawValue(progress);
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
