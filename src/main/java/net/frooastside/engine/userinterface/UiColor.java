package net.frooastside.engine.userinterface;

import org.joml.Vector3f;

public class UiColor {

  private Vector3f rawColor;

  public UiColor(Vector3f rawColor) {
    this.rawColor = rawColor;
  }

  public UiColor(float r, float g, float b) {
    this.rawColor = new Vector3f(r, g, b);
  }

  public Vector3f rawColor() {
    return rawColor;
  }
}
