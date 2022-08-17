package net.frooastside.engine.resource;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import net.frooastside.engine.graphicobjects.texture.Texture;
import net.frooastside.engine.language.I18n;
import org.lwjgl.stb.STBImage;
import org.lwjgl.stb.STBImageWrite;

public class ResourceTexture extends Texture implements ResourceItem {

  private static final long serialVersionUID = 5195096345989696754L;

  private ByteBuffer rawFile;
  private int channel;

  public ResourceTexture(ByteBuffer rawFile) {
    this.rawFile = rawFile;
  }

  public ResourceTexture() {
  }

  public void saveToFile(File file) {
    if (isPixelBufferEmpty()) {
      bind();
      read();
      unbind();
    }
    if (channel == 0 || channel == -1) {
      channel = channelCountFor(internalFormat());
    }
    STBImageWrite.stbi_write_png(file.getAbsolutePath(), width(), height(), channel, pixelBuffer(), 0);
  }

  public void readFromFile() {
    if (!isRawFileEmpty() && isPixelBufferEmpty()) {
      int[] width = new int[1];
      int[] height = new int[1];
      int[] channels = new int[1];
      if (!STBImage.stbi_info_from_memory(rawFile, width, height, channels)) {
        throw new IllegalStateException(I18n.get("error.texture.information"));
      }
      this.setWidth(width[0]);
      this.setHeight(height[0]);
      this.channel = channels[0];
      ByteBuffer pixelBuffer = STBImage.stbi_load_from_memory(rawFile, width, height, channels, 0);
      if (pixelBuffer == null) {
        throw new IllegalStateException(I18n.get("error.texture.content"));
      }
      this.setPixelBuffer(BufferUtils.copyDirect(pixelBuffer));
      STBImage.stbi_image_free(pixelBuffer);
      this.setInternalFormat(internalFormatFor(channel));
      this.setInputFormat(inputFormatFor(channel));
    }
  }

  @Override
  public void loadContextSpecific() {
    generateIdentifier();
    bind();
    store();
    unbind();
  }

  @Override
  public void loadUnspecific(ExecutorService executorService) {
    readFromFile();
  }

  @Override
  public void addQueueTasks(ExecutorService executorService, Queue<Runnable> contextSpecificQueue) {
    executorService.submit(() -> {
      loadUnspecific(executorService);
      contextSpecificQueue.offer(this::loadContextSpecific);
    });
  }

  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    if (isRawFileEmpty()) {
      if (isPixelBufferEmpty()) {
        bind();
        read();
        unbind();
      }
      File tempFile = File.createTempFile("generated_texture_", ".png");
      tempFile.deleteOnExit();
      saveToFile(tempFile);
      this.rawFile = BufferUtils.readFile(tempFile);
    }
    if (rawFile != null) {
      out.writeObject(BufferUtils.copyToArray(rawFile));
    } else {
      out.writeObject(null);
    }
    out.writeInt(filter());
  }

  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    this.rawFile = BufferUtils.wrapDirect((byte[]) in.readObject());
    this.setFilter(in.readInt());
  }

  @Override
  public float progress() {
    return -1;
  }

  public static ResourceTexture clone(Texture texture) {
    return (ResourceTexture) new ResourceTexture().set(texture);
  }

  private boolean isPixelBufferEmpty() {
    return pixelBuffer() == null || !pixelBuffer().hasRemaining();
  }

  private boolean isRawFileEmpty() {
    return rawFile == null || !rawFile.hasRemaining();
  }

  public ByteBuffer rawFile() {
    return rawFile;
  }

  public void setRawFile(ByteBuffer rawFile) {
    this.rawFile = rawFile;
  }

  public int channel() {
    return channel;
  }

  public void setChannel(int channel) {
    this.channel = channel;
  }
}
