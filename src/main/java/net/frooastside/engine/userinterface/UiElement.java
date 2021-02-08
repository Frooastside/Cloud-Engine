package net.frooastside.engine.userinterface;

import net.frooastside.engine.glfw.callbacks.CharCallback;
import net.frooastside.engine.glfw.callbacks.KeyCallback;
import net.frooastside.engine.glfw.callbacks.MouseButtonCallback;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public abstract class UiElement implements MouseButtonCallback, KeyCallback, CharCallback {

  private ElementConstraints constraints;

  private UiElement root;
  private final List<UiElement> children = new ArrayList<>();

  private boolean invisibleSelected;

  public void onRecalculation(Vector2f pixelSize) {}

  public void recalculate(Vector2f pixelSize) {
    constraints.recalculate(pixelSize);
    onRecalculation(pixelSize);
    children.forEach(child -> child.recalculate(pixelSize));
  }

  public abstract void update();

  public void addElement(UiElement child, ElementConstraints constraints) {
    child.setConstraints(constraints);
    if(root != null) {
      child.root = root;
    }
    constraints.setParent(this.constraints);
    child.recalculate(this.constraints.pixelSize());
    children.add(child);
  }

  public UiElement checkContact(float x, float y) {
    if(isInside(x, y)) {
      if(children.isEmpty()) {
        invisibleSelected = true;
        onContact();
        return this;
      }else {
        invisibleSelected = true;
        for(UiElement child : children) {
          UiElement selectedItem = child.checkContact(x, y);
          if(selectedItem != null) {
            invisibleSelected = false;
            return selectedItem;
          }
        }
        return this;
      }
    }else {
      for(UiElement child : children) {
        child.checkContact(x, y);
      }
      if(invisibleSelected) {
        invisibleSelected = false;
        onLoseContact();
      }
      return null;
    }
  }

  public abstract void onContact();

  public abstract void onLoseContact();

  public boolean isInside(float x, float y) {
    Vector4f bounds = bounds();
    float xMin = bounds.x;
    float yMin = bounds.y;
    float xMax = xMin + bounds.z;
    float yMax = yMin + bounds.w;
    return x <= xMax && x >= xMin && y <= yMax && y >= yMin;
  }

  public void addRenderElements(UiElement element) {
    if(root != null) {
      root.addRenderElements(element);
    }
  }

  public abstract UiRenderElement[] renderElements();

  public UiElement highlightedElement() {
    if(root != null) {
      return root.highlightedElement();
    }
    return null;
  }

  public void setHighlighted(UiElement element) {
    if(root != null) {
      root.setHighlighted(element);
    }
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

  public UiElement root() {
    return root;
  }

  public void setRoot(UiElement root) {
    this.root = root;
  }

  public List<UiElement> children() {
    return children;
  }

  public boolean invisibleSelected() {
    return invisibleSelected;
  }
}
