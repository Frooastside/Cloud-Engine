package net.frooastside.engine.userinterface.elements;

public abstract class RenderElement extends Element {

  public void recalculate() {}

  public abstract RenderType renderType();

  public enum RenderType {

    BOX, TEXT

  }
}
