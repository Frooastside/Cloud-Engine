package de.frooastside.engine.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL30;

import de.frooastside.engine.Engine;
import de.frooastside.engine.gui.guielements.GuiLoadingBar;
import de.frooastside.engine.gui.guielements.GuiProgressBar;
import de.frooastside.engine.gui.guielements.GuiRenderElement;
import de.frooastside.engine.gui.guielements.GuiText;
import de.frooastside.engine.gui.guielements.GuiTextField;
import de.frooastside.engine.gui.meshcreator.FontType;
import de.frooastside.engine.gui.meshcreator.TextMeshData;
import de.frooastside.engine.input.IButtonCallback;
import de.frooastside.engine.input.IMouseButtonCallback;
import de.frooastside.engine.input.ITextCallback;
import de.frooastside.engine.model.VaoData;
import de.frooastside.engine.model.VertexArrayObjectLoader;

public abstract class GuiScreen implements IMouseButtonCallback, IButtonCallback, ITextCallback {
	
	private boolean visible = true;
	private List<GuiElement> rawElements;
	private List<GuiRenderElement> tiles;
	private Map<FontType, List<GuiText>> texts = new HashMap<FontType, List<GuiText>>();
	
	private GuiElement highlightedElement;
	private GuiElement selectedElement;
	
	private float lastMouseX;
	private float lastMouseY;
	
	public GuiScreen() {
		this.rawElements = new ArrayList<GuiElement>();
		this.tiles = new ArrayList<GuiRenderElement>();
	}

	public void update(float delta) {
		float mouseX = Engine.getEngine().getInputManager().getMouseX();
		float mouseY = Engine.getEngine().getInputManager().getMouseY();
		if(lastMouseX != mouseX || lastMouseY != mouseY) {
			if(highlightedElement != null)
				highlightedElement.setHighlighted(false);
			highlightedElement = findElement(mouseX, mouseY);
			if(highlightedElement != null) {
				highlightedElement.setHighlighted(true);
			}
		}
		for(GuiElement element : rawElements) {
			element.update(delta);
		}
	}
	
	private GuiElement findElement(float mouseX, float mouseY) {
		GuiElement element = null;
		for (GuiElement guiElement : rawElements) {
			float guiElementX = guiElement.getRenderPosition().x;
			float guiElementY = guiElement.getRenderPosition().y;
			float guiElementWidth = guiElement.getRenderScale().x;
			float guiElementHeight = guiElement.getRenderScale().y;
			float windowWidth = Engine.getEngine().getGlfwManager().getCurrentWindowWidth();
			float windowHeight = Engine.getEngine().getGlfwManager().getCurrentWindowHeight();
			if(checkCollision(mouseX, mouseY, guiElementX * windowWidth, guiElementY * windowHeight, guiElementWidth * windowWidth, guiElementHeight * windowHeight))
				element = guiElement;
		}
		return element;
	}
	
	private boolean checkCollision(float mouseX, float mouseY, float x, float y, float width, float height) {
		return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
	}

	public void invokeMouseButtonPress(int button, int action, int mods) {
		if(button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_PRESS)
			selectedElement = highlightedElement;
	}

	@Override
	public void invokeButtonPress(int key, int scancode, int action, int mods) {
		if(selectedElement != null) {
			selectedElement.invokeButtonPress(key, scancode, action, mods);
		}
	}
	
	@Override
	public void invokeText(long window, int codepoint) {
		if(selectedElement != null) {
			selectedElement.invokeText(codepoint);
		}
	}

	public void open() {
		recalculateElementPosition();
	}
	
	public void close() {}
	
	public void recalculateElementPosition() {
		for (GuiElement guiElement : rawElements) {
			guiElement.recalculateRenderPosition();
		}
		for(FontType font : texts.keySet()) {
			for(GuiText guiText : texts.get(font)) {
				guiText.recalculateRenderPosition();
			}
		}
	}
	
	public void addElement(GuiElement element) {
		if(element instanceof GuiText) {
			GuiText text = (GuiText) element;
			FontType font = text.getFont();
			TextMeshData data = font.loadText(text);
			VaoData vao = VertexArrayObjectLoader.loadToVao(data.getVertexPositions(), data.getTextureCoords());
			text.setMeshInfo(vao.getID(), data.getVertexCount());
			List<GuiText> textBatch = texts.get(font);
			if(textBatch == null) {
				textBatch = new ArrayList<GuiText>();
				texts.put(font, textBatch);
			}
			textBatch.add(text);
		}else if(element instanceof GuiTextField) {
			addElement(((GuiTextField) element).getText());
		}else if(element instanceof GuiRenderElement) {
			tiles.add((GuiRenderElement) element);
		}else if(element instanceof GuiProgressBar) {
			GuiProgressBar progressBar = (GuiProgressBar) element;
			addElement(progressBar.getBackground());
			addElement(progressBar.getProgressBar());
			if(progressBar.getText() != null)
				addElement(progressBar.getText());
		}else if(element instanceof GuiLoadingBar) {
			GuiLoadingBar loadingBar = (GuiLoadingBar) element;
			for (GuiRenderElement guiRenderElement : loadingBar.getElements()) {
				addElement(guiRenderElement);
			}
		}
		rawElements.add(element);
	}
	
	public void removeElement(GuiElement element) {
		if(element instanceof GuiText) {
			GuiText text = (GuiText) element;
			List<GuiText> textBatch = texts.get(text.getFont());
			textBatch.remove(text);
			GL30.glDeleteVertexArrays(text.getMesh());
			if(textBatch.isEmpty()) {
				texts.remove(text.getFont());
			}
		}else if(element instanceof GuiTextField) {
			removeElement(((GuiTextField) element).getText());
		}else if(element instanceof GuiRenderElement) {
			tiles.remove((GuiRenderElement) element);
		}else if(element instanceof GuiProgressBar) {
			GuiProgressBar progressBar = (GuiProgressBar) element;
			removeElement(progressBar.getBackground());
			removeElement(progressBar.getProgressBar());
			if(progressBar.getText() != null)
				removeElement(progressBar.getText());
		}else if(element instanceof GuiLoadingBar) {
			GuiLoadingBar loadingBar = (GuiLoadingBar) element;
			for (GuiRenderElement guiRenderElement : loadingBar.getElements()) {
				removeElement(guiRenderElement);
			}
		}
		rawElements.remove(element);
	}

	public Map<FontType, List<GuiText>> getTexts() {
		return texts;
	}

	public List<GuiRenderElement> getTiles() {
		return tiles;
	}

	public boolean isVisible() {
		return visible;
	}
	
}
