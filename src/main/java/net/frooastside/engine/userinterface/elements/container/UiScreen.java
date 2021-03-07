package net.frooastside.engine.userinterface.elements.container;

import net.frooastside.engine.glfw.Window;
import net.frooastside.engine.glfw.callbacks.CharCallback;
import net.frooastside.engine.glfw.callbacks.KeyCallback;
import net.frooastside.engine.glfw.callbacks.MouseButtonCallback;
import net.frooastside.engine.resource.ResourceFont;
import net.frooastside.engine.userinterface.events.ClickEvent;
import net.frooastside.engine.userinterface.UiConstraints;
import net.frooastside.engine.userinterface.events.SelectionEvent;
import net.frooastside.engine.userinterface.UiColorSet;
import net.frooastside.engine.userinterface.elements.*;
import org.joml.Vector2f;

public abstract class UiScreen extends UiFunctionalElement implements ClickEvent.Listener, MouseButtonCallback, KeyCallback, CharCallback {

  private static final UiConstraints DEFAULT_ELEMENT_CONSTRAINTS = UiConstraints.getDefault();

  private final Window window;
  private final ResourceFont font;
  private final UiColorSet colorSet;

  private UiFunctionalElement selectedElement;

  public UiScreen(Window window, ResourceFont font, UiColorSet colorSet) {
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
    for (UiElement element : children()) {
      if (element != null) {
        element.recalculateBounds();
      }
    }
  }

  @Override
  public void updatePixelSize(Vector2f pixelSize) {
    for (UiElement element : children()) {
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
    UiElement selectedElement = click(event);
    if (this.selectedElement != selectedElement) {
      if (this.selectedElement != null && this.selectedElement.selectable()) {
        ((SelectionEvent.Listener) this.selectedElement).handleSelection(new SelectionEvent(false));
      }
      if (selectedElement instanceof UiFunctionalElement) {
        if (((UiFunctionalElement) selectedElement).selectable()) {
          ((SelectionEvent.Listener) selectedElement).handleSelection(new SelectionEvent(true));
        }
        this.selectedElement = (UiFunctionalElement) selectedElement;
      } else {
        this.selectedElement = null;
      }
    }
    return selectedElement != null;
  }

  @Override
  public UiConstraints constraints() {
    return DEFAULT_ELEMENT_CONSTRAINTS;
  }

  public ResourceFont font() {
    return font;
  }

  public UiColorSet colorSet() {
    return colorSet;
  }
}
