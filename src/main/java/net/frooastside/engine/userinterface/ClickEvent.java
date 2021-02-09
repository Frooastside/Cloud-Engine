package net.frooastside.engine.userinterface;

public class ClickEvent {

  private final int key;
  private final boolean inside;
  private final boolean pressed;
  private final float x, y;

  public ClickEvent(int key, boolean inside, boolean pressed, float x, float y) {
    this.key = key;
    this.inside = inside;
    this.pressed = pressed;
    this.x = x;
    this.y = y;
  }

  public interface Listener {

    void onClick(ClickEvent event);

  }

  public int key() {
    return key;
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
}
