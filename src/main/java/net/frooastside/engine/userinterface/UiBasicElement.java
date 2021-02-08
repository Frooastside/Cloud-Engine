package net.frooastside.engine.userinterface;

import org.joml.Vector4f;

public class UiBasicElement {

  private final boolean clickable;
  private final boolean selectable;

  public UiBasicElement(boolean clickable, boolean selectable) {
    this.clickable = clickable;
    this.selectable = selectable;
  }

  public abstract UiRenderElement[] renderElements();
}
