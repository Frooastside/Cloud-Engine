/*
 * Copyright 2022 @Frooastside
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package love.polardivision.engine.userinterface.elements;

import java.util.ArrayList;
import java.util.List;
import love.polardivision.engine.userinterface.Screen;
import love.polardivision.engine.userinterface.events.ClickEvent;
import love.polardivision.engine.userinterface.events.Event;
import love.polardivision.engine.userinterface.events.EventHandler;
import love.polardivision.engine.userinterface.events.ScrollEvent;
import love.polardivision.engine.userinterface.events.SelectionEvent;
import love.polardivision.engine.utils.NativeObject;
import love.polardivision.engine.window.Key;
import love.polardivision.engine.window.Window;
import love.polardivision.engine.window.callbacks.KeyCallback;
import love.polardivision.engine.ygwrapper.Alignment;
import love.polardivision.engine.ygwrapper.Direction;
import love.polardivision.engine.ygwrapper.Display;
import love.polardivision.engine.ygwrapper.Justify;
import love.polardivision.engine.ygwrapper.NodeType;
import love.polardivision.engine.ygwrapper.Overflow;
import love.polardivision.engine.ygwrapper.PositionType;
import love.polardivision.engine.ygwrapper.Wrap;
import org.joml.Vector2f;
import org.lwjgl.util.yoga.YGLayout;
import org.lwjgl.util.yoga.YGNode;
import org.lwjgl.util.yoga.Yoga;

public abstract class FunctionalElement extends Element implements NativeObject {

  private final NodeType nodeType;
  private YGNode node;

  private Alignment alignSelf = Alignment.AUTO;
  private Alignment alignItems = Alignment.AUTO;
  private Alignment alignContent = Alignment.AUTO;
  private Direction direction = Direction.COLUMN;
  private Display display = Display.FLEX;
  private Justify justifyContent = Justify.START;
  private Overflow overflow = Overflow.VISIBLE;
  private PositionType positionType = PositionType.STATIC;
  private Wrap wrap = Wrap.NO_WRAP;

  private RenderElement background;

  private Screen root;
  private FunctionalElement parent;
  private final List<Element> children = new ArrayList<>();


  private String clickEventTarget;
  private String selectEventTarget;

  protected FunctionalElement(NodeType nodeType) {
    this.nodeType = nodeType;
  }


  @Override
  public void initialize() {
    node = YGNode.create(Yoga.YGNodeNew());
    node.nodeType(nodeType.value());
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

  public NodeType nodeType() {
    return nodeType;
  }

  public Alignment alignSelf() {
    return alignSelf;
  }

  public void alignSelf(Alignment alignment) {
    if(this.alignSelf != alignment) {
      this.alignSelf = alignment;
      Yoga.YGNodeStyleSetAlignSelf(node.address(), alignment.value());
    }
  }

  public Alignment alignItems() {
    return alignItems;
  }

  public void alignItems(Alignment alignment) {
    if(this.alignItems != alignment) {
      this.alignItems = alignment;
      Yoga.YGNodeStyleSetAlignItems(node.address(), alignment.value());
    }
  }

  public Alignment alignContent() {
    return alignContent;
  }

  public void alignContent(Alignment alignment) {
    if(this.alignContent != alignment) {
      this.alignContent = alignment;
      Yoga.YGNodeStyleSetAlignContent(node.address(), alignment.value());
    }
  }

  public Direction direction() {
    return direction;
  }

  public void setDirection(Direction direction) {
    if(this.direction != direction) {
      this.direction = direction;
      Yoga.YGNodeStyleSetFlexDirection(node.address(), direction.value());
    }
  }

  public Display display() {
    return display;
  }

  public void setDisplay(Display display) {
    if(this.display != display) {
      this.display = display;
      Yoga.YGNodeStyleSetDisplay(node.address(), display.value());
    }
  }

  public Justify justifyContent() {
    return justifyContent;
  }

  public void justifyContent(Justify justify) {
    if(this.justifyContent != justify) {
      this.justifyContent = justify;
      Yoga.YGNodeStyleSetJustifyContent(node.address(), justify.value());
    }
  }

  public Overflow overflow() {
    return overflow;
  }

  public void setOverflow(Overflow overflow) {
    if(this.overflow != overflow) {
      this.overflow = overflow;
      Yoga.YGNodeStyleSetOverflow(node.address(), overflow.value());
    }
  }

  public PositionType positionType() {
    return positionType;
  }

  public void setPositionType(PositionType positionType) {
    if(this.positionType != positionType) {
      this.positionType = positionType;
      Yoga.YGNodeStyleSetPositionType(node.address(), positionType.value());
    }
  }

  public Wrap wrap() {
    return wrap;
  }

  public void setWrap(Wrap wrap) {
    if(this.wrap != wrap) {
      this.wrap = wrap;
      Yoga.nYGNodeStyleSetFlexWrap(node.address(), wrap.value());
    }
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

}
