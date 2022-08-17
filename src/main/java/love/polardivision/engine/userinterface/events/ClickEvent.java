package love.polardivision.engine.userinterface.events;

import love.polardivision.engine.window.MouseButton;

public class ClickEvent extends Event {

  private final MouseButton button;
  private final boolean inside;
  private final boolean pressed;
  private final float x, y;

  public ClickEvent(MouseButton button, boolean inside, boolean pressed, float x, float y) {
    this.button = button;
    this.inside = inside;
    this.pressed = pressed;
    this.x = x;
    this.y = y;
  }

  public MouseButton key() {
    return button;
  }

  public boolean inside() {
    return inside;
  }

  public boolean pressed() {
    return pressed;
  }

  public float x() {
    return x;
  }

  public float y() {
    return y;
  }

  public interface Handler extends EventHandler {

    @Override
    default void handle(Event event) {
      handleClick((ClickEvent) event);
    }

    boolean handleClick(ClickEvent event);

  }
}
