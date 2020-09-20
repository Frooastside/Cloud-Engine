package net.frooastside.engine.datatypes.texture;

import net.frooastside.engine.datatypes.framebuffer.FrameBufferAttachment;
import net.frooastside.engine.language.I18n;
import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;

public class Texture extends FrameBufferAttachment {

  public static final int NO_FILTER = 0x1;
  public static final int BILINEAR_FILTER = 0x2;
  public static final int TRILINEAR_FILTER = 0x4;
  public static final int ANISOTROPIC_FILTER = 0x8;

  protected ByteBuffer pixelBuffer;
  protected int filter;
  protected int dataType;
  protected int inputFormat;

  public Texture(ByteBuffer pixelBuffer, int filter, int width, int height, int channels) {
    this(pixelBuffer, filter, width, height, internalFormatFor(channels), inputFormatFor(channels));
  }

  public Texture(ByteBuffer pixelBuffer, int filter, int width, int height, int internalFormat, int inputFormat) {
    this(pixelBuffer, filter, width, height, internalFormat, inputFormat, GL11.GL_UNSIGNED_BYTE);
  }

  public Texture(ByteBuffer pixelBuffer, int filter, int width, int height, int internalFormat, int inputFormat, int dataType) {
    this.pixelBuffer = pixelBuffer;
    this.filter = filter;
    this.width = width;
    this.height = height;
    this.internalFormat = internalFormat;
    this.inputFormat = inputFormat;
    this.dataType = dataType;
  }

  public Texture() {
    this.filter = NO_FILTER;
    this.dataType = GL11.GL_UNSIGNED_BYTE;
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

  @Override
  public void generateIdentifier() {
    identifier = GL11.glGenTextures();
  }

  @Override
  public void bind() {
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, identifier);
  }

  @Override
  public void unbind() {
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
  }

  @Override
  public void delete() {
    GL11.glDeleteTextures(identifier);
  }

  @Override
  public void store() {
    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, internalFormat, width, height, 0, inputFormat, dataType, pixelBuffer);
    applyFilters();
  }

  @Override
  public void appendToFrameBuffer() {
    GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, attachment, GL11.GL_TEXTURE_2D, identifier, 0);
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

  protected static int internalFormatFor(int channel) {
    if (channel == 4) {
      return GL11.GL_RGBA;
    } else if (channel == 3) {
      return GL11.GL_RGB;
    } else if (channel == 1) {
      return 1;
    }
    return -1;
  }

  protected static int inputFormatFor(int channel) {
    if (channel == 4) {
      return GL11.GL_RGBA;
    } else if (channel == 3) {
      return GL11.GL_RGB;
    } else if (channel == 1) {
      return GL11.GL_RED;
    }
    return -1;
  }
}
