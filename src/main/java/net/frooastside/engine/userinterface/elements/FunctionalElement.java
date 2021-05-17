package net.frooastside.engine.userinterface.elements;

import net.frooastside.engine.language.I18n;
import net.frooastside.engine.userinterface.constraints.Constraint;
import net.frooastside.engine.userinterface.constraints.ElementConstraints;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public abstract class FunctionalElement extends Element {

  private final Vector4f spacing = new Vector4f();
  private ElementConstraints spacingConstraints = ElementConstraints.getDefault();
  private ElementConstraints sizeConstraints = ElementConstraints.getDefault();

  private final List<Element> children = new ArrayList<>();

  public void calculateBounds(Vector4f parentInnerArea) {
    if (sizeConstraints != null) {
      if (sizeConstraints.containsMaxValueConstraint()) {
        throw new IllegalStateException(I18n.get("error.userinterface.unallowedmvconstraint"));
      }
      float x = bounds().x;
      float y = bounds().y;
      if (sizeConstraints.x() != null && sizeConstraints.y() != null) {
        x = sizeConstraints.calculateValue(Constraint.ConstraintType.X, parentInnerArea);
        y = sizeConstraints.calculateValue(Constraint.ConstraintType.Y, parentInnerArea);
      }
      calculateSpacing(parentInnerArea);
      float width = calculateHorizontalMaxValue(parentInnerArea);
      float height = calculateVerticalMaxValue(parentInnerArea);
      bounds().set(x, y, width, height);
    } else {
      if (spacingConstraints != null) {
        if (spacingConstraints.containsMaxValueConstraint()) {
          throw new IllegalStateException(I18n.get("error.userinterface.unallowedmvconstraint"));
        }
        calculateSpacing(parentInnerArea);
      } else {
        bounds().set(0, 0, 1, 1);
      }
    }
  }

  private float calculateHorizontalMaxValue(Vector4f parentInnerArea) {
    if (Float.isNaN(spacing.x) || Float.isNaN(spacing.z)) {
      if (sizeConstraints.z() == null) {
        throw new IllegalStateException(I18n.get("error.userinterface.distanceofzero"));
      }
      float width = sizeConstraints.calculateValue(Constraint.ConstraintType.Z, parentInnerArea);
      if (Float.isNaN(spacing.x)) {
        if (Float.isNaN(spacing.z)) {
          float maxDistance = parentInnerArea.z - width / 2;
          spacing.z = maxDistance;
          spacing.x = maxDistance;
        } else {
          spacing.x = parentInnerArea.z - (width + spacing.z);
        }
      } else {
        if (Float.isNaN(spacing.z)) {
          spacing.z = parentInnerArea.z - (width + spacing.x);
        }
      }
      return width;
    }
    float width = parentInnerArea.z - (spacing().x + spacing().z);
    if (sizeConstraints.z() != null) {
      width = sizeConstraints.calculateValue(Constraint.ConstraintType.Z, parentInnerArea);
    }
    return width;
  }

  private float calculateVerticalMaxValue(Vector4f parentInnerArea) {
    if (Float.isNaN(spacing.y) || Float.isNaN(spacing.w)) {
      if (sizeConstraints.w() == null) {
        throw new IllegalStateException(I18n.get("error.userinterface.distanceofzero"));
      }
      float height = sizeConstraints.calculateValue(Constraint.ConstraintType.W, parentInnerArea);
      if (Float.isNaN(spacing.y)) {
        if (Float.isNaN(spacing.w)) {
          float maxDistance = parentInnerArea.w - height / 2;
          spacing.w = maxDistance;
          spacing.y = maxDistance;
        } else {
          spacing.y = parentInnerArea.w - (height + spacing.w);
        }
      } else {
        if (Float.isNaN(spacing.w)) {
          spacing.w = parentInnerArea.w - (height + spacing.y);
        }
      }
      return height;
    }
    float height = parentInnerArea.w - (spacing().y + spacing().w);
    if (sizeConstraints.w() != null) {
      height = sizeConstraints.calculateValue(Constraint.ConstraintType.W, parentInnerArea);
    }
    return height;
  }

  private void calculateSpacing(Vector4f parentInnerArea) {
    spacing.set(
      spacingConstraints.calculateValue(Constraint.ConstraintType.X, parentInnerArea),
      spacingConstraints.calculateValue(Constraint.ConstraintType.Y, parentInnerArea),
      spacingConstraints.calculateValue(Constraint.ConstraintType.Z, parentInnerArea),
      spacingConstraints.calculateValue(Constraint.ConstraintType.W, parentInnerArea));
  }

  public void addElement(Element element) {
    //TODO ADD ELEMENT
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

  public List<Element> children() {
    return children;
  }

  /*

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

  @Override
  public boolean doingDisplayAnimation() {
    if(super.doingDisplayAnimation()) {
      return true;
    }else {
      for(Element element : children()) {
        if(element.doingDisplayAnimation()) {
          return true;
        }
      }
    }
    return false;
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

    element.display(visible(), displayAnimationDelay());
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
  }*/
}
