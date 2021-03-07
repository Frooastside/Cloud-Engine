package net.frooastside.engine.userinterface.elements;

import net.frooastside.engine.userinterface.UiConstraints;
import net.frooastside.engine.userinterface.events.ClickEvent;
import net.frooastside.engine.userinterface.events.SelectionEvent;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public abstract class UiFunctionalElement extends UiElement {

  private final List<UiElement> children = new ArrayList<>();

  private ClickEvent.Listener clickListener;
  private SelectionEvent.Listener selectionListener;

  private UiFunctionalElement root;

  @Override
  public void update(double delta) {
    super.update(delta);
    for (UiElement element : children) {
      if (element != null) {
        element.update(delta);
      }
    }
  }

  @Override
  public void recalculateElement() {
    super.recalculateElement();
    for (UiElement element : children) {
      if (element != null) {
        element.recalculateElement();
      }
    }
  }

  @Override
  public void updatePixelSize(Vector2f pixelSize) {
    super.updatePixelSize(pixelSize);
    for (UiElement element : children) {
      if (element != null) {
        element.updatePixelSize(pixelSize);
      }
    }
  }

  public UiElement click(ClickEvent event) {
    if (event.inside()) {
      for (UiElement element : children) {
        if (element.isPixelInside(event.x(), event.y())) {
          if (element instanceof UiFunctionalElement) {
            UiFunctionalElement basicElement = (UiFunctionalElement) element;
            if (basicElement.clickable()) {
              if (((ClickEvent.Listener) basicElement).handleClick(event)) {
                if (basicElement.selectable()) {
                  return element;
                }
              }
            } else if (basicElement.selectable()) {
              return basicElement;
            } else {
              return basicElement.click(event);
            }
          }
        } else {
          if (element instanceof UiFunctionalElement) {
            UiFunctionalElement basicElement = (UiFunctionalElement) element;
            ClickEvent outsideClickEvent = new ClickEvent(event.key(), false, event.pressed(), event.x(), event.y());
            basicElement.click(outsideClickEvent);
            if (basicElement.clickable()) {
              ((ClickEvent.Listener) basicElement).handleClick(outsideClickEvent);
            }
          }
        }
      }
    } else {
      for (UiElement element : children) {
        if (element instanceof UiFunctionalElement) {
          UiFunctionalElement basicElement = (UiFunctionalElement) element;
          basicElement.click(event);
          if (basicElement.clickable()) {
            ((ClickEvent.Listener) basicElement).handleClick(event);
          }
        }
      }
    }
    return null;
  }

  public void addElement(UiElement element) {
    addElement(element, UiConstraints.getDefault());
  }

  public void addElement(UiElement element, UiConstraints constraints) {
    children.add(element);

    element.setParent(this);
    if (element instanceof UiFunctionalElement) {
      ((UiFunctionalElement) element).setRoot(root);
    }

    element.setConstraints(constraints);
    constraints.initialize(element, this);

    element.initialize();
    element.updatePixelSize(pixelSize());
    element.recalculateElement();
  }

  public List<UiElement> children() {
    return children;
  }

  public UiFunctionalElement root() {
    return root;
  }

  public void setRoot(UiFunctionalElement root) {
    this.root = root;
  }

  public boolean clickable() {
    return this instanceof ClickEvent.Listener;
  }

  public boolean selectable() {
    return this instanceof SelectionEvent.Listener;
  }

  public boolean hasClickListener() {
    return clickable() && clickListener != null;
  }

  public boolean hasSelectionListener() {
    return selectable() && selectionListener != null;
  }

  public ClickEvent.Listener clickListener() {
    return clickListener;
  }

  public void setClickListener(ClickEvent.Listener clickListener) {
    this.clickListener = clickListener;
  }

  public SelectionEvent.Listener selectionListener() {
    return selectionListener;
  }

  public void setSelectionListener(SelectionEvent.Listener selectionListener) {
    this.selectionListener = selectionListener;
  }
}
