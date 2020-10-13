package net.frooastside.engine.userinterface.constraints;

import net.frooastside.engine.userinterface.Constraint;

public class PixelConstraint extends Constraint {

  private int pixels;

  public PixelConstraint(int pixels) {
    this.pixels = pixels;
  }

  @Override
  public void recalculate() {
    if(type() == ConstraintType.X || type() == ConstraintType.WIDTH) {
      setRawValue(pixels * constraints().pixelSize().x);
    }else if(type() == ConstraintType.Y || type() == ConstraintType.HEIGHT) {
      setRawValue(pixels * constraints().pixelSize().y);
    }
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
