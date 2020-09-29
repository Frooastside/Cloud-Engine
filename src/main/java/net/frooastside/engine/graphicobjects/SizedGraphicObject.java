package net.frooastside.engine.graphicobjects;

public abstract class SizedGraphicObject extends GraphicObject {

  protected int width;
  protected int height;

  public void setSize(int width, int height) {
    this.width = width;
    this.height = height;
  }

  public int width() {
    return width;
  }

  public int height() {
    return height;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public void setHeight(int height) {
    this.height = height;
  }

}
