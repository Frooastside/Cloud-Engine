package net.frooastside.engine.resource;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import net.frooastside.engine.graphicobjects.texture.Texture;
import net.frooastside.engine.resource.settings.*;
import org.joml.Vector2f;
import org.lwjgl.stb.*;
import org.lwjgl.system.MemoryUtil;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ExecutorService;

public class ResourceFont implements ResourceItem {

  @Serial
  private static final long serialVersionUID = -8857587121382434365L;

  public static final Character DEFAULT_CHARACTER = new Character(0, 0, 0, 0, 0, 0, 0, 0, 0);
  public static final int SPACE_CODEPOINT = 32;

  public static final SettingsCreator SETTINGS_LAYOUT = SettingsCreator.createLayout(
    new ComboBoxSetting<>("imageSize", Arrays.asList(256, 512, 1024, 2048, 4096, 8192, 16384, 32768), 16384),
    new IntegerSpinnerSetting("downscale", 1, 32, 4),
    new IntegerSpinnerSetting("spread", 1, 128, 32),
    new ComboBoxSetting<>("padding", Arrays.asList(0, 1, 2, 4, 8, 16, 32, 64, 128, 256, 512), 64),
    new ComboBoxSetting<>("firstCharacter", Arrays.asList(0, 32), 32),
    new ComboBoxSetting<>("characterCount", Arrays.asList(224, 352, 560, 255, 383, 591), 352),
    new IntegerTextField("characterHeight", 1536));

  private Map<String, Node> settings;
  private Node settingsBox;

  private final Map<Integer, Character> supportedCharacters = new HashMap<>();
  private int characterHeight;
  private ResourceTexture texture;

  private ByteBuffer rawFile;
  private int imageSize;
  private int downscale;
  private float spread;
  private int padding;
  private int firstCharacter;
  private int characterCount;

  public ResourceFont(ByteBuffer fontFile) {
    this.rawFile = fontFile;
  }

  public ResourceFont() {
  }

  private void initializeFont(ByteBuffer fontFile) {
    STBTTFontinfo fontInfo = new STBTTFontinfo(MemoryUtil.memAlloc(160));
    STBTruetype.stbtt_InitFont(fontInfo, fontFile);
    fontInfo.free();
  }

  private void addCharacters(STBTTPackedchar.Buffer characterBuffer, int characterCount, int firstCharacter, int imageSize) {
    for (int i = 0; i < characterCount; i++) {
      STBTTPackedchar character = characterBuffer.get(i);
      int codepoint = i + firstCharacter;
      addCharacter(character, codepoint, imageSize);
    }
  }

  private void addCharacter(STBTTPackedchar character, int codepoint, int imageSize) {
    double x = character.x0() - padding;
    double y = character.y0() - padding;
    double width = (character.x1() + padding) - x;
    double height = (character.y1() + padding) - y;
    double xTextureCoordinate = x / imageSize;
    double yTextureCoordinate = y / imageSize;
    supportedCharacters.put(codepoint, new Character(
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
  public void loadContextSpecific() {
    texture.loadContextSpecific();
  }

  @Override
  public void loadUnspecific(ExecutorService executorService) {
    if (texture != null) {
      texture.loadUnspecific(executorService);
    } else {
      if (rawFile != null && rawFile.hasRemaining()) {
        initializeFont(rawFile);
        ByteBuffer pixelBuffer = MemoryUtil.memAlloc(imageSize * imageSize);

        STBTTPackContext packContext = STBTTPackContext.malloc();
        STBTTPackedchar.Buffer characterBuffer = STBTTPackedchar.malloc(characterCount);
        STBTruetype.stbtt_PackBegin(packContext, pixelBuffer, imageSize, imageSize, 0, padding * 2);
        STBTruetype.stbtt_PackSetSkipMissingCodepoints(packContext, false);
        STBTruetype.stbtt_PackFontRange(packContext, rawFile, 0, characterHeight, firstCharacter, characterBuffer);
        STBTruetype.stbtt_PackEnd(packContext);
        ByteBuffer distanceFieldBuffer = createDistanceField(executorService, pixelBuffer, imageSize, downscale, spread);
        int downscaledImageSize = imageSize / downscale;
        texture = new ResourceTexture(new Texture(distanceFieldBuffer, Texture.BILINEAR_FILTER, downscaledImageSize, downscaledImageSize, 1));
        addCharacters(characterBuffer, characterCount, firstCharacter, imageSize);
        characterBuffer.free();
      }
    }
  }

  @Override
  public void addQueueTasks(ExecutorService executorService, Queue<Runnable> contextSpecificQueue) {
    executorService.submit(() -> {
      loadUnspecific(executorService);
      contextSpecificQueue.offer(this::loadContextSpecific);
    });
  }

  private static ByteBuffer createDistanceField(ExecutorService executorService, ByteBuffer pixelBuffer, int imageSize, int downscale, float spread) {
    byte[] pixelArray = BufferUtils.copyToArray(pixelBuffer);
    boolean[][] bitmap = new boolean[imageSize][imageSize];
    BufferedImage bufferedImage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_BYTE_GRAY);
    for (int y = 0; y < imageSize; y++) {
      for (int x = 0; x < imageSize; x++) {
        byte rgb = pixelArray[y * imageSize + x];
        bitmap[x][y] = rgb < 0;
        bufferedImage.setRGB(x, y, rgb);
      }
    }
    int downscaledImageSize = imageSize / downscale;
    final ByteBuffer distanceFieldBuffer = ByteBuffer.allocateDirect(downscaledImageSize * downscaledImageSize);
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
        //TODO
        executorService.submit(() -> {
          int closestSquareDistance = delta * delta;
          for (int j = startY; j < endY; j++) {
            for (int i = startX; i < endX; i++) {
              if (base != bitmap[i][j]) {
                int squareDistance = (int) Vector2f.distanceSquared(centerX, centerY, i, j);
                if (squareDistance < closestSquareDistance) {
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
    return distanceFieldBuffer;
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

  @Override
  public Node settingsBox() {
    if (settings == null) {
      settings = SETTINGS_LAYOUT.createSettings();
      if (imageSize == 0
        && downscale == 0
        && spread == 0
        && padding == 0
        && firstCharacter == 0
        && characterCount == 0) {
        recalculate();
      } else {
        setSettings();
      }
    }
    if (settingsBox == null) {
      settingsBox = SettingsCreator.getBox(settings);
    }
    return settingsBox;
  }

  @Override
  public Node informationBox() {
    VBox informationBox = new VBox();
    informationBox.setAlignment(Pos.CENTER);
    informationBox.getChildren().addAll(
      new Label("ImageSize: " + imageSize),
      new Label("Downscale: " + downscale),
      new Label("Spread: " + spread),
      new Label("Padding: " + padding),
      new Label("FirstCharacter: " + firstCharacter),
      new Label("CharacterCount: " + characterCount),
      new Label("CharacterHeight: " + characterHeight)
    );
    return informationBox;
  }

  public void setSettings() {
    Setting.setComboBoxItem(settings, "imageSize", imageSize);
    Setting.setSpinnerValue(settings, "downscale", downscale);
    Setting.setSpinnerValue(settings, "spread", (int) spread);
    Setting.setComboBoxItem(settings, "padding", padding);
    Setting.setComboBoxItem(settings, "firstCharacter", firstCharacter);
    Setting.setComboBoxItem(settings, "characterCount", characterCount);
    Setting.setTextFieldInteger(settings, "characterHeight", characterHeight);
  }

  @Override
  public void recalculate() {
    Object imageSize = Setting.getComboBoxItem(settings, "imageSize");
    if (imageSize != null) {
      this.imageSize = (int) imageSize;
    }
    this.downscale = Setting.getSpinnerInteger(settings, "downscale");
    this.spread = Setting.getSpinnerInteger(settings, "spread");
    Object padding = Setting.getComboBoxItem(settings, "padding");
    if (padding != null) {
      this.padding = (int) padding;
    }
    Object firstCharacter = Setting.getComboBoxItem(settings, "firstCharacter");
    if (firstCharacter != null) {
      this.firstCharacter = (int) firstCharacter;
    }
    Object characterCount = Setting.getComboBoxItem(settings, "characterCount");
    if (characterCount != null) {
      this.characterCount = (int) characterCount;
    }
    this.characterHeight = Setting.getTextFieldInteger(settings, "characterHeight");
  }

  public Character getCharacter(int codepoint) {
    return supportedCharacters.getOrDefault(codepoint, DEFAULT_CHARACTER);
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
