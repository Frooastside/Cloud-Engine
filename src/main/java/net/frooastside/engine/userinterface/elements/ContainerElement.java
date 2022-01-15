package net.frooastside.engine.userinterface.elements;

import net.frooastside.engine.language.I18n;
import net.frooastside.engine.userinterface.constraints.Constraint;
import net.frooastside.engine.userinterface.constraints.ElementConstraints;
import net.frooastside.engine.userinterface.events.ClickEvent;
import net.frooastside.engine.userinterface.events.ScrollEvent;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ContainerElement extends FunctionalElement implements ScrollEvent.Handler {

  private final Vector4f clearance = new Vector4f();
  private final Vector4f innerArea = new Vector4f(0, 0, 1, 1);

  private ElementConstraints clearanceConstraints;

  private FlowDirection flowDirection = FlowDirection.INITIAL;
  private ContentAlignment contentAlignment = ContentAlignment.INITIAL;
  private ItemAlignment itemAlignment = ItemAlignment.INITIAL;

  private final Vector2f scroll = new Vector2f();
  private final Vector2f maxScroll = new Vector2f();

  private final List<FunctionalElement> temporaryAutoPositionChildren = new ArrayList<>();

  @Override
  public void updatePixelSize(Vector2f pixelSize) {
    super.updatePixelSize(pixelSize);
    clearanceConstraints.setPixelSize(pixelSize);
  }

  @Override
  public void calculateChildBounds() {
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
    for (FunctionalElement child : temporaryAutoPositionChildren) {
      if (flowDirection == FlowDirection.HORIZONTAL) {
        totalSize += child.bounds().z + child.spacing().x + child.spacing().z;
      } else if (flowDirection == FlowDirection.VERTICAL) {
        totalSize += child.bounds().w + child.spacing().y + child.spacing().w;
      }
    }

    calculateMaxScroll(totalSize);
    calculateInnerArea();

    if (contentAlignment != ContentAlignment.DEFAULT) {
      if (flowDirection == FlowDirection.HORIZONTAL && totalSize > innerArea.z) {
        contentAlignment = ContentAlignment.DEFAULT;
      } else if (flowDirection == FlowDirection.VERTICAL && totalSize > innerArea.w) {
        contentAlignment = ContentAlignment.DEFAULT;
      }
    }

    AtomicReference<Float> offset = new AtomicReference<>();
    float spacing = calculateSpacing(totalSize, true);
    offset.set(spacing);

    spacing = calculateSpacing(totalSize, false);
    for (FunctionalElement child : temporaryAutoPositionChildren) {
      handleItemAlignment(child);
      handleContentAlignment(child, offset, spacing);
    }
    for (Element element : children()) {
      if (element instanceof FunctionalElement) {
        FunctionalElement functionalElement = (FunctionalElement) element;
        functionalElement.calculateChildBounds();
      }
    }
  }

  @Override
  public boolean handleScroll(ScrollEvent event) {
    if(event.inside() && overflow() == Overflow.SCROLL) {
      float newScrollX = (scroll.x - event.scrollX() * 15);
      float newScrollY = (scroll.y - event.scrollY() * 15);
      scroll.x = Math.max(Math.min(newScrollX * pixelSize().x, maxScroll.x - innerArea.z), 0) / pixelSize().x;
      scroll.y = Math.max(Math.min(newScrollY * pixelSize().y, maxScroll.y - innerArea.w), 0) / pixelSize().y;
      calculateChildBounds();
      return true;
    }
    return false;
  }

  private float calculateSpacing(float totalSize, boolean firstSpace) {
    switch (contentAlignment) {
      case SPACED_EVENLY:
        if (flowDirection == FlowDirection.HORIZONTAL) {
          return (innerArea.z - totalSize) / (temporaryAutoPositionChildren.size() + 1);
        } else if (flowDirection == FlowDirection.VERTICAL) {
          return (innerArea.w - totalSize) / (temporaryAutoPositionChildren.size() + 1);
        }
        throw new IllegalStateException(I18n.get("error.userinterface.unknowndirection", flowDirection));
      case SPACE_AROUND:
        if (firstSpace) {
          if (flowDirection == FlowDirection.HORIZONTAL) {
            return (innerArea.z - totalSize) / 2;
          } else if (flowDirection == FlowDirection.VERTICAL) {
            return (innerArea.w - totalSize) / 2;
          }
        } else {
          return 0f;
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
      child.bounds().x = innerArea.x + currentOffset + child.spacing().x;
      offset.set(currentOffset + child.bounds().z + child.spacing().x + child.spacing().z + spacing);
    } else if (flowDirection == FlowDirection.VERTICAL) {
      child.bounds().y = innerArea.y + currentOffset + child.spacing().y;
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
      default:
        if (flowDirection == FlowDirection.HORIZONTAL) {
          child.bounds().y = innerArea.y + child.spacing().y;
        } else if (flowDirection == FlowDirection.VERTICAL) {
          child.bounds().x = innerArea.x + child.spacing().x;
        }
        break;
    }
  }

  private void calculateMaxScroll(float totalSize) {
    if(scrollable()) {
      maxScroll.set(
        flowDirection == FlowDirection.HORIZONTAL ? totalSize : 0,
        flowDirection == FlowDirection.VERTICAL ? totalSize : 0);
    }
  }

  private void calculateInnerArea() {
    if (clearanceConstraints != null) {
      clearance.set(
        clearanceConstraints.calculateValue(Constraint.ConstraintType.X, bounds()),
        clearanceConstraints.calculateValue(Constraint.ConstraintType.Y, bounds()),
        clearanceConstraints.calculateValue(Constraint.ConstraintType.Z, bounds()),
        clearanceConstraints.calculateValue(Constraint.ConstraintType.W, bounds()));
      innerArea.set(
        bounds().x + clearance.x + -scroll.x * pixelSize().x,
        bounds().y + clearance.y + -scroll.y * pixelSize().y,
        bounds().z - (clearance.x + clearance.z),
        bounds().w - (clearance.y + clearance.w));
    } else {
      clearance.set(0, 0, 0, 0);
    }
  }

  @Override
  public boolean scrollable() {
    return super.scrollable() && overflow() == Overflow.SCROLL;
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

  public enum FlowDirection {

    HORIZONTAL, VERTICAL;

    public static FlowDirection INITIAL = HORIZONTAL;

  }

  public enum ContentAlignment {

    DEFAULT, SPACE_AROUND, /*SPACE_BETWEEN,*/ SPACED_EVENLY;

    public static ContentAlignment INITIAL = DEFAULT;

  }

  public enum ItemAlignment {

    START, CENTER, END;

    public static ItemAlignment INITIAL = START;

  }

}
