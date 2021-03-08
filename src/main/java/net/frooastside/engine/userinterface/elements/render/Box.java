package net.frooastside.engine.userinterface.elements.render;

import net.frooastside.engine.graphicobjects.texture.Texture;
import net.frooastside.engine.userinterface.Color;
import net.frooastside.engine.userinterface.elements.RenderElement;

public class Box extends RenderElement {

  private boolean useColor;
  private Texture texture;

  public Box(Color color) {
    setColor(color);
  }

  public Box(Texture texture) {
    this.texture = texture;
  }

  @Override
  public RenderType renderType() {
    return RenderType.BOX;
  }

  @Override
  public void setColor(Color color) {
    super.setColor(color);
    useColor = true;
  }

  public boolean useTexture() {
    return texture != null;
  }

  public boolean useColor() {
    return useColor;
  }

  public Texture texture() {
    return texture;
  }
}
