package de.frooastside.engine.gui.guielements;

import org.lwjgl.glfw.GLFW;

import de.frooastside.engine.gui.GuiElement;
import de.frooastside.engine.gui.constraints.RawConstraint;
import de.frooastside.engine.gui.constraints.TextCenterConstraint;
import de.frooastside.engine.gui.meshcreator.FontType;

public class GuiTextField extends GuiElement {
	
	private GuiText text;
	
	private String previewText;
	private String rawText = "";
	private int maxLength;
	
	public GuiTextField(String previewText, FontType font, float textSize, int maxLength) {
		this.previewText = previewText;
		this.maxLength = maxLength;
		this.text = new GuiText(previewText, font, false);
		this.text.setParent(this);
		this.text.setX(new RawConstraint(0));
		this.text.setY(new TextCenterConstraint());
		this.text.setWidth(new RawConstraint(1));
		this.text.setHeight(new RawConstraint(8 * textSize));
	}
	
	@Override
	public void invokeButtonPress(int key, int scancode, int action, int mods) {
		if(action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT) {
			if(key == GLFW.GLFW_KEY_BACKSPACE) {
				if(rawText.length() > 0) {
					rawText = rawText.substring(0, rawText.length() - 1);
					reloadText();
				}
			}
		}
	}
	
	@Override
	public void invokeText(int codepoint) {
		if(rawText.length() + 1 <= maxLength) {
			rawText += (char) codepoint;
			reloadText();
		}
	}

	protected void reloadText() {
		if(rawText.length() == 0) {
			text.setText(previewText);
			text.reload();
		}else {
			text.setText(rawText);
			text.reload();
		}
	}

	public GuiText getText() {
		return text;
	}
	
}
