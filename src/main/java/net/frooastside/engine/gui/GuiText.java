package net.frooastside.engine.gui;

import net.frooastside.engine.gui.font.Character;
import net.frooastside.engine.gui.font.Font;
import net.frooastside.engine.gui.font.Line;
import net.frooastside.engine.gui.font.Word;
import net.frooastside.engine.model.VertexArrayObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class GuiText {

  private static final float HEIGHT = 5.0f;
  private static final float MAX_LENGTH = 1.0f;
  private static final boolean CENTERED = false;

  private final Font font;

  private VertexArrayObject model;
  private float[] positions;
  private float[] textureCoordinates;

  public GuiText(Font font) {
    this.font = font;
  }

  public void createMesh() {
    List<Line> lines = createStructure();
    createQuadVertices(lines);
    model = VertexArrayObject.create2DTFor(positions, textureCoordinates);
  }

  private List<Line> createStructure() {
    List<Line> lines = new ArrayList<>();
    Line currentLine = new Line(font.spaceWidth(), HEIGHT, MAX_LENGTH); //TODO SPACE WIDTH
    Word currentWord = new Word(HEIGHT);
    for(char asciiCharacter : "AAAA Samuel ist nicht voll gemein lululul.".toCharArray()) {
      if(asciiCharacter != Font.SPACE_ASCII) {
        Character character = font.getCharacter(asciiCharacter);
        currentWord.addCharacter(character);
      }else {
        if(!currentLine.attemptToAddWord(currentWord)) {
          lines.add(currentLine);
          currentLine = new Line(font.spaceWidth(), HEIGHT, MAX_LENGTH); //TODO SPACE WIDTH
          currentLine.attemptToAddWord(currentWord);
        }
        currentWord = new Word(HEIGHT);
      }
    }
    if(!currentLine.attemptToAddWord(currentWord)) {
      lines.add(currentLine);
      currentLine = new Line(font.spaceWidth(), HEIGHT, MAX_LENGTH); //TODO SPACE WIDTH
      currentLine.attemptToAddWord(currentWord);
    }
    lines.add(currentLine);
    return lines;
  }

  private void createQuadVertices(List<Line> lines) {
    double cursorX = 0.0;
    double cursorY = Font.LINE_HEIGHT * HEIGHT / 2;
    List<Float> positions = new ArrayList<>();
    List<Float> textureCoordinates = new ArrayList<>();
    for(Line line : lines) {
      if(CENTERED) {
        cursorX = (line.maxLength() - line.lineLength()) / 2;
      }
      for(Word word : line.words()) {
        for(Character character : word.characters()) {
          addVerticesFor(positions, character, cursorX, cursorY, HEIGHT);
          addPoints(textureCoordinates,
            character.xTextureCoordinate(),
            character.yTextureCoordinate(),
            character.xMaxTextureCoordinate(),
            character.yMaxTextureCoordinate());
          cursorX += character.xAdvance() * HEIGHT; //TODO ADVANCE
        }
        cursorX += font.spaceWidth() * HEIGHT; //TODO SPACE WIDTH
      }
      cursorX = 0;
      cursorY += Font.LINE_HEIGHT * HEIGHT;
    }
    this.positions = toArray(positions);
    this.textureCoordinates = toArray(textureCoordinates);
  }

  public float[] toArray(List<Float> list) {
    float[] array = new float[list.size()];
    IntStream.range(0, array.length).forEach(i -> array[i] = list.get(i));
    return array;
  }

  private void addVerticesFor(List<Float> vertices, Character character, double cursorX, double cursorY, float fontSize) {
    double x = (cursorX + (character.xOffset() * fontSize)); //TODO OFFSET
    double y = (cursorY + (character.yOffset() * fontSize)); //TODO OFFSET
    double xMax = (x + (character.xSize() * fontSize)); //TODO SIZE
    double yMax = (y + (character.ySize() * fontSize)); //TODO SIZE
    //addPoints(vertices, x, y, xMax, yMax);
    addPoints(vertices, x * 2 - 1, y * -2 + 1, xMax * 2 - 1, yMax * -2 + 1);
  }

  public void addPoints(List<Float> points, double x, double y, double xMax, double yMax) {
    points.add((float) x);
    points.add((float) y);
    points.add((float) x);
    points.add((float) yMax);
    points.add((float) xMax);
    points.add((float) yMax);

    points.add((float) x);
    points.add((float) y);
    points.add((float) xMax);
    points.add((float) yMax);
    points.add((float) xMax);
    points.add((float) y);
  }

  public VertexArrayObject model() {
    return model;
  }
}
