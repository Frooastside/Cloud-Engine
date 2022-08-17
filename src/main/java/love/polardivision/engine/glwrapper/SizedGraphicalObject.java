package love.polardivision.engine.glwrapper;

public abstract class SizedGraphicalObject extends GraphicalObject {

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
