package net.frooastside.engine.userinterface.elements.container;

import net.frooastside.engine.userinterface.Color;
import net.frooastside.engine.userinterface.elements.FunctionalElement;
import net.frooastside.engine.userinterface.elements.render.Box;

public class Panel extends FunctionalElement {

  private final Color color;

  public Panel(Color color) {
    this.color = color;
  }

  @Override
  public void initialize() {
    Box background = new Box(color);
    addElement(background);
  }

}
