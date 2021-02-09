package net.frooastside.engine.userinterface.elements;

import java.util.ArrayList;
import java.util.List;

public abstract class UiContainerElement extends UiBasicElement {

  private final List<UiElement> children = new ArrayList<>();

  public List<UiElement> children() {
    return children;
  }
}
