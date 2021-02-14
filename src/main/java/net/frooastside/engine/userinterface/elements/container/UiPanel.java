package net.frooastside.engine.userinterface.elements.container;

import net.frooastside.engine.userinterface.ElementConstraints;
import net.frooastside.engine.userinterface.UiColorSet;
import net.frooastside.engine.userinterface.elements.UiContainerElement;
import net.frooastside.engine.userinterface.elements.UiRenderElement;
import net.frooastside.engine.userinterface.elements.render.UiBox;
import org.joml.Vector2f;

public class UiPanel extends UiContainerElement {

  private final UiColorSet colorSet;

  private final UiRenderElement[] renderElements = new UiRenderElement[1];

  public UiPanel(UiColorSet colorSet) {
    this.colorSet = colorSet;
  }

  @Override
  public void initialize() {
    UiBox background = new UiBox(colorSet.background());
    ElementConstraints backgroundConstraints = ElementConstraints.getDefault();
    backgroundConstraints.setParent(constraints());
    background.setConstraints(backgroundConstraints);
    renderElements[0] = background;
  }

  @Override
  public UiRenderElement[] renderElements() {
    return renderElements;
  }
}
