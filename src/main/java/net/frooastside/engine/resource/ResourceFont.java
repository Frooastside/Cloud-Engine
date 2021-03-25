package net.frooastside.engine.resource;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import net.frooastside.engine.graphicobjects.Font;
import net.frooastside.engine.graphicobjects.texture.Texture;
import net.frooastside.engine.postprocessing.SignedDistanceFieldTask;
import net.frooastside.engine.resource.settings.*;
import org.lwjgl.stb.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ExecutorService;

public class ResourceFont extends Font implements ResourceItem {

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
  private SignedDistanceFieldTask signedDistanceFieldTask;
  private float progress;

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
    supportedCharacters().put(codepoint, new Character(
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
    texture().loadContextSpecific();
  }

  @Override
  public void loadUnspecific(ExecutorService executorService) {
    if (texture() != null) {
      texture().loadUnspecific(executorService);
    } else {
      if (rawFile != null && rawFile.hasRemaining()) {
        initializeFont(rawFile);
        ByteBuffer pixelBuffer = MemoryUtil.memAlloc(imageSize * imageSize);

        STBTTPackContext packContext = STBTTPackContext.malloc();
        STBTTPackedchar.Buffer characterBuffer = STBTTPackedchar.malloc(characterCount);
        STBTruetype.stbtt_PackBegin(packContext, pixelBuffer, imageSize, imageSize, 0, padding * 2);
        STBTruetype.stbtt_PackSetSkipMissingCodepoints(packContext, false);
        STBTruetype.stbtt_PackFontRange(packContext, rawFile, 0, characterHeight(), firstCharacter, characterBuffer);
        STBTruetype.stbtt_PackEnd(packContext);
        signedDistanceFieldTask = new SignedDistanceFieldTask(pixelBuffer, imageSize, downscale, spread);
        signedDistanceFieldTask.generate();
        while(!signedDistanceFieldTask.finished()) {
          signedDistanceFieldTask.progress();
        }
        int downscaledImageSize = imageSize / downscale;
        setTexture(ResourceTexture.clone(new Texture(signedDistanceFieldTask.distanceFieldBuffer(), Texture.BILINEAR_FILTER, downscaledImageSize, downscaledImageSize, 1)));
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
      new Label("CharacterHeight: " + characterHeight())
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
    Setting.setTextFieldInteger(settings, "characterHeight", characterHeight());
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
    setCharacterHeight(Setting.getTextFieldInteger(settings, "characterHeight"));
  }
}
