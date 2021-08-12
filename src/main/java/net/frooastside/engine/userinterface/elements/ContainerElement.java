package net.frooastside.engine.userinterface.elements;

import net.frooastside.engine.language.I18n;
import net.frooastside.engine.userinterface.constraints.Constraint;
import net.frooastside.engine.userinterface.constraints.ElementConstraints;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ContainerElement extends FunctionalElement {

  private final Vector4f clearance = new Vector4f();
  private final Vector4f innerArea = new Vector4f(0, 0, 1, 1);

  private ElementConstraints clearanceConstraints;

  private Overflow overflow;
  private FlowDirection flowDirection;
  private ContentAlignment contentAlignment;
  private ItemAlignment itemAlignment;

  private final List<FunctionalElement> temporaryAutoPositionChildren = new ArrayList<>();

  @Override
  public void updatePixelSize(Vector2f pixelSize) {
    super.updatePixelSize(pixelSize);
    clearanceConstraints.setPixelSize(pixelSize);
  }

  @Override
  public void calculateChildBounds() {
    calculateInnerArea();
    temporaryAutoPositionChildren.clear();
    for (Element element : children()) {
      if (element instanceof FunctionalElement) {
        FunctionalElement functionalElement = (FunctionalElement) element;
        functionalElement.calculateBounds(bounds());
        boolean overrideAutoPosition = functionalElement.sizeConstraints().x() != null && functionalElement.sizeConstraints().y() != null;
        if (!overrideAutoPosition) {
          temporaryAutoPositionChildren.add(functionalElement);
        }
      }
    }

    float totalSize = 0;
    for(FunctionalElement child : temporaryAutoPositionChildren) {
      if (flowDirection == FlowDirection.HORIZONTAL) {
        totalSize += child.bounds().z + child.spacing().x + child.spacing().z;
      } else if (flowDirection == FlowDirection.VERTICAL) {
        totalSize += child.bounds().w + child.spacing().y + child.spacing().w;
      }
    }

    if(contentAlignment != ContentAlignment.DEFAULT) {
      if (flowDirection == FlowDirection.HORIZONTAL && totalSize > innerArea.z) {
        contentAlignment = ContentAlignment.DEFAULT;
      } else if (flowDirection == FlowDirection.VERTICAL && totalSize > innerArea.w) {
        contentAlignment = ContentAlignment.DEFAULT;
      }
    }

    AtomicReference<Float> offset = new AtomicReference<>();
    float spacing = calculateSpacing(totalSize);
    offset.set(spacing);

    for (FunctionalElement child : temporaryAutoPositionChildren) {
      handleItemAlignment(child);
      handleContentAlignment(child, offset, spacing);
      child.calculateChildBounds();
    }
  }

  private float calculateSpacing(float totalSize) {
    switch (contentAlignment) {
      case SPACED_EVENLY:
        if (flowDirection == FlowDirection.HORIZONTAL) {
          return (innerArea.z - totalSize) / temporaryAutoPositionChildren.size() + 1;
        } else if (flowDirection == FlowDirection.VERTICAL) {
          return (innerArea.w - totalSize) / temporaryAutoPositionChildren.size() + 1;
        }
        throw new IllegalStateException(I18n.get("error.userinterface.unknowndirection", flowDirection));
      case SPACE_AROUND:
        if (flowDirection == FlowDirection.HORIZONTAL) {
          return (innerArea.z - totalSize) / 2;
        } else if (flowDirection == FlowDirection.VERTICAL) {
          return (innerArea.w - totalSize) / 2;
        }
        throw new IllegalStateException(I18n.get("error.userinterface.unknowndirection", flowDirection));
      case DEFAULT:
      default:
        return 0f;
    }
  }

  private void handleContentAlignment(FunctionalElement child, AtomicReference<Float> offset, float spacing) {
    float currentOffset = offset.get();
    if (flowDirection == FlowDirection.HORIZONTAL) {
      child.bounds().x = currentOffset + child.spacing().x;
      offset.set(currentOffset + child.bounds().z + child.spacing().x + child.spacing().z + spacing);
    } else if (flowDirection == FlowDirection.VERTICAL) {
      child.bounds().y = currentOffset + child.spacing().y;
      offset.set(currentOffset + child.bounds().w + child.spacing().y + child.spacing().w + spacing);
    }
  }

  private void handleItemAlignment(FunctionalElement child) {
    switch (itemAlignment) {
      case END:
        if (flowDirection == FlowDirection.HORIZONTAL) {
          child.bounds().y = (innerArea.y + innerArea.w) - (child.bounds().w + child.spacing().y);
        } else if (flowDirection == FlowDirection.VERTICAL) {
          child.bounds().x = (innerArea.x + innerArea.z) - (child.bounds().z + child.spacing().z);
        }
        break;
      case CENTER:
        if (flowDirection == FlowDirection.HORIZONTAL) {
          child.bounds().y = innerArea.y + ((innerArea.w - child.bounds().w) / 2);
        } else if (flowDirection == FlowDirection.VERTICAL) {
          child.bounds().x = innerArea.x + ((innerArea.z - child.bounds().z) / 2);
        }
        break;
      case START:
      case DEFAULT:
      default:
        if (flowDirection == FlowDirection.HORIZONTAL) {
          child.bounds().y = innerArea.y + child.spacing().y;
        } else if (flowDirection == FlowDirection.VERTICAL) {
          child.bounds().x = innerArea.x + child.spacing().x;
        }
        break;
    }
  }

  private void calculateInnerArea() {
    if (clearanceConstraints != null) {
      clearance.set(
        clearanceConstraints.calculateValue(Constraint.ConstraintType.X, bounds()),
        clearanceConstraints.calculateValue(Constraint.ConstraintType.Y, bounds()),
        clearanceConstraints.calculateValue(Constraint.ConstraintType.Z, bounds()),
        clearanceConstraints.calculateValue(Constraint.ConstraintType.W, bounds()));
      bounds().sub(clearance.add(0, 0, clearance.x, clearance.y, new Vector4f()), innerArea);
    } else {
      clearance.set(0, 0, 0, 0);
    }
  }

  public boolean hideOverflow() {
    return overflow == Overflow.HIDE || overflow == Overflow.SCROLL;
  }

  public Vector4f clearance() {
    return clearance;
  }

  public Vector4f innerArea() {
    return innerArea;
  }

  public ElementConstraints clearanceConstraints() {
    return clearanceConstraints;
  }

  public void setClearanceConstraints(ElementConstraints clearanceConstraints) {
    this.clearanceConstraints = clearanceConstraints;
  }

  public Overflow overflow() {
    return overflow;
  }

  public void setOverflow(Overflow overflow) {
    this.overflow = overflow;
  }

  public FlowDirection flowDirection() {
    return flowDirection;
  }

  public void setFlowDirection(FlowDirection flowDirection) {
    this.flowDirection = flowDirection;
  }

  public ContentAlignment contentAlignment() {
    return contentAlignment;
  }

  public void setContentAlignment(ContentAlignment contentAlignment) {
    this.contentAlignment = contentAlignment;
  }

  public ItemAlignment itemAlignment() {
    return itemAlignment;
  }

  public void setItemAlignment(ItemAlignment itemAlignment) {
    this.itemAlignment = itemAlignment;
  }

  public enum Overflow {

    SHOW, HIDE, SCROLL

  }

  public enum FlowDirection {

    HORIZONTAL, VERTICAL

  }

  public enum ContentAlignment {

    DEFAULT, SPACE_AROUND, /*SPACE_BETWEEN,*/ SPACED_EVENLY

  }

  public enum ItemAlignment {

    DEFAULT, CENTER, START, END

  }

}
