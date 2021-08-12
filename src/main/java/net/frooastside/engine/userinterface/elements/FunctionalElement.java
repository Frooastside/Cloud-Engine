package net.frooastside.engine.userinterface.elements;

import net.frooastside.engine.glfw.Window;
import net.frooastside.engine.glfw.callbacks.KeyCallback;
import net.frooastside.engine.userinterface.Screen;
import net.frooastside.engine.userinterface.constraints.Constraint;
import net.frooastside.engine.userinterface.constraints.ElementConstraints;
import net.frooastside.engine.userinterface.events.ClickEvent;
import net.frooastside.engine.userinterface.events.Event;
import net.frooastside.engine.userinterface.events.EventHandler;
import net.frooastside.engine.userinterface.events.SelectionEvent;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public abstract class FunctionalElement extends Element {

  private ElementConstraints sizeConstraints;
  private ElementConstraints spacingConstraints;

  private final Vector4f spacing = new Vector4f();

  private Screen root;
  private FunctionalElement parent;
  private final List<Element> children = new ArrayList<>();

  private String clickEventTarget;
  private String selectEventTarget;

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
    sizeConstraints.setPixelSize(pixelSize);
    spacingConstraints.setPixelSize(pixelSize);
    for (Element element : children) {
      if (element != null) {
        element.updatePixelSize(pixelSize);
      }
    }
  }

  public abstract void calculateChildBounds();

  public void calculateBounds(Vector4f parentInnerArea) {
    if (sizeConstraints != null) {
      float x = bounds().x;
      float y = bounds().y;
      calculateSpacing(parentInnerArea);
      if (sizeConstraints.x() != null && sizeConstraints.y() != null) {
        x = sizeConstraints.calculateValue(Constraint.ConstraintType.X, parentInnerArea);
        y = sizeConstraints.calculateValue(Constraint.ConstraintType.Y, parentInnerArea);
      } else if ((spacingConstraints.x() != null && spacingConstraints.y() != null)) {
        x = spacing().x;
        y = spacing().y;
      }
      float width = parentInnerArea.z - (spacing().x + spacing().z);
      if (sizeConstraints.z() != null) {
        width = sizeConstraints.calculateValue(Constraint.ConstraintType.Z, parentInnerArea);
      }
      float height = parentInnerArea.w - (spacing().y + spacing().w);
      if (sizeConstraints.w() != null) {
        height = sizeConstraints.calculateValue(Constraint.ConstraintType.W, parentInnerArea);
      }
      bounds().set(x, y, width, height);
    } else {
      if (spacingConstraints != null) {
        calculateSpacing(parentInnerArea);
      } else {
        bounds().set(0, 0, 1, 1);
      }
    }
  }

  private void calculateSpacing(Vector4f parentInnerArea) {
    spacing.set(
      spacingConstraints.calculateValue(Constraint.ConstraintType.X, parentInnerArea),
      spacingConstraints.calculateValue(Constraint.ConstraintType.Y, parentInnerArea),
      spacingConstraints.calculateValue(Constraint.ConstraintType.Z, parentInnerArea),
      spacingConstraints.calculateValue(Constraint.ConstraintType.W, parentInnerArea));
  }

  public void handleKey(Window window, int key, int scancode, int modifiers, KeyCallback.Action action) {
    for (Element element : children()) {
      if (element instanceof FunctionalElement) {
        ((FunctionalElement) element).handleKey(window, key, scancode, modifiers, action);
      }
    }
  }

  public void handleChar(Window window, char codepoint) {
    for (Element element : children()) {
      if (element instanceof FunctionalElement) {
        ((FunctionalElement) element).handleChar(window, codepoint);
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
              if (((ClickEvent.Handler) basicElement).handleClick(event)) {
                if (basicElement.selectable()) {
                  return element;
                }
              } else {
                return basicElement.click(event);
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
              ((ClickEvent.Handler) basicElement).handleClick(outsideClickEvent);
            }
          }
        }
      }
    } else {
      for (Element element : children) {
        if (element instanceof FunctionalElement) {
          FunctionalElement basicElement = (FunctionalElement) element;
          if (!element.isPixelInside(event.x(), event.y())) {
            basicElement.click(event);
            if (basicElement.clickable()) {
              ((ClickEvent.Handler) basicElement).handleClick(event);
            }
          } else {
            ClickEvent insideClickEvent = new ClickEvent(event.key(), true, event.pressed(), event.x(), event.y());
            basicElement.click(insideClickEvent);
            if (basicElement.clickable()) {
              ((ClickEvent.Handler) basicElement).handleClick(insideClickEvent);
            }
          }
        }
      }
    }
    return null;
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

  @Override
  public boolean doingDisplayAnimation() {
    if (super.doingDisplayAnimation()) {
      return true;
    } else {
      for (Element element : children()) {
        if (element.doingDisplayAnimation()) {
          return true;
        }
      }
    }
    return false;
  }

  public void emitEvent(Event event, Class<? extends EventHandler> type, String targets) {
    root().emitEvent(event, type, targets);
  }

  public void addElement(Element element) {
    children.add(element);
    element.updatePixelSize(pixelSize());
    element.setDisplayAnimation(displayAnimation(), displayAnimationDelay());
    if (element instanceof FunctionalElement) {
      FunctionalElement functionalElement = (FunctionalElement) element;
      functionalElement.setRoot(root());
      functionalElement.setParent(this);
      //TODO MAYBE RECALCULATE
      functionalElement.calculateBounds(bounds());
    }
  }

  public Vector4f spacing() {
    return spacing;
  }

  public ElementConstraints spacingConstraints() {
    return spacingConstraints;
  }

  public void setSpacingConstraints(ElementConstraints spacingConstraints) {
    this.spacingConstraints = spacingConstraints;
  }

  public ElementConstraints sizeConstraints() {
    return sizeConstraints;
  }

  public void setSizeConstraints(ElementConstraints sizeConstraints) {
    this.sizeConstraints = sizeConstraints;
  }

  public Screen root() {
    return root;
  }

  public void setRoot(Screen root) {
    this.root = root;
  }

  public Element parent() {
    return parent;
  }

  public void setParent(FunctionalElement parent) {
    this.parent = parent;
  }

  public List<Element> children() {
    return children;
  }

  public boolean selectable() {
    return this instanceof SelectionEvent.Handler;
  }

  public boolean clickable() {
    return this instanceof ClickEvent.Handler;
  }

  public String clickEventTarget() {
    return clickEventTarget;
  }

  public void setClickEventTarget(String clickEventTarget) {
    this.clickEventTarget = clickEventTarget;
  }

  public String selectEventTarget() {
    return selectEventTarget;
  }

  public void setSelectEventTarget(String selectEventTarget) {
    this.selectEventTarget = selectEventTarget;
  }

  /*



  private ClickEvent.Listener clickListener;
  private SelectionEvent.Listener selectionListener;

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
*/
}
