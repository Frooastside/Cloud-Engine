package de.frooastside.engine.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL30;

import de.frooastside.engine.gui.guielements.GuiLoadingBar;
import de.frooastside.engine.gui.guielements.GuiProgressBar;
import de.frooastside.engine.gui.guielements.GuiRenderElement;
import de.frooastside.engine.gui.guielements.GuiText;
import de.frooastside.engine.gui.meshcreator.FontType;
import de.frooastside.engine.gui.meshcreator.TextMeshData;

public abstract class GuiScreen {
	
	private static int width;
	private static int height;
	
	private boolean guiVisible = true;
	private List<GuiElement> rawElements;
	private List<GuiRenderElement> tiles;
	private List<GuiProgressBar> progressBars;
	private List<GuiLoadingBar> loadingBars;
	private Map<FontType, List<GuiText>> texts = new HashMap<FontType, List<GuiText>>();
	
	public GuiScreen() {
		this.rawElements = new ArrayList<GuiElement>();
		this.tiles = new ArrayList<GuiRenderElement>();
		this.progressBars = new ArrayList<GuiProgressBar>();
		this.loadingBars = new ArrayList<GuiLoadingBar>();
	}

	public void update(float delta) {
		for(GuiElement element : rawElements) {
			element.update(delta);
		}
	}
	
	public void recalculateElementPosition() {
		for (GuiRenderElement guiRenderElement : tiles) {
			guiRenderElement.recalculateRenderPosition();
		}
		for (GuiProgressBar guiProgressBar : progressBars) {
			guiProgressBar.recalculateRenderPosition();
		}
		for (GuiLoadingBar guiLoadingBar : loadingBars) {
			guiLoadingBar.recalculateRenderPosition();
		}
		for(FontType font : texts.keySet()) {
			for(GuiText guiText : texts.get(font)) {
				guiText.recalculateRenderPosition();
			}
		}
	}
	
	public void addElement(GuiRenderElement element) {
		tiles.add(element);
	}
	
	public void removeElement(GuiRenderElement element) {
		tiles.remove(element);
	}
	
	public void addText(GuiText text) {
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		VaoData vao = VaoLoader.loadToVao(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vao.getID(), data.getVertexCount());
		List<GuiText> textBatch = texts.get(font);
		if(textBatch == null) {
			textBatch = new ArrayList<GuiText>();
			texts.put(font, textBatch);
		}
		textBatch.add(text);
	}
	
	public void reloadText(GuiText text) {
		text.reload();
	}
	
	public void removeText(GuiText text) {
		List<GuiText> textBatch = texts.get(text.getFont());
		textBatch.remove(text);
		GL30.glDeleteVertexArrays(text.getMesh());
		if(textBatch.isEmpty()) {
			texts.remove(text.getFont());
		}
	}
	
	public void addProgressBar(GuiProgressBar element) {
		progressBars.add(element);
		tiles.add(element.getBackground());
		tiles.add(element.getProgressBar());
		if(element.getText() != null)
			addText(element.getText());
	}
	
	public void removeProgressBar(GuiProgressBar element) {
		progressBars.remove(element);
		tiles.remove(element.getBackground());
		tiles.remove(element.getProgressBar());
		if(element.getText() != null)
			removeText(element.getText());
	}
	
	public void addLoadingBar(GuiLoadingBar element) {
		loadingBars.add(element);
		tiles.addAll(element.getElements());
	}
	
	public void removeLoadingBar(GuiLoadingBar element) {
		loadingBars.remove(element);
		tiles.removeAll(element.getElements());
	}

	public Map<FontType, List<GuiText>> getTexts() {
		return texts;
	}

	public List<GuiRenderElement> getTiles() {
		return tiles;
	}

	public List<GuiProgressBar> getProgressBars() {
		return progressBars;
	}

	public boolean isGuiVisible() {
		return guiVisible;
	}

	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}

	public static void setWindowSize(int width, int height) {
		GuiScreen.width = width;
		GuiScreen.height = height;
	}
	
}
