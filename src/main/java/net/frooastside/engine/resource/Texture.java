package net.frooastside.engine.resource;

import net.frooastside.engine.language.I18n;
import org.lwjgl.opengl.*;
import org.lwjgl.stb.STBImage;
import org.lwjgl.stb.STBImageWrite;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;

public class Texture extends ResourceItem {

  public static final int NO_FILTER = 0x1;
  public static final int BILINEAR_FILTER = 0x2;
  public static final int TRILINEAR_FILTER = 0x4;
  public static final int ANISOTROPIC_FILTER = 0x8;

  private int identifier;

  private ByteBuffer pixelBuffer;
  private int filter = NO_FILTER;
  private int channels;
  private int width;
  private int height;
  private int internalFormat;
  private int inputFormat;

  public Texture(ByteBuffer textureFile) {
    this.rawFile = textureFile;
  }

  public Texture(ByteBuffer pixelBuffer, int filter, int width, int height, int channels) {
    this.pixelBuffer = pixelBuffer;
    this.filter = filter;
    this.width = width;
    this.height = height;
    this.channels = channels;
    if (channels == 4) {
      this.internalFormat = GL11.GL_RGBA;
      this.inputFormat = GL11.GL_RGBA;
    } else if (channels == 3) {
      this.internalFormat = GL11.GL_RGB;
      this.inputFormat = GL11.GL_RGB;
    } else if (channels == 1) {
      this.internalFormat = 1;
      this.inputFormat = GL11.GL_RED;
    }
  }

  public Texture() {
  }

  public void createImage() {
    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, internalFormat, width, height, 0, inputFormat, GL11.GL_UNSIGNED_BYTE, pixelBuffer);
  }

  public void applyFilters() {
    if ((filter & NO_FILTER) == NO_FILTER) {
      noFilter();
    }
    if ((filter & BILINEAR_FILTER) == BILINEAR_FILTER) {
      bilinearFilter();
    }
    if ((filter & TRILINEAR_FILTER) == TRILINEAR_FILTER) {
      trilinearFilter();
    }
    if ((filter & ANISOTROPIC_FILTER) == ANISOTROPIC_FILTER) {
      anisotropicFilter();
    }
  }

  public void bind() {
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, identifier);
  }

  public void unbind() {
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
  }

  public void delete() {
    GL11.glDeleteTextures(identifier);
  }

  public void saveToFile(File file) {
    if(pixelBuffer != null && pixelBuffer.hasRemaining()) {
      STBImageWrite.stbi_write_png(file.getAbsolutePath(), width, height, channels, pixelBuffer, 0);
    }
  }

  @Override
  public Runnable getThreadSpecificLoader() {
    return () -> {
      identifier = GL11.glGenTextures();
      bind();
      createImage();
      applyFilters();
      unbind();
      pixelBuffer = null;
    };
  }

  @Override
  public Runnable getThreadUnspecificLoader() {
    return () -> {
      if(rawFile != null && rawFile.hasRemaining() && (pixelBuffer == null || !pixelBuffer.hasRemaining())) {
        int[] width = new int[1];
        int[] height = new int[1];
        int[] channels = new int[1];
        if (!STBImage.stbi_info_from_memory(rawFile, width, height, channels)) {
          throw new IllegalStateException(I18n.get("error.texture.information"));
        }
        this.width = width[0];
        this.height = height[0];
        this.channels = channels[0];
        ByteBuffer pixelBuffer = STBImage.stbi_load_from_memory(rawFile, width, height, channels, 0);
        if (pixelBuffer == null) {
          throw new IllegalStateException(I18n.get("error.texture.content"));
        }
        this.pixelBuffer = copyDirect(pixelBuffer);
        STBImage.stbi_image_free(pixelBuffer);
        if (this.channels == 1) {
          this.internalFormat = 1;
          this.inputFormat = GL11.GL_RED;
        } else if (this.channels == 3) {
          this.internalFormat = GL11.GL_RGB;
          this.inputFormat = GL11.GL_RGB;
        } else if (this.channels == 4) {
          this.internalFormat = GL11.GL_RGBA;
          this.inputFormat = GL11.GL_RGBA;
        }
      }
    };
  }

  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    if ((rawFile == null || !rawFile.hasRemaining()) && pixelBuffer != null && pixelBuffer.hasRemaining()) {
      File tempFile = File.createTempFile("generated_texture", ".png");
      tempFile.deleteOnExit();
      saveToFile(tempFile);
      this.rawFile = readFile(tempFile);
    }
    if(rawFile != null) {
      byte[] rawFileArray = new byte[rawFile.remaining()];
      rawFile.get(rawFileArray);
      out.writeObject(rawFileArray);
    }
    out.writeInt(filter);
  }

  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    this.rawFile = wrapDirect((byte[]) in.readObject());
    this.filter = in.readInt();
  }

  private void noFilter() {
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
  }

  private void bilinearFilter() {
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
  }

  private void trilinearFilter() {
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
    GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
  }

  private void anisotropicFilter() {
    if (GL.getCapabilities().GL_EXT_texture_filter_anisotropic) {
      //TODO FROM CONFIG
      float maxFilterLevel = GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT);
      GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, maxFilterLevel);
    } else {
      System.err.println(I18n.get("error.texture.anisotropic"));
    }
  }
}
