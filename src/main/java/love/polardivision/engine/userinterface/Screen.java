/*
 * Copyright © 2022-2023 @Frooastside
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package love.polardivision.engine.userinterface;

import love.polardivision.engine.userinterface.elements.Element;
import love.polardivision.engine.userinterface.elements.FunctionalElement;
import love.polardivision.engine.userinterface.events.*;
import love.polardivision.engine.window.Key;
import love.polardivision.engine.window.MouseButton;
import love.polardivision.engine.window.Window;
import love.polardivision.engine.window.callbacks.KeyCallback;
import love.polardivision.engine.wrappers.NativeObject;
import org.joml.Vector2f;
import org.lwjgl.util.yoga.YGNode;
import org.lwjgl.util.yoga.Yoga;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Screen implements NativeObject {

  private final List<FunctionalElement> children = new ArrayList<>();
  private final Map<String, EventHandler> eventHandlers = new HashMap<>();

  private YGNode node;
  private final Window window;
  private final Font font;
  private final ColorSet colorSet;

  private final Vector2f pixelSize = new Vector2f();
  private FunctionalElement selectedElement;

  public Screen(Window window, Font font, ColorSet colorSet) {
    this.window = window;
    this.font = font;
    this.colorSet = colorSet;
  }

  @Override
  public void initialize() {
    long config = Yoga.YGConfigGetDefault();
    node = YGNode.create(Yoga.YGNodeNewWithConfig(config));
    Yoga.YGConfigFree(config);
  }

  @Override
  public void delete() {
    Yoga.YGNodeFree(node.address());
    node.free();
  }

  public void recalculateScreen() {
    updatePixelSize(pixelSize().set(1f / window.resolution().x, 1f / window.resolution().y));
    Yoga.YGNodeCalculateLayout(node.address(), window.resolution().x, window.resolution().y, Yoga.YGDirectionLTR/*TODO*/);
    recalculateBounds();
  }

  public void updatePixelSize(Vector2f pixelSize) {
    for (FunctionalElement element : children()) {
      if (element != null) {
        element.updatePixelSize(pixelSize);
      }
    }
  }

  public void recalculateBounds() {
    for (FunctionalElement element : children()) {
      if (element != null) {
        element.calculateBounds();
        element.calculateChildBounds();
      }
    }
  }

  public void update() {
    for (FunctionalElement element : children()) {
      if (element != null) {
        element.update(window.delta());
      }
    }
  }

  public void display(boolean show, float delay) {
    for (FunctionalElement element : children) {
      if (element != null) {
        element.display(show, delay);
      }
    }
  }

  public void emitEvent(Event event, Class<? extends EventHandler> type, String targets) {
    if (targets != null) {
      for (String target : targets.split(" ")) {
        EventHandler eventHandler = eventHandlers.get(target);
        if (type.isInstance(eventHandler)) {
          eventHandler.handle(event);
        }
      }
    }
  }

  public void handleMouseButton(Window window, MouseButton button, boolean pressed) {
    if (window == this.window) {
      Vector2f mousePosition = window.input().mousePosition();
      handleClick(new ClickEvent(button, true, pressed, mousePosition.x, mousePosition.y));
    }
  }

  public void handleKey(Window window, Key key, int scancode, int modifiers, KeyCallback.Action action) {
    if (window == this.window) {
      for (FunctionalElement element : children()) {
        if (element != null) {
          element.handleKey(window, key, scancode, modifiers, action);
        }
      }
    }
  }

  public void handleChar(Window window, char codepoint) {
    if (window == this.window) {
      for (FunctionalElement element : children()) {
        if (element != null) {
          element.handleChar(window, codepoint);
        }
      }
    }
  }

  public void handleScroll(Window window, double scrollX, double scrollY) {
    if (window == this.window) {
      Vector2f mousePosition = window.input().mousePosition();
      handleScroll(new ScrollEvent(true, mousePosition.x, mousePosition.y, (float) scrollX, (float) scrollY));
    }
  }

  public void handleScroll(ScrollEvent event) {
    scrollChildren(event);
  }

  public Element scrollChildren(ScrollEvent event) {
    if (event.inside()) {
      for (FunctionalElement element : children) {
        if (element.isPixelInside(event.x(), event.y())) {
          if (!element.scrollable() || !((ScrollEvent.Handler) element).handleScroll(event)) {
            return element.scroll(event);
          }
        } else {
          ScrollEvent outsideScrollEvent = new ScrollEvent(false, event.x(), event.y(), event.scrollX(), event.scrollY());
          element.scroll(outsideScrollEvent);
          if (element.scrollable()) {
            ((ScrollEvent.Handler) element).handleScroll(outsideScrollEvent);
          }
        }
      }
    } else {
      for (FunctionalElement element : children) {
        if (!element.isPixelInside(event.x(), event.y())) {
          element.scroll(event);
          if (element.scrollable()) {
            ((ScrollEvent.Handler) element).handleScroll(event);
          }
        } else {
          ScrollEvent insideScrollEvent = new ScrollEvent(true, event.x(), event.y(), event.scrollX(), event.scrollY());
          element.scroll(insideScrollEvent);
          if (element.scrollable()) {
            ((ScrollEvent.Handler) element).handleScroll(insideScrollEvent);
          }
        }
      }
    }
    return null;
  }

  public void emulateClick(MouseButton button, boolean pressed, float x, float y) {
    handleClick(new ClickEvent(button, true, pressed, x, y));
  }

  public boolean handleClick(ClickEvent event) {
    Element selectedElement = clickChildren(event);
    if (this.selectedElement != selectedElement && event.pressed()) {
      if (this.selectedElement != null && this.selectedElement.selectable()) {
        ((SelectionEvent.Handler) this.selectedElement).handleSelection(new SelectionEvent(false));
      }
      if (selectedElement instanceof FunctionalElement) {
        if (((FunctionalElement) selectedElement).selectable()) {
          ((SelectionEvent.Handler) selectedElement).handleSelection(new SelectionEvent(true));
        }
        this.selectedElement = (FunctionalElement) selectedElement;
      } else {
        this.selectedElement = null;
      }
    }
    return selectedElement != null;
  }

  public Element clickChildren(ClickEvent event) {
    if (event.inside()) {
      for (FunctionalElement element : children) {
        if (element.isPixelInside(event.x(), event.y())) {
          if (!element.clickable() || !((ClickEvent.Handler) element).handleClick(event)) {
            return element.click(event);
          }
        } else {
          ClickEvent outsideClickEvent = new ClickEvent(event.key(), false, event.pressed(), event.x(), event.y());
          element.click(outsideClickEvent);
          if (element.clickable()) {
            ((ClickEvent.Handler) element).handleClick(outsideClickEvent);
          }
        }
      }
    } else {
      for (FunctionalElement element : children) {
        if (!element.isPixelInside(event.x(), event.y())) {
          element.click(event);
          if (element.clickable()) {
            ((ClickEvent.Handler) element).handleClick(event);
          }
        } else {
          ClickEvent insideClickEvent = new ClickEvent(event.key(), true, event.pressed(), event.x(), event.y());
          element.click(insideClickEvent);
          if (element.clickable()) {
            ((ClickEvent.Handler) element).handleClick(insideClickEvent);
          }
        }
      }
    }
    return null;
  }

  public void addElement(FunctionalElement functionalElement) {
    this.addElement(functionalElement, Yoga.YGNodeGetChildCount(node.address()));
  }

  public void addElement(FunctionalElement functionalElement, int index) {
    children().add(functionalElement);
    Yoga.YGNodeInsertChild(node.address(), functionalElement.node().address(), index);
    functionalElement.setRoot(this);
  }

  public void removeElement(FunctionalElement functionalElement) {
    if (children.contains(functionalElement)) {
      Yoga.YGNodeRemoveChild(node.address(), functionalElement.node().address());
    }
  }

  public List<FunctionalElement> children() {
    return children;
  }

  public Map<String, EventHandler> eventHandler() {
    return eventHandlers;
  }

  public Window window() {
    return window;
  }

  public Font font() {
    return font;
  }

  public ColorSet colorSet() {
    return colorSet;
  }

  public Vector2f pixelSize() {
    return pixelSize;
  }
}
