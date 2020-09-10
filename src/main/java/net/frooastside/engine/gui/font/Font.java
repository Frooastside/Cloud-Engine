package net.frooastside.engine.gui.font;

import net.frooastside.engine.resource.ResourceItem;
import net.frooastside.engine.resource.texture.Texture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class Font extends ResourceItem {

  public static final Character UNSUPPORTED_CHARACTER = new Character(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
  public static final double LINE_HEIGHT = 0.03f;
  public static final int SPACE_ASCII = 32;

  private final Map<Integer, Character> supportedCharacters = new HashMap<>();
  private double spaceWidth;
  private Texture texture;

  public void loadFont(ByteBuffer fontFile, double aspectRatio, int imageSize, int characterHeight, int characterCount, int firstCharacter) {
    STBTTFontinfo fontInfo = new STBTTFontinfo(MemoryUtil.memAlloc(160));
    STBTruetype.stbtt_InitFont(fontInfo, fontFile);
    ByteBuffer pixelBuffer = MemoryUtil.memAlloc(imageSize * imageSize);
    STBTTBakedChar.Buffer characterBuffer = STBTTBakedChar.malloc(characterCount);
    STBTruetype.stbtt_BakeFontBitmap(fontFile, characterHeight, pixelBuffer, imageSize, imageSize, firstCharacter, characterBuffer);
    texture = new Texture(pixelBuffer, Texture.BILINEAR_FILTER, imageSize, imageSize, 1, GL11.GL_RED);
    texture.run();
    double verticalPerPixelSize = LINE_HEIGHT / (double) characterHeight;
    double horizontalPerPixelSize = verticalPerPixelSize / aspectRatio;
    System.out.println("verticalPerPixelSize: " + verticalPerPixelSize);
    System.out.println("horizontalPerPixelSize: " + horizontalPerPixelSize);
    for (int i = 0; i < characterCount; i++) {
      STBTTBakedChar character = characterBuffer.get(i);
      int asciiCharacter = i + firstCharacter;
      if(asciiCharacter != SPACE_ASCII) {
        double x = character.x0();
        double y = character.y0();
        double width = character.x1() - x;
        double height = character.y1() - y;
        double xTextureCoordinate = x / imageSize;
        double yTextureCoordinate = y / imageSize;
        if(asciiCharacter == 65) {
          System.out.println(x + ", " + y + ", " + width + ", " + height + ", " + (x / imageSize) + ", " + (y / imageSize) + ", ");
        }
        supportedCharacters.put(i + firstCharacter, new Character(
          asciiCharacter,
          xTextureCoordinate,
          yTextureCoordinate,
          xTextureCoordinate + width / imageSize,
          yTextureCoordinate + height / imageSize,
          character.xoff() * horizontalPerPixelSize,
          character.yoff() * verticalPerPixelSize,
          width * horizontalPerPixelSize,
          height * verticalPerPixelSize,
          character.xadvance() * horizontalPerPixelSize));
      }else {
        spaceWidth = character.xadvance() * horizontalPerPixelSize;
      }
    }
  }

  public Character getCharacter(int asciiCharacter) {
    return supportedCharacters.getOrDefault(asciiCharacter, UNSUPPORTED_CHARACTER);
  }

  public double spaceWidth() {
    return spaceWidth;
  }

  public Texture texture() {
    return texture;
  }

  @Override
  public Runnable getThreadSpecificLoader() {
    return null;
  }

  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeObject(supportedCharacters);
    out.writeDouble(spaceWidth);
    out.writeObject(texture);
  }

  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    for(Map.Entry<?, ?> entry : ((Map<?, ?>) in.readObject()).entrySet()) {
      if(entry.getKey() instanceof Integer && entry.getValue() instanceof Character) {
        supportedCharacters.put((Integer) entry.getKey(), (Character) entry.getValue());
      }
    }
    spaceWidth = in.readDouble();
    texture = (Texture) in.readObject();
  }
}
