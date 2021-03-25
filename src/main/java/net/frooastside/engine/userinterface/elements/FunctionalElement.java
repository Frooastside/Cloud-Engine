package net.frooastside.engine.userinterface.elements;

import net.frooastside.engine.userinterface.constraints.ElementConstraints;
import net.frooastside.engine.userinterface.events.ClickEvent;
import net.frooastside.engine.userinterface.events.SelectionEvent;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public abstract class FunctionalElement extends Element {

  private final List<Element> children = new ArrayList<>();

  private FunctionalElement root;

  private ClickEvent.Listener clickListener;
  private SelectionEvent.Listener selectionListener;

  private boolean hideOverflow;

  @Override
  public void update(double delta) {
    super.update(delta);
    for (Element element : children) {
      if (element != null) {
        element.update(delta);
      }
    }
  }

  @Override
  public void updatePixelSize(Vector2f pixelSize) {
    super.updatePixelSize(pixelSize);
    for (Element element : children) {
      if (element != null) {
        element.updatePixelSize(pixelSize);
      }
    }
  }

  @Override
  public void recalculateBounds() {
    super.recalculateBounds();
    for (Element element : children) {
      if (element != null) {
        element.recalculateBounds();
      }
    }
  }

  @Override
  public void display(boolean show, float parentDelay) {
    super.display(show, parentDelay);
    for (Element element : children) {
      if (element != null) {
        element.display(show, parentDelay + displayAnimationDelay());
      }
    }
  }

  public Element click(ClickEvent event) {
    if (event.inside()) {
      for (Element element : children) {
        if (element.isPixelInside(event.x(), event.y())) {
          if (element instanceof FunctionalElement) {
            FunctionalElement basicElement = (FunctionalElement) element;
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
          if (element instanceof FunctionalElement) {
            FunctionalElement basicElement = (FunctionalElement) element;
            ClickEvent outsideClickEvent = new ClickEvent(event.key(), false, event.pressed(), event.x(), event.y());
            basicElement.click(outsideClickEvent);
            if (basicElement.clickable()) {
              ((ClickEvent.Listener) basicElement).handleClick(outsideClickEvent);
            }
          }
        }
      }
    } else {
      for (Element element : children) {
        if (element instanceof FunctionalElement) {
          FunctionalElement basicElement = (FunctionalElement) element;
          basicElement.click(event);
          if (basicElement.clickable()) {
            ((ClickEvent.Listener) basicElement).handleClick(event);
          }
        }
      }
    }
    return null;
  }

  public void addElement(Element element) {
    addElement(element, ElementConstraints.getDefault());
  }

  public void addElement(Element element, ElementConstraints constraints) {
    children.add(element);

    element.setParent(this);
    if (element instanceof FunctionalElement) {
      ((FunctionalElement) element).setRoot(root);
    }

    element.setConstraints(constraints);
    constraints.initialize(element, this);

    element.initialize();
    element.updatePixelSize(pixelSize());
    element.recalculateBounds();
  }

  public List<Element> children() {
    return children;
  }

  public FunctionalElement root() {
    return root;
  }

  public void setRoot(FunctionalElement root) {
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

  public boolean hideOverflow() {
    return hideOverflow;
  }

  public void setHideOverflow(boolean hideOverflow) {
    this.hideOverflow = hideOverflow;
  }
}
