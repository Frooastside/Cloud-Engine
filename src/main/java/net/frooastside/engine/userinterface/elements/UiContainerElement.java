package net.frooastside.engine.userinterface.elements;

import net.frooastside.engine.userinterface.ClickEvent;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public abstract class UiContainerElement extends UiBasicElement {

  private final List<UiElement> children = new ArrayList<>();
  private UiRootElement root;

  @Override
  public void update(double delta) {
    super.update(delta);
    for (UiElement element : children) {
      element.update(delta);
    }
  }

  @Override
  public void recalculate(Vector2f pixelSize) {
    super.recalculate(pixelSize);
    for (UiElement element : children) {
      element.recalculate(pixelSize);
    }
  }

  public UiElement click(ClickEvent event) {
    if (event.inside()) {
      for (UiElement element : children) {
        if (element.isPixelInside(event.x(), event.y())) {
          if (element instanceof UiBasicElement) {
            UiBasicElement basicElement = (UiBasicElement) element;
            if (basicElement instanceof UiContainerElement) {
              return ((UiContainerElement) basicElement).click(event);
            } else if (basicElement.clickable()) {
              if (((ClickEvent.Listener) basicElement).onClick(event)) {
                if (basicElement.selectable()) {
                  return element;
                }
              }
            } else if (basicElement.selectable()) {
              return basicElement;
            }
          }
        } else {
          if (element instanceof UiBasicElement) {
            UiBasicElement basicElement = (UiBasicElement) element;
            ClickEvent outsideClickEvent = new ClickEvent(event.key(), false, event.pressed(), event.x(), event.y());
            if (basicElement instanceof UiContainerElement) {
              ((UiContainerElement) basicElement).click(outsideClickEvent);
            } else if (basicElement.clickable()) {
              ((ClickEvent.Listener) basicElement).onClick(outsideClickEvent);
            }
          }
        }
      }
    } else {
      for (UiElement element : children) {
        if (element instanceof UiBasicElement) {
          UiBasicElement basicElement = (UiBasicElement) element;
          if (basicElement instanceof UiContainerElement) {
            ((UiContainerElement) basicElement).click(event);
          } else if (basicElement.clickable()) {
            ((ClickEvent.Listener) basicElement).onClick(event);
          }
        }
      }
    }
    return null;
  }

  public void addElement(UiElement element) {
    children.add(element);
    element.constraints().setParent(constraints());
    element.initialize();
    element.recalculate(pixelSize());
    if (element instanceof UiContainerElement) {
      ((UiContainerElement) element).setRoot(root);
    }
    if (root != null) {
      root.addLeafElement(element);
    }
  }

  public List<UiElement> children() {
    return children;
  }

  public void setRoot(UiRootElement root) {
    this.root = root;
  }
}
