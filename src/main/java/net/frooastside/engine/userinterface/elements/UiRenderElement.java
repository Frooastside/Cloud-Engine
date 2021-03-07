package net.frooastside.engine.userinterface.elements;

import net.frooastside.engine.userinterface.UiColor;

public abstract class UiRenderElement extends UiElement {

  private UiColor color;

  public abstract RenderType renderType();

  public UiColor color() {
    return color;
  }

  public void setColor(UiColor color) {
    this.color = color;
  }

  public enum RenderType {

    BOX, TEXT

  }
}
