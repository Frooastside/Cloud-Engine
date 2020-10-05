package net.frooastside.engine.userinterface;

public abstract class UiRenderElement extends UiElement {

  private UiColor color;
  private float visibility = 1.0f;

  @Override
  public UiRenderElement[] renderElements() {
    return new UiRenderElement[]{this};
  }

  public abstract RenderType renderType();

  public UiColor color() {
    return color;
  }

  public void setColor(UiColor color) {
    this.color = color;
  }

  public float visibility() {
    return visibility;
  }

  public void setVisibility(float visibility) {
    this.visibility = visibility;
  }

  public enum RenderType {

    BOX, TEXT

  }
}
