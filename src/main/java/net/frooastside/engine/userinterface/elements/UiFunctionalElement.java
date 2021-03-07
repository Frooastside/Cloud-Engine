package net.frooastside.engine.userinterface.elements;

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
      if(element != null) {
        element.update(delta);
      }
    }
  }

  @Override
  public void recalculate(Vector2f pixelSize) {
    super.recalculate(pixelSize);
    for (UiElement element : children) {
      if (element != null) {
        element.recalculate(pixelSize);
      }
    }
  }

  public UiElement click(ClickEvent event) {
    if (event.inside()) {
      System.out.println("inside");
      for (UiElement element : children) {
        System.out.println("loop: " + element);
        if (element.isPixelInside(event.x(), event.y())) {
          System.out.println("child inside");
          if (element instanceof UiFunctionalElement) {
            System.out.println("child functional");
            UiFunctionalElement basicElement = (UiFunctionalElement) element;
            if (basicElement.clickable()) {
              System.out.println("child clickable");
              if (((ClickEvent.Listener) basicElement).handleClick(event)) {
                System.out.println("child clicked");
                if (basicElement.selectable()) {
                  System.out.println("child selectable");
                  return element;
                }
              }
            }else if (basicElement.selectable()) {
              System.out.println("selectable");
              return basicElement;
            }else {
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
    children.add(element);
    element.constraints().setParent(constraints());
    element.initialize();
    element.recalculate(pixelSize());
    if (element instanceof UiFunctionalElement) {
      ((UiFunctionalElement) element).setRoot(root);
    }
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
