package net.frooastside.engine.gui.font;

import java.util.ArrayList;
import java.util.List;

public class Word {

  private final List<Character> characters = new ArrayList<>();
  private final double fontSize;

  private double width = 0;

  public Word(double fontSize) {
    this.fontSize = fontSize;
  }

  public void addCharacter(Character character) {
    characters.add(character);
    width += character.xAdvance() * fontSize;
  }

  public List<Character> characters() {
    return characters;
  }

  public double wordWidth() {
    return width;
  }

}
