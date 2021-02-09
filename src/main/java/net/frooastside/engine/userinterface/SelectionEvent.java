package net.frooastside.engine.userinterface;

public class SelectionEvent {

  private final boolean selected;

  public SelectionEvent(boolean selected) {
    this.selected = selected;
  }

  public interface Listener {

    boolean onSelection(SelectionEvent event);

  }

  public boolean selected() {
    return selected;
  }
}
