package net.frooastside.engine.userinterface.elements;

import net.frooastside.engine.glfw.Window;
import net.frooastside.engine.graphicobjects.texture.Texture;
import net.frooastside.engine.userinterface.UiColor;
import net.frooastside.engine.userinterface.UiColorSet;
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
  public void onLoseContact() {
    //TODO
  }

  @Override
  public void update() {

  }

  @Override
  public void onContact() {
    //TODO
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

  @Override
  public void invokeCharCallback(Window window, char codepoint) {

  }

  @Override
  public void invokeKeyCallback(Window window, int key, int scancode, Modifier modifier, Action buttonState) {

  }

  @Override
  public void invokeMouseButtonCallback(Window window, int key, boolean pressed) {

  }
}
