package de.frooastside.engine.gui.meshcreator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.frooastside.engine.Engine;
import de.frooastside.engine.GLFWManager;
import de.frooastside.engine.gui.guielements.GuiText;

public class TextMeshCreator {

	public static final double LINE_HEIGHT = 0.03f;
	protected static final int SPACE_ASCII = 32;

	private MetaFile metaData;
	
	private GLFWManager glfwManager;

	protected TextMeshCreator(File metaFile) {
		glfwManager = Engine.getEngine().getGlfwManager();
		metaData = new MetaFile(metaFile, (float) glfwManager.getCurrentWindowWidth() / (float) glfwManager.getCurrentWindowHeight());
	}

	protected TextMeshData createTextMesh(GuiText text) {
		List<Line> lines = createStructure(text);
		TextMeshData data = createQuadVertices(text, lines);
		return data;
	}

	private List<Line> createStructure(GuiText text) {
		char[] chars = text.getText().toCharArray();
		List<Line> lines = new ArrayList<Line>();
		Line currentLine = new Line(metaData.getSpaceWidth(), text.getRenderScale().y, text.getRenderScale().x);
		Word currentWord = new Word(text.getRenderScale().y);
		for (char c : chars) {
			int ascii = (int) c;
			if(ascii == SPACE_ASCII) {
				boolean added = currentLine.attemptToAddWord(currentWord);
				if(!added) {
					lines.add(currentLine);
					currentLine = new Line(metaData.getSpaceWidth(), text.getRenderScale().y, text.getRenderScale().x);
					currentLine.attemptToAddWord(currentWord);
				}
				currentWord = new Word(text.getRenderScale().y);
				continue;
			}
			Character character = metaData.getCharacter(ascii);
			currentWord.addCharacter(character);
		}
		completeStructure(lines, currentLine, currentWord, text);
		return lines;
	}

	private void completeStructure(List<Line> lines, Line currentLine, Word currentWord, GuiText text) {
		boolean added = currentLine.attemptToAddWord(currentWord);
		if(!added) {
			lines.add(currentLine);
			currentLine = new Line(metaData.getSpaceWidth(), text.getRenderScale().y, text.getRenderScale().x);
			currentLine.attemptToAddWord(currentWord);
		}
		lines.add(currentLine);
	}

	private TextMeshData createQuadVertices(GuiText text, List<Line> lines) {
		text.setNumberOfLines(lines.size());
		double curserX = 0f;
		double curserY = 0f;
		List<Float> vertices = new ArrayList<Float>();
		List<Float> textureCoords = new ArrayList<Float>();
		for (Line line : lines) {
			if(text.isCentered()) {
				curserX = (line.getMaxLength() - line.getLineLength()) / 2;
			}
			for (Word word : line.getWords()) {
				for (Character letter : word.getCharacters()) {
					addVerticesForCharacter(curserX, curserY, letter, text.getRenderScale().y, vertices);
					addTexCoords(textureCoords, letter.getxTextureCoord(), letter.getyTextureCoord(),
							letter.getXMaxTextureCoord(), letter.getYMaxTextureCoord());
					curserX += letter.getxAdvance() * text.getRenderScale().y;
				}
				curserX += metaData.getSpaceWidth() * text.getRenderScale().y;
			}
			curserX = 0;
			curserY += LINE_HEIGHT * text.getRenderScale().y;
		}		
		return new TextMeshData(listToArray(vertices), listToArray(textureCoords));
	}

	private void addVerticesForCharacter(double curserX, double curserY, Character character, double fontSize,
			List<Float> vertices) {
		double x = curserX + (character.getxOffset() * fontSize);
		double y = curserY + (character.getyOffset() * fontSize);
		double maxX = x + (character.getSizeX() * fontSize);
		double maxY = y + (character.getSizeY() * fontSize);
		double properX = (2 * x) - 1;
		double properY = (-2 * y) + 1;
		double properMaxX = (2 * maxX) - 1;
		double properMaxY = (-2 * maxY) + 1;
		addVertices(vertices, properX, properY, properMaxX, properMaxY);
	}

	private static void addVertices(List<Float> vertices, double x, double y, double maxX, double maxY) {
		vertices.add((float) x);
		vertices.add((float) y);
		vertices.add((float) x);
		vertices.add((float) maxY);
		vertices.add((float) maxX);
		vertices.add((float) maxY);
		vertices.add((float) maxX);
		vertices.add((float) maxY);
		vertices.add((float) maxX);
		vertices.add((float) y);
		vertices.add((float) x);
		vertices.add((float) y);
	}

	private static void addTexCoords(List<Float> texCoords, double x, double y, double maxX, double maxY) {
		texCoords.add((float) x);
		texCoords.add((float) y);
		texCoords.add((float) x);
		texCoords.add((float) maxY);
		texCoords.add((float) maxX);
		texCoords.add((float) maxY);
		texCoords.add((float) maxX);
		texCoords.add((float) maxY);
		texCoords.add((float) maxX);
		texCoords.add((float) y);
		texCoords.add((float) x);
		texCoords.add((float) y);
	}

	
	private static float[] listToArray(List<Float> listOfFloats) {
		float[] array = new float[listOfFloats.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = listOfFloats.get(i);
		}
		return array;
	}

	public MetaFile getMetaData() {
		return metaData;
	}

	public void setMetaData(MetaFile metaData) {
		this.metaData = metaData;
	}

	public void reload(File fontFile) {
		metaData = new MetaFile(fontFile, (float) glfwManager.getCurrentWindowWidth() / (float) glfwManager.getCurrentWindowHeight());
	}

}
