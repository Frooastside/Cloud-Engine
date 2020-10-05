package net.frooastside.engine.graphicobjects;

public abstract class SizedGraphicObject extends GraphicObject {

  private int width;
  private int height;

  public void setSize(int width, int height) {
    this.width = width;
    this.height = height;
  }

  public int width() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int height() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

}
