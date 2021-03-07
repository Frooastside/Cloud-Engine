package net.frooastside.engine.userinterface.elements;

import net.frooastside.engine.userinterface.UiConstraints;
import org.joml.Vector2f;
import org.joml.Vector4f;

public abstract class UiElement {

  private final Vector2f pixelSize = new Vector2f();

  private UiElement parent;
  private UiConstraints constraints;

  private final Vector4f bounds = new Vector4f(0, 0, 1, 1);
  private float alpha = 1.0f;

  public void recalculateElement() {
    calculateBounds();
  }

  public void updatePixelSize(Vector2f pixelSize) {
    this.pixelSize.set(pixelSize);
    constraints.setPixelSize(pixelSize);
  }

  public void calculateBounds() {
    float x = constraints.x().rawValue();
    float y = constraints.y().rawValue();
    float width = constraints.width().rawValue();
    float height = constraints.height().rawValue();
    bounds.set(
      (constraints.x().relative() ? x * parent.bounds().z : x) + parent.bounds().x,
      (constraints.y().relative() ? y * parent.bounds().w : y) + parent.bounds().y,
      constraints.width().relative() ? (width * parent.bounds().z) : width,
      constraints.height().relative() ? (height * parent.bounds().w) : height);
  }

  public void update(double delta) {
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

  public Vector2f pixelSize() {
    return pixelSize;
  }

  public Vector4f bounds() {
    return bounds;
  }

  public UiElement parent() {
    return parent;
  }

  public void setParent(UiElement parent) {
    this.parent = parent;
  }

  public UiConstraints constraints() {
    return constraints;
  }

  public void setConstraints(UiConstraints constraints) {
    this.constraints = constraints;
  }

  public float alpha() {
    return alpha;
  }

  public void setAlpha(float alpha) {
    this.alpha = alpha;
  }
}
