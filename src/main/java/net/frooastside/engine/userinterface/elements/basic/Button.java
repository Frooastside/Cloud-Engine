package net.frooastside.engine.userinterface.elements.basic;

import net.frooastside.engine.userinterface.elements.FunctionalElement;
import net.frooastside.engine.userinterface.events.ClickEvent;
import org.lwjgl.glfw.GLFW;

public abstract class Button extends FunctionalElement implements ClickEvent.Handler {

  private boolean wasClicked;

  @Override
  public boolean handleClick(ClickEvent event) {
    if (event.inside() && event.key() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
      if (event.pressed()) {
        wasClicked = true;
      } else {
        if (wasClicked) {
          wasClicked = false;
          handleInternalClick(event);
          emitEvent(
            new ClickEvent(event.key(), event.inside(), event.pressed(), event.x(), event.y()).caller(this),
            ClickEvent.Handler.class,
            clickEventTarget());
        }
      }
    } else {
      wasClicked = false;
    }
    return false;
  }

  public abstract void handleInternalClick(ClickEvent event);
}
