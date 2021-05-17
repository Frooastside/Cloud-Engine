package net.frooastside.engine.userinterface.elements;

import net.frooastside.engine.userinterface.Color;

public abstract class RenderElement extends Element {

  public abstract RenderType renderType();

  public enum RenderType {

    BOX, TEXT

  }
}
