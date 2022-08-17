package love.polardivision.engine.userinterface.events;

public class SelectionEvent extends Event {

  private final boolean selected;

  public SelectionEvent(boolean selected) {
    this.selected = selected;
  }

  public boolean selected() {
    return selected;
  }

  public interface Handler extends EventHandler {

    @Override
    default void handle(Event event) {
      handleSelection((SelectionEvent) event);
    }

    void handleSelection(SelectionEvent event);

  }
}
