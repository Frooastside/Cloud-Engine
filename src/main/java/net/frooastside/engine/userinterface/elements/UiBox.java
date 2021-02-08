package net.frooastside.engine.userinterface.elements;

import net.frooastside.engine.graphicobjects.texture.Texture;
import net.frooastside.engine.userinterface.UiColor;
import net.frooastside.engine.userinterface.UiRenderElement;

public class UiBox extends UiRenderElement {

  private final boolean useTexture;
  private Texture texture;

  public UiBox(UiColor color) {
    super.setColor(color);
    this.useTexture = false;
  }

  public UiBox(Texture texture) {
    this.texture = texture;
    this.useTexture = true;
  }

  @Override
  public RenderType renderType() {
    return RenderType.BOX;
  }

  public boolean useTexture() {
    return useTexture;
  }

  public Texture texture() {
    return texture;
  }
}
