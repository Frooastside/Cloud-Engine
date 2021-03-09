package net.frooastside.engine.userinterface.elements.basic;

import net.frooastside.engine.userinterface.elements.FunctionalElement;
import net.frooastside.engine.userinterface.events.ClickEvent;
import org.lwjgl.glfw.GLFW;

public abstract class Button extends FunctionalElement implements ClickEvent.Listener {

  private boolean wasClicked;

  @Override
  public boolean handleClick(ClickEvent event) {
    if (event.inside() && hasClickListener() && event.key() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
      if (event.pressed()) {
        wasClicked = true;
      } else {
        if (wasClicked) {
          if (hasClickListener()) {
            handleInternalClick(event);
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

  public abstract void handleInternalClick(ClickEvent event);
}
