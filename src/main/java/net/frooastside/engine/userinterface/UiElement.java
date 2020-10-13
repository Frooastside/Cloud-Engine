package net.frooastside.engine.userinterface;

import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public abstract class UiElement {

  private ElementConstraints constraints;

  private UiElement parent;
  private final List<UiElement> children = new ArrayList<>();

  public void onRecalculation(Vector2f pixelSize) {}

  public void recalculate(Vector2f pixelSize) {
    constraints.recalculate(pixelSize);
    onRecalculation(pixelSize);
    children.forEach(child -> child.recalculate(pixelSize));
  }

  public void addElement(UiElement child, ElementConstraints constraints) {
    child.setConstraints(constraints);
    child.parent = this;
    constraints.setParent(this.constraints);
    child.recalculate(this.constraints.pixelSize());
    appendRenderElements(child);
    children.add(child);
  }

  public void appendRenderElements(UiElement element) {
    parent.appendRenderElements(element);
  }

  public abstract UiRenderElement[] renderElements();

  public Vector4f bounds() {
    return constraints.bounds();
  }

  public ElementConstraints constraints() {
    return constraints;
  }

  public void setConstraints(ElementConstraints constraints) {
    this.constraints = constraints;
  }

  public UiElement parent() {
    return parent;
  }

  public void setParent(UiElement parent) {
    this.parent = parent;
  }

  public List<UiElement> children() {
    return children;
  }
}
