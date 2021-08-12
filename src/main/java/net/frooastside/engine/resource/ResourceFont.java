package net.frooastside.engine.resource;

import net.frooastside.engine.graphicobjects.Font;
import net.frooastside.engine.graphicobjects.texture.Texture;
import net.frooastside.engine.postprocessing.SignedDistanceFieldTask;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

public class ResourceFont extends Font implements ResourceItem {

  private SignedDistanceFieldTask signedDistanceFieldTask;

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
        signedDistanceFieldTask.waitForCompletion();
        int downscaledImageSize = imageSize / downscale;
        Texture texture = new Texture(signedDistanceFieldTask.distanceFieldBuffer(), Texture.BILINEAR_FILTER, downscaledImageSize, downscaledImageSize, 1);
        ResourceTexture resourceTexture = ResourceTexture.clone(texture);
        setTexture(resourceTexture);
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
  public float progress() {
    return signedDistanceFieldTask != null ? signedDistanceFieldTask.progress() : 0;
  }

  public int imageSize() {
    return imageSize;
  }

  public void setImageSize(int imageSize) {
    this.imageSize = imageSize;
  }

  public int downscale() {
    return downscale;
  }

  public void setDownscale(int downscale) {
    this.downscale = downscale;
  }

  public float spread() {
    return spread;
  }

  public void setSpread(float spread) {
    this.spread = spread;
  }

  public int padding() {
    return padding;
  }

  public void setPadding(int padding) {
    this.padding = padding;
  }

  public int firstCharacter() {
    return firstCharacter;
  }

  public void setFirstCharacter(int firstCharacter) {
    this.firstCharacter = firstCharacter;
  }

  public int characterCount() {
    return characterCount;
  }

  public void setCharacterCount(int characterCount) {
    this.characterCount = characterCount;
  }
}
