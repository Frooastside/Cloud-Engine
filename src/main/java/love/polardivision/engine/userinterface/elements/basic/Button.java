package love.polardivision.engine.userinterface.elements.basic;

import love.polardivision.engine.userinterface.elements.FunctionalElement;
import love.polardivision.engine.userinterface.events.ClickEvent;
import love.polardivision.engine.window.MouseButton;

public class Button extends FunctionalElement implements ClickEvent.Handler {

  private boolean wasClicked;

  @Override
  public void update(double delta) {
    super.update(delta);
  }

  @Override
  public boolean handleClick(ClickEvent event) {
    if (event.inside() && event.key() == MouseButton.MOUSE_BUTTON_LEFT) {
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
}
