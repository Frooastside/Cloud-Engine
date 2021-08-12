package net.frooastside.engine.userinterface;

import net.frooastside.engine.glfw.Window;
import net.frooastside.engine.glfw.callbacks.KeyCallback;
import net.frooastside.engine.graphicobjects.Font;
import net.frooastside.engine.userinterface.animation.Animation;
import net.frooastside.engine.userinterface.constraints.ElementConstraints;
import net.frooastside.engine.userinterface.elements.ContainerElement;
import net.frooastside.engine.userinterface.elements.Element;
import net.frooastside.engine.userinterface.elements.FunctionalElement;
import net.frooastside.engine.userinterface.events.ClickEvent;
import net.frooastside.engine.userinterface.events.Event;
import net.frooastside.engine.userinterface.events.EventHandler;
import net.frooastside.engine.userinterface.events.SelectionEvent;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Screen {

  private static final Vector4f DEFAULT_ELEMENT_AREA = new Vector4f(0, 0, 1, 1);
  private static final ElementConstraints DEFAULT_ELEMENT_CONSTRAINTS = ElementConstraints.getDefault();

  private final Map<String, Animation> animations = new HashMap<>();
  private final List<ContainerElement> children = new ArrayList<>();
  private final Map<String, EventHandler> eventHandlers = new HashMap<>();

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

  public void recalculateScreen() {
    updatePixelSize(pixelSize().set(1f / window.resolution().x, 1f / window.resolution().y));
    this.recalculateBounds();
  }

  public void updatePixelSize(Vector2f pixelSize) {
    for (ContainerElement element : children()) {
      if (element != null) {
        element.updatePixelSize(pixelSize);
      }
    }
  }

  public void recalculateBounds() {
    for (ContainerElement element : children()) {
      if (element != null) {
        element.calculateBounds(DEFAULT_ELEMENT_AREA);
        element.calculateChildBounds();
      }
    }
  }

  public void update() {
    for (ContainerElement element : children()) {
      if (element != null) {
        element.update(window.delta());
      }
    }
  }

  public void display(boolean show, float delay) {
    for (ContainerElement element : children) {
      if (element != null) {
        element.display(show, delay);
        System.out.println("show, " + element);
      }
    }
  }

  public void emitEvent(Event event, Class<? extends EventHandler> type, String targets) {
    if (targets != null) {
      for (String target : targets.split(",")) {
        EventHandler eventHandler = eventHandlers.get(target);
        if (type.isInstance(eventHandler)) {
          eventHandler.handle(event);
        }
      }
    }
  }

  public void handleMouseButton(Window window, int key, boolean pressed) {
    if (window == this.window) {
      Vector2f mousePosition = window.input().mousePosition();
      handleClick(new ClickEvent(key, true, pressed, mousePosition.x, mousePosition.y));
    }
  }

  public void handleKey(Window window, int key, int scancode, int modifiers, KeyCallback.Action action) {
    if (window == this.window) {
      for (ContainerElement element : children()) {
        if (element != null) {
          element.handleKey(window, key, scancode, modifiers, action);
        }
      }
    }
  }

  public void handleChar(Window window, char codepoint) {
    if (window == this.window) {
      for (ContainerElement element : children()) {
        if (element != null) {
          element.handleChar(window, codepoint);
        }
      }
    }
  }

  public void emulateClick(int key, boolean pressed, float x, float y) {
    handleClick(new ClickEvent(key, true, pressed, x, y));
  }

  public boolean handleClick(ClickEvent event) {
    Element selectedElement = clickChildren(event);
    if (this.selectedElement != selectedElement) {
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
      for (ContainerElement element : children) {
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
      for (ContainerElement element : children) {
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

  public void addElement(ContainerElement containerElement) {
    children().add(containerElement);
    containerElement.setRoot(this);
  }

  public Map<String, Animation> animations() {
    return animations;
  }

  public List<ContainerElement> children() {
    return children;
  }

  public Map<String, EventHandler> eventHandler() {
    return eventHandlers;
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
