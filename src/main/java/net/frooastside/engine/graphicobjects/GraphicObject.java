package net.frooastside.engine.graphicobjects;

public abstract class GraphicObject {

  private int identifier;

  public abstract void generateIdentifier();

  public abstract void bind();

  public abstract void unbind();

  public abstract void delete();

  public int identifier() {
    return identifier;
  }

  protected void setIdentifier(int identifier) {
    this.identifier = identifier;
  }

}
