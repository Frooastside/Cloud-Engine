package net.frooastside.engine.gui;

import net.frooastside.engine.resource.Font;

public class GuiTextArea extends GuiText {
  public GuiTextArea(Font font, String text, boolean centered) {
    super(font, text, centered);
  }

  /*private static final float HEIGHT = 5.0f;
  private static final float MAX_LENGTH = 1.0f;
  private static final boolean CENTERED = false;

  private final Font font;

  private VertexArrayObject model;
  private int currentVertexArrayLength;

  public GuiTextArea(Font font) {
    this.font = font;
  }

  public void createMesh() {
    createVertexArrayObject();
    List<Line> lines = createStructure();
    createQuadVertices(lines);
  }

  private List<Line> createStructure() {
    List<Line> lines = new ArrayList<>();
    Line currentLine = new Line(font.spaceWidth(), HEIGHT, MAX_LENGTH); //TODO SPACE WIDTH
    Word currentWord = new Word(HEIGHT);
    for (char asciiCharacter : "AAAA nøkk ist voll aua lululul.".toCharArray()) {
      if (asciiCharacter != Font.SPACE_ASCII) {
        Font.Character character = font.getCharacter(asciiCharacter);
        currentWord.addCharacter(character);
      } else {
        if (!currentLine.attemptToAddWord(currentWord)) {
          lines.add(currentLine);
          currentLine = new Line(font.spaceWidth(), HEIGHT, MAX_LENGTH); //TODO SPACE WIDTH
          currentLine.attemptToAddWord(currentWord);
        }
        currentWord = new Word(HEIGHT);
      }
    }
    if (!currentLine.attemptToAddWord(currentWord)) {
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
    for (Line line : lines) {
      if (CENTERED) {
        cursorX = (line.maxLength() - line.lineLength()) / 2;
      }
      for (Word word : line.words()) {
        for (Font.Character character : word.characters()) {
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

  private void addVerticesFor(List<Float> vertices, Font.Character character, double cursorX, double cursorY, float fontSize) {
    double x = cursorX + (character.xOffset() * fontSize); //TODO OFFSET
    double y = cursorY + (character.yOffset() * fontSize); //TODO OFFSET
    double xMax = x + (character.xSize() * fontSize); //TODO SIZE
    double yMax = y + (character.ySize() * fontSize); //TODO SIZE
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

  private void createVertexArrayObject() {
    model = new VertexArrayObject(VertexArrayObject.generateIdentifier(), 0);
    model.bind();
    VertexBufferObject positionBuffer = VertexBufferObject.createVertexBufferObject(BufferDataType.FLOAT, BufferTarget.ARRAY_BUFFER, BufferUsage.STATIC_DRAW);
    model.appendVertexBufferObject(positionBuffer, 0, 2, false, 0, 0);
    VertexBufferObject textureCoordinateBuffer = VertexBufferObject.createVertexBufferObject(BufferDataType.FLOAT, BufferTarget.ARRAY_BUFFER, BufferUsage.STATIC_DRAW);
    model.appendVertexBufferObject(textureCoordinateBuffer, 1, 2, false, 0, 0);
    model.unbind();
  }

  public void bufferData(float[] positions, float[] textureCoordinates) {
    model.bind();
    currentVertexArrayLength = positions.length / 2;
    model.setLength(currentVertexArrayLength);
    VertexBufferObject positionBuffer = model.getVertexBufferObject(0);
    positionBuffer.storeFloatData(VertexBufferUtils.store(positions));
    VertexBufferObject textureCoordinateBuffer = model.getVertexBufferObject(1);
    textureCoordinateBuffer.storeFloatData(VertexBufferUtils.store(textureCoordinates));
    model.unbind();
  }

  public void bufferSubData(float[] positions, float[] textureCoordinates) {
    model.bind();
    VertexBufferObject positionBuffer = model.getVertexBufferObject(0);
    positionBuffer.storeFloatSubData(VertexBufferUtils.store(positions), 0);
    VertexBufferObject textureCoordinateBuffer = model.getVertexBufferObject(1);
    textureCoordinateBuffer.storeFloatSubData(VertexBufferUtils.store(textureCoordinates), 0);
    model.unbind();
  }

  public VertexArrayObject model() {
    return model;
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

    private final List<Font.Character> characters = new ArrayList<>();
    private final double fontSize;

    private double width = 0;

    public Word(double fontSize) {
      this.fontSize = fontSize;
    }

    public void addCharacter(Font.Character character) {
      characters.add(character);
      width += character.xAdvance() * fontSize; //TODO ADVANCE
    }

    public List<Font.Character> characters() {
      return characters;
    }

    public double wordWidth() {
      return width;
    }

  }*/
}
