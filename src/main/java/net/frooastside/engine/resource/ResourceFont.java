package net.frooastside.engine.resource;

import net.frooastside.engine.graphicobjects.texture.Texture;
import org.joml.Vector2f;
import org.lwjgl.stb.*;
import org.lwjgl.system.MemoryUtil;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ResourceFont implements ResourceItem {

  public static final Character UNSUPPORTED_CHARACTER = new Character(0, 0, 0, 0, 0, 0, 0, 0, 0);
  public static final int SPACE_ASCII = 32;

  private final Map<Integer, Character> supportedCharacters = new HashMap<>();
  private int characterHeight;
  private ResourceTexture texture;

  private int imageSize;
  private final int padding = 64;
  private int firstCharacter;
  private int characterCount;

  private ByteBuffer rawFile;

  public ResourceFont(ByteBuffer fontFile) {
    this(fontFile, 16384, 32, 352, (int) (1024.0f * 1.5f));
  }

  public ResourceFont(ByteBuffer fontFile, int imageSize, int firstCharacter, int characterCount, int characterHeight) {
    this.rawFile = fontFile;
    this.imageSize = imageSize;
    this.firstCharacter = firstCharacter;
    this.characterCount = characterCount;
    this.characterHeight = characterHeight;
  }

  public ResourceFont() {
  }

  private void initFont(ByteBuffer fontFile) {
    STBTTFontinfo fontInfo = new STBTTFontinfo(MemoryUtil.memAlloc(160));
    STBTruetype.stbtt_InitFont(fontInfo, fontFile);
    fontInfo.free();
  }

  private void addCharacters(STBTTPackedchar.Buffer characterBuffer, int characterCount, int firstCharacter, int imageSize) {
    for (int i = 0; i < characterCount; i++) {
      STBTTPackedchar character = characterBuffer.get(i);
      int asciiCharacter = i + firstCharacter;
      addCharacter(character, asciiCharacter, imageSize);
    }
  }

  private void addCharacter(STBTTPackedchar character, int asciiCharacter, int imageSize) {
    double x = character.x0() - padding;
    double y = character.y0() - padding;
    double width = (character.x1() + padding) - x;
    double height = (character.y1() + padding) - y;
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
    return texture.getThreadSpecificLoader();
  }

  @Override
  public Runnable getThreadUnspecificLoader() {
    return () -> {
      if(texture != null) {
        texture.getThreadUnspecificLoader().run();
      }else {
        if (rawFile != null && rawFile.hasRemaining()) {
          initFont(rawFile);
          ByteBuffer pixelBuffer = MemoryUtil.memAlloc(imageSize * imageSize);

          STBTTPackContext packContext = STBTTPackContext.malloc();
          STBTTPackedchar.Buffer characterBuffer = STBTTPackedchar.malloc(characterCount);
          STBTruetype.stbtt_PackBegin(packContext, pixelBuffer, imageSize, imageSize, 0, padding * 2);
          STBTruetype.stbtt_PackSetSkipMissingCodepoints(packContext, false);
          STBTruetype.stbtt_PackFontRange(packContext, rawFile, 0, characterHeight, firstCharacter, characterBuffer);
          STBTruetype.stbtt_PackEnd(packContext);

          /*byte[] pixelArray = BufferUtils.copyToArray(pixelBuffer);
          boolean[][] bitmap = new boolean[imageSize][imageSize];
          BufferedImage bufferedImage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_BYTE_GRAY);
          for (int y = 0; y < imageSize; y++) {
            for (int x = 0; x < imageSize; x++) {
              byte rgb = pixelArray[y * imageSize + x];
              //System.out.println(rgb);
              bitmap[x][y] = rgb == (byte) 255;
              bufferedImage.setRGB(x, y, rgb);
            }
          }
          int downscale = 4;
          float spread = 32;
          int downscaledImageSize = imageSize / downscale;
          final ByteBuffer distanceFieldBuffer = ByteBuffer.allocateDirect(downscaledImageSize * downscaledImageSize);
          ExecutorService executorService = Executors.newFixedThreadPool(6);
          for (int y = 0; y < downscaledImageSize; y++) {
            for (int x = 0; x < downscaledImageSize; x++) {
              final int centerX = x * downscale + downscale / 2;
              final int centerY = y * downscale + downscale / 2;
              final boolean base = bitmap[centerX][centerY];
              final int delta = (int) Math.ceil(spread);
              final int startX = Math.max(0, centerX - delta);
              final int startY = Math.max(0, centerY - delta);
              final int endX = Math.min(imageSize - 1, centerX + delta);
              final int endY = Math.min(imageSize - 1, centerY + delta);
              int finalY = y;
              int finalX = x;
              executorService.execute(() -> {
                int closestSquareDistance = delta * delta;
                for (int j = startY; j < endY; j++) {
                  for (int i = startX; i < endX; i++) {
                    if(base != bitmap[i][j]) {
                      int squareDistance = (int) Vector2f.distanceSquared(centerX, centerY, i, j);
                      if(squareDistance < closestSquareDistance) {
                        closestSquareDistance = squareDistance;
                      }
                    }
                  }
                }
                float closestDistance = (float) Math.sqrt(closestSquareDistance);
                float distance = (base ? 1 : -1) * Math.min(closestDistance, spread);
                float alpha = 0.5f + 0.5f * distance / spread;
                int alphaByte = (int) (Math.min(1.0f, Math.max(0.0f, alpha)) * 255.0f);
                synchronized (distanceFieldBuffer) {
                  distanceFieldBuffer.put(finalY * downscaledImageSize + finalX, (byte) alphaByte);
                }
              });
            }
          }

          executorService.shutdown();
          try {
            if(!executorService.awaitTermination(200, TimeUnit.SECONDS)) {
              System.out.println("HREYHYHYHHYHYHYHYHAAAAAAA");
            }

          } catch (InterruptedException exception) {
            System.out.println("HEYYAYA");
          }

          distanceFieldBuffer.flip();*/

          //STBTTBakedChar.Buffer characterBuffer = STBTTBakedChar.malloc(characterCount);
          //STBTruetype.stbtt_BakeFontBitmap(rawFile, characterHeight, pixelBuffer, imageSize, imageSize, firstCharacter, characterBuffer);
          //texture = new ResourceTexture(distanceFieldBuffer, Texture.BILINEAR_FILTER, downscaledImageSize, downscaledImageSize, 1);
          texture = new ResourceTexture(pixelBuffer, Texture.BILINEAR_FILTER, imageSize, imageSize, 1);
          addCharacters(characterBuffer, characterCount, firstCharacter, imageSize);
          characterBuffer.free();
        }
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
    texture = (ResourceTexture) in.readObject();
  }

  public Character getCharacter(int asciiCharacter) {
    return supportedCharacters.getOrDefault(asciiCharacter, UNSUPPORTED_CHARACTER);
  }

  public int characterHeight() {
    return characterHeight;
  }

  public ResourceTexture texture() {
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
