package net.frooastside.engine.userinterface.elements.container;

import net.frooastside.engine.userinterface.ColorSet;
import net.frooastside.engine.userinterface.elements.FunctionalElement;
import net.frooastside.engine.userinterface.elements.render.Box;

public class Panel extends FunctionalElement {

  private final ColorSet colorSet;

  public Panel(ColorSet colorSet) {
    this.colorSet = colorSet;
  }

  @Override
  public void initialize() {
    Box background = new Box(colorSet.background());
    addElement(background);
  }

}
