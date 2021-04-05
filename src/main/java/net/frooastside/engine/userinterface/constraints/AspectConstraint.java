package net.frooastside.engine.userinterface.constraints;

public class AspectConstraint extends Constraint {

  private float aspectRatio;

  public AspectConstraint(float aspectRatio) {
    this.aspectRatio = aspectRatio;
  }

  @Override
  public float rawValue() {
    float screenAspectRatio = ((type() == ConstraintType.X || type() == ConstraintType.WIDTH) ? current().pixelSize().x / current().pixelSize().y : current().pixelSize().y / current().pixelSize().x);
    return getCounterpart().rawValue() * aspectRatio * screenAspectRatio;
  }

  @Override
  public void setValue(float value) {
    aspectRatio = value;
  }

  @Override
  public boolean relative() {
    return false;
  }
}
