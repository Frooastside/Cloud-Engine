package love.polardivision.engine.glwrapper.texture;

import java.nio.ByteBuffer;
import love.polardivision.engine.glwrapper.DataType;
import love.polardivision.engine.glwrapper.SizedGraphicalObject;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class Texture extends SizedGraphicalObject {

  public static final int NO_FILTER = 0x1;
  public static final int BILINEAR_FILTER = 0x2;
  public static final int TRILINEAR_FILTER = 0x4;
  public static final int ANISOTROPIC_FILTER = 0x8;

  private ByteBuffer pixelBuffer;

  private int filter = NO_FILTER;
  private DataType dataType = DataType.UNSIGNED_BYTE;
  private ColorFormat internalFormat;
  private ColorFormat inputFormat;

  public Texture() {
  }

  public Texture(ByteBuffer pixelBuffer, int filter, int width, int height, int channels) {
    this(pixelBuffer, filter, width, height, ColorFormat.formatFromChannelCount(channels), ColorFormat.formatFromChannelCount(channels));
  }

  public Texture(ByteBuffer pixelBuffer, int filter, int width, int height, ColorFormat internalFormat, ColorFormat inputFormat) {
    this(pixelBuffer, filter, width, height, internalFormat, inputFormat, DataType.UNSIGNED_BYTE);
  }

  public Texture(ByteBuffer pixelBuffer, int filter, int width, int height, ColorFormat internalFormat, ColorFormat inputFormat, DataType dataType) {
    this.setWidth(width);
    this.setHeight(height);
    this.pixelBuffer = pixelBuffer;
    this.filter = filter;
    this.internalFormat = internalFormat;
    this.inputFormat = inputFormat;
    this.dataType = dataType;
  }

  protected Texture set(Texture source) {
    this.setIdentifier(source.identifier());
    this.setWidth(source.width());
    this.setHeight(source.height());
    this.pixelBuffer = source.pixelBuffer;
    this.filter = source.filter;
    this.dataType = source.dataType;
    this.internalFormat = source.internalFormat;
    this.inputFormat = source.inputFormat;
    return this;
  }

  @Override
  public void initialize() {
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
    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, internalFormat.value(), width(), height(), 0, inputFormat.value(), dataType.value(), pixelBuffer);
    applyFilters();
  }

  public void read() {
    GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, inputFormat.value(), dataType.value(), pixelBuffer);
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

  private void anisotropicFilter() throws UnsupportedOperationException {
    if (GL.getCapabilities().GL_EXT_texture_filter_anisotropic) {
      float maxFilterLevel = GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT);
      GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, maxFilterLevel);
    } else {
      throw new UnsupportedOperationException();
    }
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

  public DataType dataType() {
    return dataType;
  }

  public void setDataType(DataType dataType) {
    this.dataType = dataType;
  }

  public ColorFormat internalFormat() {
    return internalFormat;
  }

  public void setInternalFormat(ColorFormat internalFormat) {
    this.internalFormat = internalFormat;
  }

  public ColorFormat inputFormat() {
    return inputFormat;
  }

  public void setInputFormat(ColorFormat inputFormat) {
    this.inputFormat = inputFormat;
  }

}
