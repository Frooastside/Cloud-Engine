package net.frooastside.engine.graphicobjects;

import net.frooastside.engine.resource.ResourceTexture;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public abstract class Font implements Externalizable {

  private static final long serialVersionUID = -1857587127382434365L;

  public static final Character DEFAULT_CHARACTER = new Character(0, 0, 0, 0, 0, 0, 0, 0, 0);
  public static final int SPACE_CODEPOINT = 32;

  private final Map<Integer, Character> supportedCharacters = new HashMap<>();
  private int characterHeight;
  private ResourceTexture texture;

  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeObject(supportedCharacters);
    out.writeShort(characterHeight);
    out.writeObject(texture);
  }

  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    for (Map.Entry<?, ?> entry : ((Map<?, ?>) in.readObject()).entrySet()) {
      if (entry.getKey() instanceof Integer && entry.getValue() instanceof Character) {
        supportedCharacters.put((Integer) entry.getKey(), (Character) entry.getValue());
      }
    }
    characterHeight = in.readShort();
    texture = (ResourceTexture) in.readObject();
  }

  public Character getCharacter(int codepoint) {
    return supportedCharacters.getOrDefault(codepoint, DEFAULT_CHARACTER);
  }

  public Map<Integer, Character> supportedCharacters() {
    return supportedCharacters;
  }

  public int characterHeight() {
    return characterHeight;
  }

  public void setCharacterHeight(int characterHeight) {
    this.characterHeight = characterHeight;
  }

  public ResourceTexture texture() {
    return texture;
  }

  public void setTexture(ResourceTexture texture) {
    this.texture = texture;
  }

  public static class Character implements Serializable {

    private final double xTextureCoordinate;
    private final double yTextureCoordinate;
    private final double xMaxTextureCoordinate;
    private final double yMaxTextureCoordinate;
    private final double xOffset;
    private final double yOffset;
    private final double xSize;
    private final double ySize;
    private final double xAdvance;

    public Character(double xTextureCoordinate, double yTextureCoordinate, double xMaxTextureCoordinate, double yMaxTextureCoordinate, double xOffset, double yOffset, double xSize, double ySize, double xAdvance) {
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
}
