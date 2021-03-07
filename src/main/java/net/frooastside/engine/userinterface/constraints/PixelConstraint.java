package net.frooastside.engine.userinterface.constraints;

import net.frooastside.engine.userinterface.UiConstraint;

public class PixelConstraint extends UiConstraint {

  private int pixels;

  public PixelConstraint(int pixels) {
    this.pixels = pixels;
  }

  @Override
  public float rawValue() {
    if (type() == ConstraintType.X || type() == ConstraintType.WIDTH) {
      return pixels * constraints().pixelSize().x;
    } else {
      return pixels * constraints().pixelSize().y;
    }
  }

  @Override
  public void setValue(float value) {

  }

  @Override
  public boolean relative() {
    return false;
  }

  public int pixels() {
    return pixels;
  }

  public void setPixels(int pixels) {
    this.pixels = pixels;
  }
}
