package net.frooastside.engine.userinterface.elements.basic;

import net.frooastside.engine.resource.ResourceFont;
import net.frooastside.engine.userinterface.elements.UiFunctionalElement;
import net.frooastside.engine.userinterface.events.ClickEvent;
import net.frooastside.engine.userinterface.UiConstraints;
import net.frooastside.engine.userinterface.UiColorSet;
import net.frooastside.engine.userinterface.constraints.RawConstraint;
import net.frooastside.engine.userinterface.constraints.RelativeConstraint;
import net.frooastside.engine.userinterface.elements.render.UiBox;
import net.frooastside.engine.userinterface.elements.render.UiText;
import org.lwjgl.glfw.GLFW;

public class UiButton extends UiFunctionalElement implements ClickEvent.Listener {

  private final UiColorSet colorSet;
  private final ResourceFont font;
  private final float textSize;
  private final boolean constantTextSize;

  private String text;

  private boolean wasClicked;

  public UiButton(UiColorSet colorSet, ResourceFont font, String text, float textSize, boolean constantTextSize) {
    this.colorSet = colorSet;
    this.font = font;
    this.text = text;
    this.textSize = textSize;
    this.constantTextSize = constantTextSize;
  }

  @Override
  public void initialize() {
    UiBox background = new UiBox(colorSet.element());
    addElement(background);

    UiConstraints textConstraints = new UiConstraints(
      new RelativeConstraint(0),
      new RelativeConstraint(0.5f),
      new RelativeConstraint(1),
      constantTextSize ? new RawConstraint(textSize) : new RelativeConstraint(textSize));
    UiText text = new UiText(font, this.text, colorSet.text(), true);
    addElement(text, textConstraints);
  }

  @Override
  public boolean handleClick(ClickEvent event) {
    if (event.inside() && hasClickListener() && event.key() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
      if (event.pressed()) {
        wasClicked = true;
      } else {
        if (wasClicked) {
          if (hasClickListener()) {
            clickListener().handleClick(event);
          }
          wasClicked = false;
        }
      }
    } else {
      wasClicked = false;
    }
    return false;
  }

  public void setText(String text) {
    this.text = text;
    recalculateBounds();
  }
}
