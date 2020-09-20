package net.frooastside.engine.datatypes;

public abstract class GLObject {

  protected int identifier;

  public abstract void generateIdentifier();

  public abstract void bind();

  public abstract void unbind();

  public abstract void delete();

  public int identifier() {
    return identifier;
  }

}
