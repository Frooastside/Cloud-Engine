package net.frooastside.engine.userinterface.elements.render;

import net.frooastside.engine.graphicobjects.texture.Texture;
import net.frooastside.engine.userinterface.UiColor;
import net.frooastside.engine.userinterface.elements.UiRenderElement;

public class UiBox extends UiRenderElement {

  private boolean useColor;
  private Texture texture;

  public UiBox(UiColor color) {
    setColor(color);
  }

  public UiBox(Texture texture) {
    this.texture = texture;
  }

  @Override
  public RenderType renderType() {
    return RenderType.BOX;
  }

  @Override
  public void setColor(UiColor color) {
    super.setColor(color);
    useColor = true;
  }

  public boolean useColor() {
    return useColor;
  }

  public boolean useTexture() {
    return texture != null;
  }

  public Texture texture() {
    return texture;
  }
}
