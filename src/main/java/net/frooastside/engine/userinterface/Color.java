package net.frooastside.engine.userinterface;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class Color {

  private final Vector4f rawColor;

  public Color(Vector4f rawColor) {
    this.rawColor = rawColor;
  }

  public Color(float r, float g, float b, float a) {
    this.rawColor = new Vector4f(r, g, b, a);
  }

  public Color(Vector3f rawColor) {
    this.rawColor = new Vector4f(rawColor, 1.0f);
  }

  public Color(float r, float g, float b) {
    this.rawColor = new Vector4f(r, g, b, 1.0f);
  }

  public Vector4f rawColor() {
    return rawColor;
  }
}
