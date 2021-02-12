package net.frooastside.engine.userinterface.elements;

import net.frooastside.engine.userinterface.UiColor;
import org.joml.Vector2f;

public abstract class UiRenderElement extends UiElement {

  private UiColor color;
  private float visibility = 1.0f;

  @Override
  public void recalculate(Vector2f pixelSize) {
    super.recalculate(pixelSize);
    onRecalculation();
  }

  public void onRecalculation() {
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
