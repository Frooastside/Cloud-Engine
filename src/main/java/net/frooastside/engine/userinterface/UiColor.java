package net.frooastside.engine.userinterface;

import org.joml.Vector3f;

public enum UiColor {

  GREEN(new Vector3f(0.2f, 0.4f, 0.1f));

  private final Vector3f color;

  UiColor(Vector3f color) {
    this.color = color;
  }

  public Vector3f rawColor() {
    return color;
  }
}
