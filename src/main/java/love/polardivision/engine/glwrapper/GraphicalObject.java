package love.polardivision.engine.glwrapper;

public abstract class GraphicalObject implements NativeObject {

  private int identifier;

  public abstract void bind();

  public abstract void unbind();

  public int identifier() {
    return identifier;
  }

  protected void setIdentifier(int identifier) {
    this.identifier = identifier;
  }
}
