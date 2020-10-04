package net.frooastside.engine.resource;

import net.frooastside.engine.language.I18n;
import net.frooastside.engine.graphicobjects.texture.Texture;
import org.lwjgl.stb.STBImage;
import org.lwjgl.stb.STBImageWrite;

import java.io.*;
import java.nio.ByteBuffer;

public class ResourceTexture extends Texture implements ResourceItem {

  private ByteBuffer rawFile;
  private int channel;

  public ResourceTexture(ByteBuffer rawFile) {
    this.rawFile = rawFile;
  }

  public ResourceTexture(Texture texture) {
    copyFrom(texture);
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
      channel = channelCountFor(internalFormat);
    }
    STBImageWrite.stbi_write_png(file.getAbsolutePath(), width, height, channel, pixelBuffer, 0);
  }

  public void readFromFile() {
    if (!isRawFileEmpty() && isPixelBufferEmpty()) {
      int[] width = new int[1];
      int[] height = new int[1];
      int[] channels = new int[1];
      if (!STBImage.stbi_info_from_memory(rawFile, width, height, channels)) {
        throw new IllegalStateException(I18n.get("error.texture.information"));
      }
      this.width = width[0];
      this.height = height[0];
      this.channel = channels[0];
      ByteBuffer pixelBuffer = STBImage.stbi_load_from_memory(rawFile, width, height, channels, 0);
      if (pixelBuffer == null) {
        throw new IllegalStateException(I18n.get("error.texture.content"));
      }
      this.pixelBuffer = BufferUtils.copyDirect(pixelBuffer);
      STBImage.stbi_image_free(pixelBuffer);
      this.internalFormat = internalFormatFor(channel);
      this.inputFormat = inputFormatFor(channel);
    }
  }

  @Override
  public Runnable getThreadSpecificLoader() {
    return () -> {
      generateIdentifier();
      bind();
      store();
      unbind();
    };
  }

  @Override
  public Runnable getThreadUnspecificLoader() {
    return this::readFromFile;
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
    out.writeInt(filter);
  }

  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    this.rawFile = BufferUtils.wrapDirect((byte[]) in.readObject());
    this.filter = in.readInt();
  }

  private boolean isPixelBufferEmpty() {
    return pixelBuffer == null || !pixelBuffer.hasRemaining();
  }

  private boolean isRawFileEmpty() {
    return rawFile == null || !rawFile.hasRemaining();
  }

}
