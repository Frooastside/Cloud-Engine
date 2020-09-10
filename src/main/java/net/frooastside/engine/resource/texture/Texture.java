package net.frooastside.engine.resource.texture;

import net.frooastside.engine.language.I18n;
import net.frooastside.engine.resource.ResourceItem;
import org.lwjgl.opengl.*;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.util.Base64;

public class Texture extends ResourceItem implements Runnable {

  public static final int NO_FILTER = 0x1;
  public static final int BILINEAR_FILTER = 0x2;
  public static final int TRILINEAR_FILTER = 0x4;
  public static final int ANISOTROPIC_FILTER = 0x8;

  private int identifier;

  private ByteBuffer pixelBuffer;
  private int filter;
  private int width;
  private int height;
  private int internalFormat;
  private int inputFormat;

  public Texture(ByteBuffer pixelBuffer, int filter, int width, int height, int format) {
    this.pixelBuffer = pixelBuffer;
    this.filter = filter;
    this.width = width;
    this.height = height;
    this.internalFormat = format;
    this.inputFormat = format;
  }

  public Texture(ByteBuffer pixelBuffer, int filter, int width, int height, int internalFormat, int inputFormat) {
    this.pixelBuffer = pixelBuffer;
    this.filter = filter;
    this.width = width;
    this.height = height;
    this.internalFormat = internalFormat;
    this.inputFormat = inputFormat;
  }

  public Texture() {}

  @Override
  public void run() {
    identifier = GL11.glGenTextures();
    bind();
    createImage();
    applyFilters();
    unbind();
    pixelBuffer.clear();
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

  @Override
  public Runnable getThreadSpecificLoader() {
    return this;
  }

  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeUTF(Base64.getEncoder().encodeToString(pixelBuffer.array()));
    out.writeInt(filter);
    out.writeShort(width);
    out.writeShort(height);
    out.writeInt(internalFormat);
    out.writeInt(inputFormat);
  }

  @Override
  public void readExternal(ObjectInput in) throws IOException {
    this.pixelBuffer = ByteBuffer.wrap(Base64.getDecoder().decode(in.readUTF()));
    this.filter = in.readInt();
    this.width = in.readShort();
    this.height = in.readShort();
    this.internalFormat = in.readInt();
    this.inputFormat = in.readInt();
  }
}
