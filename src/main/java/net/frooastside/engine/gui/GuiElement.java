package net.frooastside.engine.gui;

import net.frooastside.engine.model.VertexArrayObject;

public abstract class GuiElement {

  public static final VertexArrayObject DEFAULT_SHAPE = createDefaultShape();

  public abstract void recalculate(float aspectRatio);

  private static VertexArrayObject createDefaultShape() {
    return null;
  }

}
