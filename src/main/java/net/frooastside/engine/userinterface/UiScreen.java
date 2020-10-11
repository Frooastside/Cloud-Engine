package net.frooastside.engine.userinterface;

import net.frooastside.engine.Window;
import net.frooastside.engine.resource.ResourceFont;
import org.joml.Vector2f;

import java.util.*;

public abstract class UiScreen extends UiElement {

  private static final ElementConstraints DEFAULT_ELEMENT_CONSTRAINTS = ElementConstraints.getDefault();

  private final Window window;
  private final ResourceFont font;
  private final Map<UiRenderElement.RenderType, List<UiRenderElement>> renderElements = new HashMap<>();

  private final Vector2f pixelSize = new Vector2f();

  public UiScreen(Window window, ResourceFont font) {
    this.window = window;
    this.font = font;
  }

  public abstract void initialize();

  public void recalculate() {
    pixelSize.set(1f / window.resolution().x, 1f / window.resolution().y);
    children().forEach(child -> child.recalculate(pixelSize));
  }

  @Override
  public void addElement(UiElement child, ElementConstraints constraints) {
    child.setConstraints(constraints);
    constraints.setParent(DEFAULT_ELEMENT_CONSTRAINTS);
    pixelSize.set(1f / window.resolution().x, 1f / window.resolution().y);
    child.recalculate(pixelSize);
    appendRenderElements(child);
    children().add(child);
  }

  @Override
  public void appendRenderElements(UiElement element) {
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
  public UiRenderElement[] renderElements() {
    return new UiRenderElement[0];
  }

  public ResourceFont font() {
    return font;
  }

  public List<UiRenderElement> renderElements(UiRenderElement.RenderType renderType) {
    return renderElements.get(renderType);
  }

  public Map<UiRenderElement.RenderType, List<UiRenderElement>> allRenderElementTypes() {
    return renderElements;
  }
}
