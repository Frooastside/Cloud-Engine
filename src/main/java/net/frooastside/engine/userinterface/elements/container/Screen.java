package net.frooastside.engine.userinterface.elements.container;

import net.frooastside.engine.glfw.Window;
import net.frooastside.engine.glfw.callbacks.CharCallback;
import net.frooastside.engine.glfw.callbacks.KeyCallback;
import net.frooastside.engine.glfw.callbacks.MouseButtonCallback;
import net.frooastside.engine.resource.ResourceFont;
import net.frooastside.engine.userinterface.events.ClickEvent;
import net.frooastside.engine.userinterface.constraints.ElementConstraints;
import net.frooastside.engine.userinterface.events.SelectionEvent;
import net.frooastside.engine.userinterface.ColorSet;
import net.frooastside.engine.userinterface.elements.*;
import org.joml.Vector2f;

public abstract class Screen extends FunctionalElement implements ClickEvent.Listener, MouseButtonCallback, KeyCallback, CharCallback {

  private static final ElementConstraints DEFAULT_ELEMENT_CONSTRAINTS = ElementConstraints.getDefault();

  private final Window window;
  private final ResourceFont font;
  private final ColorSet colorSet;

  private FunctionalElement selectedElement;

  public Screen(Window window, ResourceFont font, ColorSet colorSet) {
    this.window = window;
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
  public void invokeKeyCallback(Window window, int key, int scancode, int modifiers, Action action) {
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

  @Override
  public ElementConstraints constraints() {
    return DEFAULT_ELEMENT_CONSTRAINTS;
  }

  public ResourceFont font() {
    return font;
  }

  public ColorSet colorSet() {
    return colorSet;
  }
}
