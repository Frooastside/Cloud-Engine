/*
 * Copyright © 2022-2023 @Frooastside
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
import love.polardivision.engine.window.Key;
import love.polardivision.engine.window.Window;
import love.polardivision.engine.window.callbacks.KeyCallback;
import love.polardivision.engine.wrappers.NativeObject;
import love.polardivision.engine.wrappers.yoga.LayoutNode;
import love.polardivision.engine.wrappers.yoga.LayoutNode.Layout;
import love.polardivision.engine.wrappers.yoga.NodeType;
import org.joml.Vector2f;

public abstract class FunctionalElement extends Element implements NativeObject {

  private final LayoutNode layoutNode;

  private RenderElement background;

  private Screen root;
  private FunctionalElement parent;
  private final List<Element> children = new ArrayList<>();

  private String clickEventTarget;
  private String selectEventTarget;

  protected FunctionalElement(NodeType nodeType) {
    this.layoutNode = new LayoutNode(nodeType);
  }

  @Override
  public void initialize() {
    layoutNode.initialize();
    if (background != null && background instanceof NativeObject nativeBackground) {
      nativeBackground.initialize();
    }
  }

  @Override
  public void delete() {
    layoutNode.delete();
    if (background != null && background instanceof NativeObject nativeBackground) {
      nativeBackground.delete();
    }
    for (Element element : children) {
      if (element instanceof FunctionalElement functionalElement) {
        functionalElement.delete();
      }
    }
  }

  @Override
  public void update(double delta) {
    if (background != null) {
      background.update(delta);
    }
    for (Element element : children) {
      if (element != null) {
        element.update(delta);
      }
    }
  }

  @Override
  public void updatePixelSize(Vector2f pixelSize) {
    super.updatePixelSize(pixelSize);
    if (background != null) {
      background.updatePixelSize(pixelSize);
    }
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
    Layout layout = layoutNode.layout();
    float x = layout.position().x;
    float y = layout.position().y;
    float width = layout.dimensions().x;
    float height = layout.dimensions().y;
    bounds()
        .set(x * pixelSize().x, y * pixelSize().y, width * pixelSize().x, height * pixelSize().y);
  }

  public void handleKey(
      Window window, Key key, int scancode, int modifiers, KeyCallback.Action action) {
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
            ScrollEvent outsideScrollEvent =
                new ScrollEvent(false, event.x(), event.y(), event.scrollX(), event.scrollY());
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
            ScrollEvent insideScrollEvent =
                new ScrollEvent(true, event.x(), event.y(), event.scrollX(), event.scrollY());
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
            ClickEvent outsideClickEvent =
                new ClickEvent(event.key(), false, event.pressed(), event.x(), event.y());
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
            ClickEvent insideClickEvent =
                new ClickEvent(event.key(), true, event.pressed(), event.x(), event.y());
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
    if (background != null) {
      background.display(show, parentDelay);
    }
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
    addElement(element, layoutNode.childCount());
  }

  public void addElement(Element element, int index) {
    children.add(element);
    element.updatePixelSize(pixelSize());
    if (element instanceof FunctionalElement functionalElement) {
      layoutNode.insertChild(functionalElement.layoutNode(), index);
      functionalElement.setRoot(root());
      functionalElement.setParent(this);
      functionalElement.calculateBounds();
    }
  }

  public void removeElement(Element element) {
    if (children.contains(element)) {
      children.remove(element);
      if (element instanceof FunctionalElement functionalElement) {
        layoutNode.removeChild(functionalElement.layoutNode());
      }
    }
  }

  public LayoutNode layoutNode() {
    return layoutNode;
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
