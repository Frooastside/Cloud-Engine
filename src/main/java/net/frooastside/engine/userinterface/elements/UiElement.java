package net.frooastside.engine.userinterface.elements;

import net.frooastside.engine.userinterface.ElementConstraints;
import org.joml.Vector2f;
import org.joml.Vector4f;

public abstract class UiElement {

  private ElementConstraints constraints;

  private final Vector2f pixelSize = new Vector2f();

  public void recalculate(Vector2f pixelSize) {
    this.pixelSize.set(pixelSize);
    constraints.recalculate(this.pixelSize);
  }

  public void update() {
  }

  public void initialize() {
  }

  public boolean isPixelInside(float x, float y) {
    float rawX = x * pixelSize.x;
    float rawY = y * pixelSize.y;
    return isInside(rawX, rawY);
  }

  public boolean isInside(float rawX, float rawY) {
    Vector4f bounds = bounds();
    float xMin = bounds.x;
    float yMin = bounds.y;
    float xMax = xMin + bounds.z;
    float yMax = yMin + bounds.w;
    return rawX <= xMax && rawX >= xMin && rawY <= yMax && rawY >= yMin;
  }

  public Vector4f bounds() {
    return constraints.bounds();
  }

  public ElementConstraints constraints() {
    return constraints;
  }

  public void setConstraints(ElementConstraints constraints) {
    this.constraints = constraints;
  }

  public Vector2f pixelSize() {
    return pixelSize;
  }
}
