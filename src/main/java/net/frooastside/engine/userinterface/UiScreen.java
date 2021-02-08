package net.frooastside.engine.userinterface;

import net.frooastside.engine.glfw.Window;
import net.frooastside.engine.resource.ResourceFont;
import org.joml.Vector2f;

import java.util.*;

public abstract class UiScreen extends UiElement {

  private static final ElementConstraints DEFAULT_ELEMENT_CONSTRAINTS = ElementConstraints.getDefault();

  private final Window window;
  private final ResourceFont font;
  private final UiColorSet colorSet;
  private final Map<UiRenderElement.RenderType, List<UiRenderElement>> renderElements = new HashMap<>();

  private final Vector2f pixelSize = new Vector2f();

  private UiElement invisibleSelectedElement;
  private UiElement highlightedElement;

  public UiScreen(Window window, ResourceFont font, UiColorSet colorSet) {
    this.window = window;
    this.font = font;
    this.colorSet = colorSet;
  }

  public abstract void initialize();

  public void recalculate() {
    pixelSize.set(1f / window.resolution().x, 1f / window.resolution().y);
    children().forEach(child -> child.recalculate(pixelSize));
  }

  @Override
  public void update() {
    Vector2f mousePosition = window.input().mousePosition();
    UiElement invisibleSelectedElement = checkContact(mousePosition.x * pixelSize.x, mousePosition.y * pixelSize.y);
    if(invisibleSelectedElement != null) {
      this.invisibleSelectedElement = invisibleSelectedElement;
    }
  }

  @Override
  public void addElement(UiElement child, ElementConstraints constraints) {
    child.setConstraints(constraints);
    child.setRoot(this);
    constraints.setParent(DEFAULT_ELEMENT_CONSTRAINTS);
    pixelSize.set(1f / window.resolution().x, 1f / window.resolution().y);
    child.recalculate(pixelSize);
    addRenderElements(child);
    children().add(child);
  }

  @Override
  public void addRenderElements(UiElement element) {
    for (int i = 0; i < element.renderElements().length; i++) {
      UiRenderElement renderElement = element.renderElements()[i];
      if (renderElements.containsKey(renderElement.renderType())) {
        renderElements.get(renderElement.renderType()).add(renderElement);
      } else {
        List<UiRenderElement> elements = new ArrayList<>();
        elements.add(renderElement);
        renderElements.put(renderElement.renderType(), elements);
      }
    }
  }

  @Override
  public void invokeMouseButtonCallback(Window window, int key, boolean pressed) {
    if(highlightedElement != null) {
      highlightedElement.invokeMouseButtonCallback(window, key, pressed);
    }
    if(highlightedElement != invisibleSelectedElement) {
      if(invisibleSelectedElement != null) {
        invisibleSelectedElement.invokeMouseButtonCallback(window, key, pressed);
      }
    }
  }

  @Override
  public void invokeKeyCallback(Window window, int key, int scancode, Modifier modifier, Action buttonState) {
    if(highlightedElement != null) {
      highlightedElement.invokeKeyCallback(window, key, scancode, modifier, buttonState);
    }
    if(highlightedElement != invisibleSelectedElement) {
      if(invisibleSelectedElement != null) {
        invisibleSelectedElement.invokeKeyCallback(window, key, scancode, modifier, buttonState);
      }
    }
  }

  @Override
  public void invokeCharCallback(Window window, char codepoint) {
    if(highlightedElement != null) {
      highlightedElement.invokeCharCallback(window, codepoint);
    }
    if(highlightedElement != invisibleSelectedElement) {
      if(invisibleSelectedElement != null) {
        invisibleSelectedElement.invokeCharCallback(window, codepoint);
      }
    }
  }

  @Override
  public UiElement checkContact(float x, float y) {
    recalculate();
    if(!children().isEmpty()) {
      for(UiElement child : children()) {
        UiElement selectedItem = child.checkContact(x, y);
        if(selectedItem != null) {
          return selectedItem;
        }
      }
    }
    return null;
  }

  @Override
  public void onContact() {}

  @Override
  public void onLoseContact() {}

  @Override
  public UiRenderElement[] renderElements() {
    return new UiRenderElement[0];
  }

  @Override
  public UiElement highlightedElement() {
    return highlightedElement;
  }

  @Override
  public void setHighlighted(UiElement element) {
    this.highlightedElement = element;
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
