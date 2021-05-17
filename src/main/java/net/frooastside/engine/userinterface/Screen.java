package net.frooastside.engine.userinterface;

import net.frooastside.engine.glfw.Window;
import net.frooastside.engine.glfw.callbacks.KeyCallback;
import net.frooastside.engine.graphicobjects.Font;
import net.frooastside.engine.userinterface.animation.Animation;
import net.frooastside.engine.userinterface.constraints.ElementConstraints;
import net.frooastside.engine.userinterface.elements.Element;
import net.frooastside.engine.userinterface.elements.FunctionalElement;
import net.frooastside.engine.userinterface.events.ClickEvent;
import net.frooastside.engine.userinterface.events.SelectionEvent;
import org.joml.Vector2f;

import java.util.HashMap;
import java.util.Map;

public class Screen {

  private final Map<String, Animation> animations = new HashMap<>();

  public Map<String, Animation> animations() {
    return animations;
  }

  private static final ElementConstraints DEFAULT_ELEMENT_CONSTRAINTS = ElementConstraints.getDefault();

  private final Window window;
  private final Font font;
  private final ColorSet colorSet;

  private FunctionalElement selectedElement;

  public Screen(Window window, Font font, ColorSet colorSet) {
    this.j = window;
    this.font = font;
    this.colorSet = colorSet;
    setRoot(this);
  }

  public abstract void initialize();

  public void recalculateScreen() {
    updatePixelSize(pixelSize().set(1f / window.resolution().x, 1f / window.resolution().y));
    this.recalculateBounds();
  }

  @Override
  public void recalculateBounds() {
    for (Element element : children()) {
      if (element != null) {
        element.recalculateBounds();
      }
    }
  }

  @Override
  public void updatePixelSize(Vector2f pixelSize) {
    for (Element element : children()) {
      if (element != null) {
        element.updatePixelSize(pixelSize);
      }
    }
  }

  public void update() {
    update(window.delta());
  }

  @Override
  public void invokeCharCallback(Window window, char codepoint) {
    if (window == this.window && selectedElement != null && selectedElement instanceof SelectionEvent.Listener) {
      ((SelectionEvent.Listener) selectedElement).handleCharEvent(window, codepoint);
    }
  }

  @Override
  public void invokeKeyCallback(Window window, int key, int scancode, int modifiers, KeyCallback.Action action) {
    if (window == this.window && selectedElement != null && selectedElement instanceof SelectionEvent.Listener) {
      ((SelectionEvent.Listener) selectedElement).handleKeyEvent(window, key, scancode, modifiers, action);
    }
  }

  @Override
  public void invokeMouseButtonCallback(Window window, int key, boolean pressed) {
    if (window == this.window) {
      Vector2f mousePosition = window.input().mousePosition();
      handleClick(new ClickEvent(key, true, pressed, mousePosition.x, mousePosition.y));
    }
  }

  public void emulateClick(int key, boolean pressed, float x, float y) {
    handleClick(new ClickEvent(key, true, pressed, x, y));
  }

  @Override
  public boolean handleClick(ClickEvent event) {
    Element selectedElement = click(event);
    if (this.selectedElement != selectedElement) {
      if (this.selectedElement != null && this.selectedElement.selectable()) {
        ((SelectionEvent.Listener) this.selectedElement).handleSelection(new SelectionEvent(false));
      }
      if (selectedElement instanceof FunctionalElement) {
        if (((FunctionalElement) selectedElement).selectable()) {
          ((SelectionEvent.Listener) selectedElement).handleSelection(new SelectionEvent(true));
        }
        this.selectedElement = (FunctionalElement) selectedElement;
      } else {
        this.selectedElement = null;
      }
    }
    return selectedElement != null;
  }

  public Font font() {
    return font;
  }

  public ColorSet colorSet() {
    return colorSet;
  }
}
