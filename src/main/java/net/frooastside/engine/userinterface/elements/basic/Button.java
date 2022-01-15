package net.frooastside.engine.userinterface.elements.basic;

import net.frooastside.engine.userinterface.animation.Animation;
import net.frooastside.engine.userinterface.constraints.Constraint;
import net.frooastside.engine.userinterface.constraints.ElementConstraints;
import net.frooastside.engine.userinterface.constraints.RelativeConstraint;
import net.frooastside.engine.userinterface.elements.FunctionalElement;
import net.frooastside.engine.userinterface.elements.RenderElement;
import net.frooastside.engine.userinterface.elements.render.Text;
import net.frooastside.engine.userinterface.events.ClickEvent;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class Button extends FunctionalElement implements ClickEvent.Handler {

  private RenderElement background;
  private Text text;
  private final ElementConstraints textConstraints = new ElementConstraints(
    new RelativeConstraint(0f),
    new RelativeConstraint(0f),
    new RelativeConstraint(1f),
    new RelativeConstraint(1f));
  private Animation hoverAnimation;

  private boolean wasClicked;
  private boolean wasInside;

  @Override
  public void update(double delta) {
    super.update(delta);
    if (hoverAnimation != null) {
      Vector2f mousePosition = this.root().window().input().mousePosition();
      if (wasInside != isPixelInside(mousePosition.x, mousePosition.y)) {
        wasInside = !wasInside;
        animator().applyAnimation(hoverAnimation, !wasInside, 0);
      }
    }
  }

  @Override
  public void calculateChildBounds() {
    if (background != null) {
      background.bounds().set(this.bounds());
    }
    if (text != null) {
      textConstraints.setPixelSize(pixelSize());
      text.bounds().set(
        textConstraints.x().calculate(bounds()) + bounds().x,
        textConstraints.y().calculate(bounds()) + bounds().y + (bounds().w / 2 - textConstraints.w().calculate(bounds()) / 4),
        textConstraints.z().calculate(bounds()),
        textConstraints.w().calculate(bounds()));
      text.recalculate();
    }
  }

  @Override
  public boolean handleClick(ClickEvent event) {
    if (event.inside() && event.key() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
      if (event.pressed()) {
        wasClicked = true;
      } else {
        if (wasClicked) {
          wasClicked = false;
          handleInternalClick(event);
        }
      }
    } else {
      wasClicked = false;
    }
    return false;
  }

  public void handleInternalClick(ClickEvent event) {
    emitEvent(
      new ClickEvent(event.key(), event.inside(), event.pressed(), event.x(), event.y()).caller(this),
      ClickEvent.Handler.class,
      clickEventTarget());
  }

  public RenderElement background() {
    return background;
  }

  public void setBackground(RenderElement background) {
    if (this.background != null) {
      children().remove(background);
    }
    if (background != null) {
      addElement(background);
    }
    this.background = background;
  }

  public Text text() {
    return text;
  }

  public void setText(Text text, Constraint fontSize) {
    if (this.text != null) {
      children().remove(this.text);
    }
    if (text != null) {
      addElement(text);
    }
    if (fontSize != null) {
      fontSize.setConstraints(textConstraints);
      textConstraints.setW(fontSize);
    }
    this.text = text;
  }

  public Animation hoverAnimation() {
    return hoverAnimation;
  }

  public void setHoverAnimation(Animation hoverAnimation) {
    this.hoverAnimation = hoverAnimation;
  }
}
