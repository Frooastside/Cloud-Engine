package net.frooastside.engine.userinterface.elements.container;

import net.frooastside.engine.glfw.Window;
import net.frooastside.engine.glfw.callbacks.CharCallback;
import net.frooastside.engine.glfw.callbacks.KeyCallback;
import net.frooastside.engine.glfw.callbacks.MouseButtonCallback;
import net.frooastside.engine.resource.ResourceFont;
import net.frooastside.engine.userinterface.events.ClickEvent;
import net.frooastside.engine.userinterface.ElementConstraints;
import net.frooastside.engine.userinterface.events.SelectionEvent;
import net.frooastside.engine.userinterface.UiColorSet;
import net.frooastside.engine.userinterface.elements.*;
import org.joml.Vector2f;

public abstract class UiScreen extends UiFunctionalElement implements ClickEvent.Listener, MouseButtonCallback, KeyCallback, CharCallback {

  private static final ElementConstraints DEFAULT_ELEMENT_CONSTRAINTS = ElementConstraints.getDefault();

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

  public void recalculate() {
    recalculate(pixelSize().set(1f / window.resolution().x, 1f / window.resolution().y));
  }

  @Override
  public void recalculate(Vector2f pixelSize) {
    for (UiElement element : children()) {
      element.recalculate(pixelSize);
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
    System.out.println("click");
    UiElement selectedElement = click(event);
    System.out.println(selectedElement);
    if (this.selectedElement != selectedElement) {
      System.out.println("old slctd element not equal to clicked element");
      if (this.selectedElement != null && this.selectedElement.selectable()) {
        System.out.println("deselect old slctd element");
        ((SelectionEvent.Listener) this.selectedElement).handleSelection(new SelectionEvent(false));
      }
      if (selectedElement instanceof UiFunctionalElement) {
        System.out.println("look if functional element did get clicked");
        if (((UiFunctionalElement) selectedElement).selectable()) {
          System.out.println("find slctn listener on new element");
          ((SelectionEvent.Listener) selectedElement).handleSelection(new SelectionEvent(true));
        }
        this.selectedElement = (UiFunctionalElement) selectedElement;
      }else {
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

  public UiColorSet colorSet() {
    return colorSet;
  }
}
