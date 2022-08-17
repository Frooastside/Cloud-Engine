package love.polardivision.engine.userinterface.elements;

import java.util.ArrayList;
import java.util.List;
import love.polardivision.engine.glwrapper.NativeObject;
import love.polardivision.engine.userinterface.Screen;
import love.polardivision.engine.userinterface.events.*;
import love.polardivision.engine.window.Key;
import love.polardivision.engine.window.Window;
import love.polardivision.engine.window.callbacks.KeyCallback;
import org.joml.Vector2f;
import org.lwjgl.util.yoga.YGLayout;
import org.lwjgl.util.yoga.YGNode;
import org.lwjgl.util.yoga.Yoga;

public abstract class FunctionalElement extends Element implements NativeObject {

  private YGNode node;

  private Overflow overflow = Overflow.INITIAL;

  private RenderElement background;

  private Screen root;
  private FunctionalElement parent;
  private final List<Element> children = new ArrayList<>();


  private String clickEventTarget;
  private String selectEventTarget;

  @Override
  public void initialize() {
    node = YGNode.create(Yoga.YGNodeNew());
  }

  @Override
  public void delete() {
    Yoga.YGNodeFree(node.address());
    node.free();
    for (Element element : children) {
      if (element instanceof FunctionalElement functionalElement) {
        functionalElement.delete();
      }
    }
  }

  @Override
  public void update(double delta) {
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

  public RenderElement background() {
    return background;
  }

  public void setBackground(RenderElement background) {
    this.background = background;
  }

  public void calculateChildBounds() {
    if (background != null) {
      background.bounds().set(this.bounds());
    }
  }

  public void calculateBounds() {
    YGLayout layout = node.layout();
    if (node.hasNewLayout()) {
      float x = layout.positions(0);
      float y = layout.positions(1);
      float width = layout.dimensions(0);
      float height = layout.dimensions(1);
      bounds().set(x * pixelSize().x, y * pixelSize().y, width * pixelSize().x, height * pixelSize().y);
    }
  }

  public void handleKey(Window window, Key key, int scancode, int modifiers, KeyCallback.Action action) {
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

  public Element scroll(ScrollEvent event) {
    if (event.inside()) {
      for (Element element : children) {
        if (element.isPixelInside(event.x(), event.y())) {
          if (element instanceof FunctionalElement basicElement) {
            if (basicElement.scrollable()) {
              if (((ScrollEvent.Handler) basicElement).handleScroll(event)) {
                if (basicElement.selectable()) {
                  return element;
                }
              } else {
                return basicElement.scroll(event);
              }
            } else if (basicElement.selectable()) {
              return basicElement;
            } else {
              return basicElement.scroll(event);
            }
          }
        } else {
          if (element instanceof FunctionalElement basicElement) {
            ScrollEvent outsideScrollEvent = new ScrollEvent(false, event.x(), event.y(), event.scrollX(), event.scrollY());
            basicElement.scroll(outsideScrollEvent);
            if (basicElement.scrollable()) {
              ((ScrollEvent.Handler) basicElement).handleScroll(outsideScrollEvent);
            }
          }
        }
      }
    } else {
      for (Element element : children) {
        if (element instanceof FunctionalElement basicElement) {
          if (!element.isPixelInside(event.x(), event.y())) {
            basicElement.scroll(event);
            if (basicElement.scrollable()) {
              ((ScrollEvent.Handler) basicElement).handleScroll(event);
            }
          } else {
            ScrollEvent insideScrollEvent = new ScrollEvent(true, event.x(), event.y(), event.scrollX(), event.scrollY());
            basicElement.scroll(insideScrollEvent);
            if (basicElement.scrollable()) {
              ((ScrollEvent.Handler) basicElement).handleScroll(insideScrollEvent);
            }
          }
        }
      }
    }
    return null;
  }

  public Element click(ClickEvent event) {
    if (event.inside()) {
      for (Element element : children) {
        if (element.isPixelInside(event.x(), event.y())) {
          if (element instanceof FunctionalElement basicElement) {
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
          if (element instanceof FunctionalElement basicElement) {
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
        if (element instanceof FunctionalElement basicElement) {
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
        element.display(show, parentDelay);
      }
    }
  }

  public void emitEvent(Event event, Class<? extends EventHandler> type, String targets) {
    root().emitEvent(event, type, targets);
  }

  public void addElement(Element element) {
    addElement(element, Yoga.YGNodeGetChildCount(node.address()));
  }

  public void addElement(Element element, int index) {
    children.add(element);
    element.updatePixelSize(pixelSize());
    if (element instanceof FunctionalElement functionalElement) {
      Yoga.YGNodeInsertChild(node.address(), functionalElement.node().address(), index);
      functionalElement.setRoot(root());
      functionalElement.setParent(this);
      functionalElement.calculateBounds();
    }
  }

  public void removeElement(FunctionalElement functionalElement) {
    if (children.contains(functionalElement)) {
      Yoga.YGNodeRemoveChild(node.address(), functionalElement.node().address());
    }
  }

  public YGNode node() {
    return node;
  }

  public boolean hideOverflow() {
    return overflow == Overflow.HIDE || overflow == Overflow.SCROLL;
  }

  public Overflow overflow() {
    return overflow;
  }

  public void setOverflow(Overflow overflow) {
    this.overflow = overflow;
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

  public boolean scrollable() {
    return this instanceof ScrollEvent.Handler;
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

  public enum Overflow {

    SHOW, HIDE, SCROLL;

    public static final Overflow INITIAL = SHOW;

  }

}
