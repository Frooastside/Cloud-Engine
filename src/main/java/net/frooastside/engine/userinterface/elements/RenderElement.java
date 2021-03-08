package net.frooastside.engine.userinterface.elements;

import net.frooastside.engine.userinterface.Color;

public abstract class RenderElement extends Element {

  private Color color;

  public abstract RenderType renderType();

  public Color color() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public enum RenderType {

    BOX, TEXT

  }
}
