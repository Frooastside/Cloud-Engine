package love.polardivision.engine.userinterface.events;

public class ScrollEvent extends Event {

  private final boolean inside;
  private final float x, y;
  private final float scrollX, scrollY;

  public ScrollEvent(boolean inside, float x, float y, float scrollX, float scrollY) {
    this.inside = inside;
    this.x = x;
    this.y = y;
    this.scrollX = scrollX;
    this.scrollY = scrollY;
  }

  public boolean inside() {
    return inside;
  }

  public float x() {
    return x;
  }

  public float y() {
    return y;
  }

  public float scrollX() {
    return scrollX;
  }

  public float scrollY() {
    return scrollY;
  }

  public interface Handler extends EventHandler {

    @Override
    default void handle(Event event) {
      handleScroll((ScrollEvent) event);
    }

    boolean handleScroll(ScrollEvent event);

  }
}
