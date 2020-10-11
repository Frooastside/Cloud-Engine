package net.frooastside.engine.userinterface;

import org.joml.Vector3f;

public enum UiColor {

  BACKGROUND(new Vector3f(0.2f, 0.2f, 0.2f)),
  WHITE(new Vector3f(1.0f, 1.0f, 1.0f)),
  ELEMENT(new Vector3f(0.24f, 0.24f, 0.24f)),
  ACCENT(new Vector3f(0.1215f, 0.5529f, 0.1215f));

  private final Vector3f color;

  UiColor(Vector3f color) {
    this.color = color;
  }

  public Vector3f rawColor() {
    return color;
  }
}
