package net.frooastside.engine.userinterface.elements.basic;

import net.frooastside.engine.userinterface.animation.Animation;
import net.frooastside.engine.userinterface.elements.Element;
import net.frooastside.engine.userinterface.elements.FunctionalElement;
import net.frooastside.engine.userinterface.elements.render.Text;
import net.frooastside.engine.userinterface.events.ClickEvent;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class Button extends FunctionalElement implements ClickEvent.Handler {

  private Element background;
  private Text text;
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
      text.bounds().set(this.bounds().x, this.bounds().y, this.bounds().z, text.bounds().w);
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

  public Element background() {
    return background;
  }

  public void setBackground(Element background) {
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

  public void setText(Text text) {
    if (this.text != null) {
      children().remove(text);
    }
    if (text != null) {
      addElement(text);
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
