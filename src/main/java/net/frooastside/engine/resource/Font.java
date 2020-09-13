package net.frooastside.engine.resource;

import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class Font extends ResourceItem {

  public static final Character UNSUPPORTED_CHARACTER = new Character(0, 0, 0, 0, 0, 0, 0, 0, 0);
  public static final int SPACE_ASCII = 32;

  private final Map<Integer, Character> supportedCharacters = new HashMap<>();
  private int characterHeight;
  private Texture texture;

  private int imageSize;
  private int firstCharacter;
  private int characterCount;

  public Font(Texture texture, int characterHeight) {
    this.characterHeight = characterHeight;
    this.texture = texture;
  }

  public Font(ByteBuffer fontFile) {
    this(fontFile, 4096, 32, 224, 512);
  }

  public Font(ByteBuffer fontFile, int imageSize, int firstCharacter, int characterCount, int characterHeight) {
    this.rawFile = fontFile;
    this.imageSize = imageSize;
    this.firstCharacter = firstCharacter;
    this.characterCount = characterCount;
    this.characterHeight = characterHeight;
  }

  public Font() {
  }

  private void initFont(ByteBuffer fontFile) {
    STBTTFontinfo fontInfo = new STBTTFontinfo(MemoryUtil.memAlloc(160));
    STBTruetype.stbtt_InitFont(fontInfo, fontFile);
    fontInfo.free();
  }

  private void addCharacters(STBTTBakedChar.Buffer characterBuffer, int characterCount, int firstCharacter, int imageSize) {
    for (int i = 0; i < characterCount; i++) {
      STBTTBakedChar character = characterBuffer.get(i);
      int asciiCharacter = i + firstCharacter;
      addCharacter(character, asciiCharacter, imageSize);
    }
  }

  private void addCharacter(STBTTBakedChar character, int asciiCharacter, int imageSize) {
    double x = character.x0();
    double y = character.y0();
    double width = character.x1() - x;
    double height = character.y1() - y;
    double xTextureCoordinate = x / imageSize;
    double yTextureCoordinate = y / imageSize;
    supportedCharacters.put(asciiCharacter, new Character(
      xTextureCoordinate,
      yTextureCoordinate,
      xTextureCoordinate + width / imageSize,
      yTextureCoordinate + height / imageSize,
      character.xoff(),
      character.yoff(),
      width,
      height,
      character.xadvance()));
  }

  @Override
  public Runnable getThreadSpecificLoader() {
    return () -> texture.getThreadSpecificLoader().run();
  }

  @Override
  public Runnable getThreadUnspecificLoader() {
    return () -> {
      if(rawFile != null && rawFile.hasRemaining()) {
        initFont(rawFile);
        ByteBuffer pixelBuffer = MemoryUtil.memAlloc(imageSize * imageSize);
        STBTTBakedChar.Buffer characterBuffer = STBTTBakedChar.malloc(characterCount);
        STBTruetype.stbtt_BakeFontBitmap(rawFile, characterHeight, pixelBuffer, imageSize, imageSize, firstCharacter, characterBuffer);
        texture = new Texture(pixelBuffer, Texture.BILINEAR_FILTER, imageSize, imageSize, 1);
        addCharacters(characterBuffer, characterCount, firstCharacter, imageSize);
        characterBuffer.free();
      }
    };
  }

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
    texture = (Texture) in.readObject();
  }

  public Character getCharacter(int asciiCharacter) {
    return supportedCharacters.getOrDefault(asciiCharacter, UNSUPPORTED_CHARACTER);
  }

  public int characterHeight() {
    return characterHeight;
  }

  public Texture texture() {
    return texture;
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
