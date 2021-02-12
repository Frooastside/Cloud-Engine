package net.frooastside.engine.userinterface;

import net.frooastside.engine.glfw.Window;
import net.frooastside.engine.glfw.callbacks.CharCallback;
import net.frooastside.engine.glfw.callbacks.KeyCallback;
import net.frooastside.engine.glfw.callbacks.MouseButtonCallback;
import net.frooastside.engine.resource.ResourceFont;
import net.frooastside.engine.userinterface.elements.*;
import org.joml.Vector2f;

import java.util.*;

public abstract class UiScreen extends UiContainerElement implements UiRootElement, ClickEvent.Listener, MouseButtonCallback, KeyCallback, CharCallback {

  private static final ElementConstraints DEFAULT_ELEMENT_CONSTRAINTS = ElementConstraints.getDefault();

  private final Window window;
  private final ResourceFont font;
  private final UiColorSet colorSet;

  private final Map<UiRenderElement.RenderType, List<UiRenderElement>> renderElements = new HashMap<>();

  private UiBasicElement selectedElement;

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

  @Override
  public void update() {

  }

  @Override
  public void invokeCharCallback(Window window, char codepoint) {
    if (window == this.window && selectedElement != null) {
      selectedElement.onCharEvent(codepoint);
    }
  }

  @Override
  public void invokeKeyCallback(Window window, int key, int scancode, int modifiers, Action action) {
    if (window == this.window && selectedElement != null) {
      selectedElement.onKeyEvent(key, scancode, modifiers, action);
    }
  }

  @Override
  public void invokeMouseButtonCallback(Window window, int key, boolean pressed) {
    if (window == this.window) {
      Vector2f mousePosition = window.input().mousePosition();
      onClick(new ClickEvent(key, true, pressed, mousePosition.x, mousePosition.y));
    }
  }

  public void emulateClick(int key, boolean pressed, float x, float y) {
    onClick(new ClickEvent(key, true, pressed, x, y));
  }

  @Override
  public boolean onClick(ClickEvent event) {
    UiElement selectedElement = click(event);
    if (this.selectedElement != selectedElement) {
      if (this.selectedElement != null && this.selectedElement.selectable()) {
        ((SelectionEvent.Listener) this.selectedElement).onSelection(new SelectionEvent(false));
      }
      if (selectedElement instanceof UiBasicElement) {
        if (((UiBasicElement) selectedElement).selectable()) {
          ((SelectionEvent.Listener) selectedElement).onSelection(new SelectionEvent(true));
        }
        this.selectedElement = (UiBasicElement) selectedElement;
      }
    }
    return selectedElement != null;
  }

  @Override
  public void addLeafElement(UiElement element) {
    if (element instanceof UiBasicElement) {
      UiBasicElement basicElement = (UiBasicElement) element;
      UiRenderElement[] renderElements = basicElement.renderElements();
      for (UiRenderElement renderElement : renderElements) {
        if (renderElement != null) {
          if (this.renderElements.containsKey(renderElement.renderType())) {
            this.renderElements.get(renderElement.renderType()).add(renderElement);
          } else {
            List<UiRenderElement> elements = new ArrayList<>();
            elements.add(renderElement);
            this.renderElements.put(renderElement.renderType(), elements);
          }
        }
      }
    }
  }

  @Override
  public UiRenderElement[] renderElements() {
    return new UiRenderElement[0];
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

  public List<UiRenderElement> renderElements(UiRenderElement.RenderType renderType) {
    return renderElements.get(renderType);
  }

  public Map<UiRenderElement.RenderType, List<UiRenderElement>> renderElementTypes() {
    return renderElements;
  }
}
