package net.frooastside.engine.userinterface.events;

import net.frooastside.engine.glfw.Window;
import net.frooastside.engine.glfw.callbacks.KeyCallback;

public class SelectionEvent {

  private final boolean selected;

  public SelectionEvent(boolean selected) {
    this.selected = selected;
  }

  public interface Listener {

    void handleSelection(SelectionEvent event);

    public void handleKeyEvent(Window window, int key, int scancode, int modifiers, KeyCallback.Action action);

    public void handleCharEvent(Window window, int codepoint);

  }

  public boolean selected() {
    return selected;
  }
}
