package net.frooastside.engine.userinterface.elements.render;

import net.frooastside.engine.graphicobjects.Font;
import net.frooastside.engine.userinterface.Color;

import java.util.ArrayList;
import java.util.List;

public class TextArea extends Text {

  public TextArea(Font font, String text, Color color) {
    super(font, text, color, false);
  }

  @Override
  public void recalculateBounds() {
    super.recalculateBounds();
    float maxLineLength = bounds().z;
    float rawHeight = bounds().w;

    double verticalPerPixelSize = LINE_HEIGHT / (double) font().characterHeight();
    double horizontalPerPixelSize = verticalPerPixelSize / aspectRatio();
    double fontWidth = horizontalPerPixelSize * rawHeight;
    double fontHeight = verticalPerPixelSize * rawHeight;

    double spaceWidth = font().getCharacter(Font.SPACE_CODEPOINT).xAdvance() * fontWidth;

    List<Line> lines = new ArrayList<>();
    Line currentLine = new Line(spaceWidth, maxLineLength);
    Word currentWord = new Word(fontWidth);

    int characterCount = 0;
    for (char codepoint : text().toCharArray()) {
      if (codepoint != Font.SPACE_CODEPOINT) {
        Font.Character character = font().getCharacter(codepoint);
        currentWord.addCharacter(character);
        characterCount++;
      } else {
        if (!currentLine.attemptToAddWord(currentWord)) {
          lines.add(currentLine);
          currentLine = new Line(spaceWidth, maxLineLength);
          currentLine.attemptToAddWord(currentWord);
        }
        currentWord = new Word(fontWidth);
      }
    }
    if (!currentLine.attemptToAddWord(currentWord)) {
      lines.add(currentLine);
      currentLine = new Line(spaceWidth, maxLineLength);
      currentLine.attemptToAddWord(currentWord);
    }
    lines.add(currentLine);

    double cursorX = 0;
    double yLineOffset = LINE_HEIGHT * (rawHeight / 1.5f);
    float[] positions = new float[characterCount * 12];
    float[] textureCoordinates = new float[characterCount * 12];

    int index = 0;
    for (Line line : lines) {
      for (Word word : line.words()) {
        for (Font.Character character : word.characters()) {
          addVerticesFor(positions, textureCoordinates, character, index, fontHeight, fontWidth, cursorX, yLineOffset);
          cursorX += character.xAdvance() * fontWidth;
          index++;
        }
        cursorX += spaceWidth;
      }
      cursorX = 0;
      yLineOffset += LINE_HEIGHT * rawHeight;
    }

    if (positions.length / 2 == model().length()) {
      bufferSubData(positions, textureCoordinates);
    } else {
      bufferData(positions, textureCoordinates);
    }
  }

  public static class Line {

    private final List<Word> words = new ArrayList<>();
    private final double maxLineLength;
    private final double spaceWidth;

    private double currentLineLength = 0;

    public Line(double spaceWidth, double maxLineLength) {
      this.spaceWidth = spaceWidth;
      this.maxLineLength = maxLineLength;
    }

    public boolean attemptToAddWord(Word word) {
      double additionalLength = word.wordWidth();
      additionalLength += !words.isEmpty() ? spaceWidth : 0;
      if (currentLineLength + additionalLength <= maxLineLength) {
        words.add(word);
        currentLineLength += additionalLength;
        return true;
      } else {
        return false;
      }
    }

    public List<Word> words() {
      return words;
    }

  }

  public static class Word {

    private final List<Font.Character> characters = new ArrayList<>();
    private final double fontWidth;

    private double width = 0;

    public Word(double fontWidth) {
      this.fontWidth = fontWidth;
    }

    public void addCharacter(Font.Character character) {
      characters.add(character);
      width += character.xAdvance() * fontWidth;
    }

    public List<Font.Character> characters() {
      return characters;
    }

    public double wordWidth() {
      return width;
    }

  }
}
