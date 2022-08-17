package net.frooastside.engine.userinterface.elements.container;

import net.frooastside.engine.userinterface.elements.ContainerElement;
import net.frooastside.engine.userinterface.elements.Element;

public class Container extends ContainerElement {

  private Element background;

  @Override
  public void calculateChildBounds() {
    super.calculateChildBounds();
    if (background != null) {
      background.bounds().set(this.bounds());
    }
  }

  public Element background() {
    return background;
  }

  public void setBackground(Element background) {
    if (this.background != null) {
      children().remove(background);
    }
    if (background != null) {
      addElement(background);
    }
    this.background = background;
  }
}
