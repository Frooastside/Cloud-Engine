package love.polardivision.engine.userinterface.elements;

public abstract class RenderElement extends Element {

  public void recalculate() {
  }

  @Override
  public void update(double delta) {
  }

  public abstract RenderType renderType();

  public enum RenderType {

    BOX, TEXT

  }
}
