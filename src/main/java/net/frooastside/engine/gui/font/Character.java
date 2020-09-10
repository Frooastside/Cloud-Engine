package net.frooastside.engine.gui.font;

public class Character {

  private final int id;
  private final double xTextureCoordinate;
  private final double yTextureCoordinate;
  private final double xMaxTextureCoordinate;
  private final double yMaxTextureCoordinate;
  private final double xOffset;
  private final double yOffset;
  private final double xSize;
  private final double ySize;
  private final double xAdvance;

  public Character(int id, double xTextureCoordinate, double yTextureCoordinate, double xMaxTextureCoordinate, double yMaxTextureCoordinate, double xOffset, double yOffset, double xSize, double ySize, double xAdvance) {
    this.id = id;
    this.xTextureCoordinate = xTextureCoordinate;
    this.yTextureCoordinate = yTextureCoordinate;
    this.xMaxTextureCoordinate = xMaxTextureCoordinate;
    this.yMaxTextureCoordinate = yMaxTextureCoordinate;
    this.xOffset = xOffset;
    this.yOffset = yOffset;
    this.xSize = xSize;
    this.ySize = ySize;
    this.xAdvance = xAdvance;
  }

  public int id() {
    return id;
  }

  public double xTextureCoordinate() {
    return xTextureCoordinate;
  }

  public double yTextureCoordinate() {
    return yTextureCoordinate;
  }

  public double xMaxTextureCoordinate() {
    return xMaxTextureCoordinate;
  }

  public double yMaxTextureCoordinate() {
    return yMaxTextureCoordinate;
  }

  public double xOffset() {
    return xOffset;
  }

  public double yOffset() {
    return yOffset;
  }

  public double xSize() {
    return xSize;
  }

  public double ySize() {
    return ySize;
  }

  public double xAdvance() {
    return xAdvance;
  }
}
