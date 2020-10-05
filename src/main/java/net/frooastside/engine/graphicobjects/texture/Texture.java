package net.frooastside.engine.graphicobjects.texture;

import net.frooastside.engine.graphicobjects.SizedGraphicObject;
import net.frooastside.engine.language.I18n;
import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;

public class Texture extends SizedGraphicObject {

  public static final int NO_FILTER = 0x1;
  public static final int BILINEAR_FILTER = 0x2;
  public static final int TRILINEAR_FILTER = 0x4;
  public static final int ANISOTROPIC_FILTER = 0x8;

  private ByteBuffer pixelBuffer;

  private int filter = NO_FILTER;
  private int dataType = GL11.GL_UNSIGNED_BYTE;
  private int internalFormat;
  private int inputFormat;

  public Texture() {
  }

  public Texture(ByteBuffer pixelBuffer, int filter, int width, int height, int channels) {
    this(pixelBuffer, filter, width, height, internalFormatFor(channels), inputFormatFor(channels));
  }

  public Texture(ByteBuffer pixelBuffer, int filter, int width, int height, int internalFormat, int inputFormat) {
    this(pixelBuffer, filter, width, height, internalFormat, inputFormat, GL11.GL_UNSIGNED_BYTE);
  }

  public Texture(ByteBuffer pixelBuffer, int filter, int width, int height, int internalFormat, int inputFormat, int dataType) {
    this.setWidth(width);
    this.setHeight(height);
    this.pixelBuffer = pixelBuffer;
    this.filter = filter;
    this.internalFormat = internalFormat;
    this.inputFormat = inputFormat;
    this.dataType = dataType;
  }

  protected void copyFrom(Texture source) {
    this.setIdentifier(source.identifier());
    this.setWidth(source.width());
    this.setHeight(source.height());
    this.pixelBuffer = source.pixelBuffer;
    this.filter = source.filter;
    this.dataType = source.dataType;
    this.internalFormat = source.internalFormat;
    this.inputFormat = source.inputFormat;
  }

  @Override
  public void generateIdentifier() {
    setIdentifier(GL11.glGenTextures());
  }

  @Override
  public void bind() {
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, identifier());
  }

  @Override
  public void unbind() {
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
  }

  @Override
  public void delete() {
    GL11.glDeleteTextures(identifier());
  }

  public void store() {
    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, internalFormat, width(), height(), 0, inputFormat, dataType, pixelBuffer);
    applyFilters();
  }

  public void read() {
    GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, inputFormat, dataType, pixelBuffer);
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

  protected static int channelCountFor(int internalFormat) {
    if (internalFormat == GL11.GL_RGBA ||
      internalFormat == GL11.GL_RGB5_A1 ||
      internalFormat == GL11.GL_RGBA8 ||
      internalFormat == GL31.GL_RGBA8_SNORM ||
      internalFormat == GL11.GL_RGB10_A2 ||
      internalFormat == GL33.GL_RGB10_A2UI ||
      internalFormat == GL11.GL_RGBA12 ||
      internalFormat == GL11.GL_RGBA16 ||
      internalFormat == GL21.GL_SRGB8_ALPHA8 ||
      internalFormat == GL30.GL_RGBA16F ||
      internalFormat == GL30.GL_RGBA32F ||
      internalFormat == GL30.GL_RGBA8I ||
      internalFormat == GL30.GL_RGBA8UI ||
      internalFormat == GL30.GL_RGBA16I ||
      internalFormat == GL30.GL_RGBA16UI ||
      internalFormat == GL30.GL_RGBA32I ||
      internalFormat == GL30.GL_RGBA32UI ||
      internalFormat == GL13.GL_COMPRESSED_RGBA ||
      internalFormat == GL21.GL_COMPRESSED_SRGB_ALPHA ||
      internalFormat == GL42.GL_COMPRESSED_RGBA_BPTC_UNORM ||
      internalFormat == GL42.GL_COMPRESSED_SRGB_ALPHA_BPTC_UNORM) {
      return 4;
    } else if (internalFormat == GL11.GL_RGB ||
      internalFormat == GL11.GL_R3_G3_B2 ||
      internalFormat == GL11.GL_RGB4 ||
      internalFormat == GL11.GL_RGB5 ||
      internalFormat == GL11.GL_RGB8 ||
      internalFormat == GL31.GL_RGB8_SNORM ||
      internalFormat == GL11.GL_RGB10 ||
      internalFormat == GL11.GL_RGB12 ||
      internalFormat == GL31.GL_RGB16_SNORM ||
      internalFormat == GL21.GL_SRGB8 ||
      internalFormat == GL30.GL_RGB16F ||
      internalFormat == GL30.GL_RGB32F ||
      internalFormat == GL30.GL_R11F_G11F_B10F ||
      internalFormat == GL30.GL_RGB9_E5 ||
      internalFormat == GL30.GL_RGB8I ||
      internalFormat == GL30.GL_RGB8UI ||
      internalFormat == GL30.GL_RGB16I ||
      internalFormat == GL30.GL_RGB16UI ||
      internalFormat == GL30.GL_RGB32I ||
      internalFormat == GL30.GL_RGB32UI ||
      internalFormat == GL13.GL_COMPRESSED_RGB ||
      internalFormat == GL21.GL_COMPRESSED_SRGB ||
      internalFormat == GL42.GL_COMPRESSED_RGB_BPTC_SIGNED_FLOAT ||
      internalFormat == GL42.GL_COMPRESSED_RGB_BPTC_UNSIGNED_FLOAT) {
      return 3;
    } else if (internalFormat == 1 ||
      internalFormat == GL11.GL_RED ||
      internalFormat == GL30.GL_R8 ||
      internalFormat == GL31.GL_R8_SNORM ||
      internalFormat == GL30.GL_R16 ||
      internalFormat == GL31.GL_R16_SNORM ||
      internalFormat == GL30.GL_R16F ||
      internalFormat == GL30.GL_R8I ||
      internalFormat == GL30.GL_R8UI ||
      internalFormat == GL30.GL_R16I ||
      internalFormat == GL30.GL_R16UI ||
      internalFormat == GL30.GL_R32I ||
      internalFormat == GL30.GL_R32UI ||
      internalFormat == GL30.GL_COMPRESSED_RED ||
      internalFormat == GL30.GL_COMPRESSED_RED_RGTC1 ||
      internalFormat == GL30.GL_COMPRESSED_SIGNED_RED_RGTC1) {
      return 1;
    } else {
      return -1;
    }
  }

  protected static int internalFormatFor(int channel) {
    if (channel == 4) {
      return GL11.GL_RGBA;
    } else if (channel == 3) {
      return GL11.GL_RGB;
    } else {
      return channel;
    }
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

  public ByteBuffer pixelBuffer() {
    return pixelBuffer;
  }

  public void setPixelBuffer(ByteBuffer pixelBuffer) {
    this.pixelBuffer = pixelBuffer;
  }

  public int filter() {
    return filter;
  }

  public void setFilter(int filter) {
    this.filter = filter;
  }

  public int dataType() {
    return dataType;
  }

  public void setDataType(int dataType) {
    this.dataType = dataType;
  }

  public int internalFormat() {
    return internalFormat;
  }

  public void setInternalFormat(int internalFormat) {
    this.internalFormat = internalFormat;
  }

  public int inputFormat() {
    return inputFormat;
  }

  public void setInputFormat(int inputFormat) {
    this.inputFormat = inputFormat;
  }
}
