package love.polardivision.engine.userinterface.elements.render;

import love.polardivision.engine.glwrapper.texture.Texture;
import love.polardivision.engine.userinterface.Color;
import love.polardivision.engine.userinterface.elements.RenderElement;

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
