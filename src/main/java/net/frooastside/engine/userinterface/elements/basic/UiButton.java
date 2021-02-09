package net.frooastside.engine.userinterface.elements.basic;

import net.frooastside.engine.resource.ResourceFont;
import net.frooastside.engine.userinterface.ClickEvent;
import net.frooastside.engine.userinterface.ElementConstraints;
import net.frooastside.engine.userinterface.UiColorSet;
import net.frooastside.engine.userinterface.constraints.RawConstraint;
import net.frooastside.engine.userinterface.constraints.RelativeConstraint;
import net.frooastside.engine.userinterface.elements.UiBasicElement;
import net.frooastside.engine.userinterface.elements.UiRenderElement;
import net.frooastside.engine.userinterface.elements.render.UiText;
import org.joml.Vector2f;

public class UiButton extends UiBasicElement implements ClickEvent.Listener {

  private final UiRenderElement[] renderElements = new UiRenderElement[2];

  private final UiColorSet colorSet;
  private final ResourceFont font;
  private final float textSize;
  private final boolean constantTextSize;

  private String text;

  public UiButton(UiColorSet colorSet, ResourceFont font, String text, float textSize, boolean constantTextSize) {
    this.font = font;
    this.text = text;
    this.textSize = textSize;
    this.colorSet = colorSet;
    this.constantTextSize = constantTextSize;
  }

  @Override
  public void recalculate(Vector2f pixelSize) {
    super.recalculate(pixelSize);
    for(UiRenderElement renderElement : renderElements) {
      if (renderElement != null) {
        renderElement.recalculate(pixelSize);
      }
    }
  }

  public void initialize() {
    ElementConstraints backgroundConstraints = new ElementConstraints();

    ElementConstraints elementConstraints = new ElementConstraints();
    elementConstraints.setParent(constraints());
    elementConstraints.setX(new RelativeConstraint(0));
    elementConstraints.setY(new RelativeConstraint(0.5f));
    elementConstraints.setWidth(new RelativeConstraint(1));
    elementConstraints.setHeight(constantTextSize ? new RawConstraint(textSize) : new RelativeConstraint(textSize));
    renderElements[1] = new UiText(font, text, colorSet.text(), true);
  }

  @Override
  public void onClick(ClickEvent event) {
    if(event.inside()) {
      if(hasClickListener()) {
        clickListener().onClick(event);
      }
    }
  }

  @Override
  public UiRenderElement[] renderElements() {
    return renderElements;
  }

  public void setText(String text) {
    this.text = text;
    recalculate(pixelSize());
  }
}
