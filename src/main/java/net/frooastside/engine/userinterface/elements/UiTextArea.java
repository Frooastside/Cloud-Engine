package net.frooastside.engine.userinterface.elements;

import net.frooastside.engine.resource.ResourceFont;
import net.frooastside.engine.userinterface.UiColor;

public class UiTextArea extends UiText {

  public UiTextArea(ResourceFont font, String text, boolean centered) {
    super(font, text, UiColor.ACCENT, centered);
  }

  /*public void createMesh() {
    createVertexArrayObject();
    List<Line> lines = createStructure();
    createQuadVertices(lines);
  }

  private List<Line> createStructure() {
    List<Line> lines = new ArrayList<>();
    Line currentLine = new Line(font.spaceWidth(), constraints.bounds().w, constraints.bounds().z); //TODO SPACE WIDTH
    Word currentWord = new Word(constraints.bounds().w);
    for (char asciiCharacter : "AAAA nøkk ist voll aua lululul.".toCharArray()) {
      if (asciiCharacter != ResourceFont.SPACE_ASCII) {
        ResourceFont.Character character = font.getCharacter(asciiCharacter);
        currentWord.addCharacter(character);
      } else {
        if (!currentLine.attemptToAddWord(currentWord)) {
          lines.add(currentLine);
          currentLine = new Line(font.getCharacter(ResourceFont.SPACE_ASCII).xAdvance(), constraints.bounds().w, constraints.bounds().z);
          currentLine.attemptToAddWord(currentWord);
        }
        currentWord = new Word(constraints.bounds().w);
      }
    }
    if (!currentLine.attemptToAddWord(currentWord)) {
      lines.add(currentLine);
      currentLine = new Line(font.spaceWidth(), constraints.bounds().w, constraints.bounds().z);
      currentLine.attemptToAddWord(currentWord);
    }
    lines.add(currentLine);
    return lines;
  }

  private void createQuadVertices(List<Line> lines) {
    double cursorX = 0.0;
    double cursorY = ResourceFont.LINE_HEIGHT * constraints.bounds().w / 2;
    List<Float> positions = new ArrayList<>();
    List<Float> textureCoordinates = new ArrayList<>();
    for (Line line : lines) {
      if (CENTERED) {
        cursorX = (line.maxLength() - line.lineLength()) / 2;
      }
      for (Word word : line.words()) {
        for (Font.Character character : word.characters()) {
          addVerticesFor(positions, character, cursorX, cursorY, constraints.bounds().w);
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
    if (currentVertexArrayLength == positions.size() / 2) {
      bufferSubData(toArray(positions), toArray(textureCoordinates));
    } else {
      bufferData(toArray(positions), toArray(textureCoordinates));
    }
  }

  public float[] toArray(List<Float> list) {
    float[] array = new float[list.size()];
    IntStream.range(0, array.length).forEach(i -> array[i] = list.get(i));
    return array;
  }

  public static class Line {

    private final List<Word> words = new ArrayList<>();
    private final double maxLength;
    private final double spaceSize;

    private double currentLineLength = 0;

    public Line(double spaceWidth, double fontSize, double maxLength) {
      this.spaceSize = spaceWidth * fontSize;
      this.maxLength = maxLength;
    }

    public boolean attemptToAddWord(Word word) {
      double additionalLength = word.wordWidth();
      additionalLength += !words.isEmpty() ? spaceSize : 0;
      if (currentLineLength + additionalLength <= maxLength) {
        words.add(word);
        currentLineLength += additionalLength;
        return true;
      } else {
        return false;
      }
    }

    public double maxLength() {
      return maxLength;
    }

    public double lineLength() {
      return currentLineLength;
    }

    public List<Word> words() {
      return words;
    }

  }

  public static class Word {

    private final List<ResourceFont.Character> characters = new ArrayList<>();
    private final double fontSize;

    private double width = 0;

    public Word(double fontSize) {
      this.fontSize = fontSize;
    }

    public void addCharacter(ResourceFont.Character character) {
      characters.add(character);
      width += character.xAdvance() * fontSize; //TODO ADVANCE
    }

    public List<ResourceFont.Character> characters() {
      return characters;
    }

    public double wordWidth() {
      return width;
    }

  }*/
}
