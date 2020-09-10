package net.frooastside.engine.gui.font;

import java.util.ArrayList;
import java.util.List;

public class Line {

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
    if(currentLineLength + additionalLength <= maxLength) {
      words.add(word);
      currentLineLength += additionalLength;
      return true;
    }else {
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
