package love.polardivision.engine.glwrapper.framebuffer.attachments;

import love.polardivision.engine.glwrapper.SizedGraphicalObject;
import love.polardivision.engine.glwrapper.framebuffer.FrameBufferAttachment;
import love.polardivision.engine.glwrapper.texture.ColorFormat;
import org.lwjgl.opengl.GL30;

public class BufferAttachment extends SizedGraphicalObject implements FrameBufferAttachment {

  private final int attachment;
  private final int internalFormat;
  private final int samples;

  public BufferAttachment(int attachment, int samples, ColorFormat internalFormat, int width, int height) {
    this(attachment, samples, internalFormat.value(), width, height);
  }

  public BufferAttachment(int attachment, int samples, int internalFormat, int width, int height) {
    this.attachment = attachment;
    this.samples = samples;
    this.internalFormat = internalFormat;
    setSize(width, height);
  }

  @Override
  public void initialize() {
    setIdentifier(GL30.glGenRenderbuffers());
  }

  @Override
  public void bind() {
    GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, identifier());
  }

  @Override
  public void unbind() {
    GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);
  }

  @Override
  public void delete() {
    GL30.glDeleteRenderbuffers(identifier());
  }

  public void store() {
    if (samples == 0) {
      GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, internalFormat, width(), height());
    } else {
      GL30.glRenderbufferStorageMultisample(GL30.GL_RENDERBUFFER, samples, internalFormat, width(), height());
    }
  }

  @Override
  public void setSize(int width, int height) {
    super.setSize(width, height);
    bind();
    store();
    unbind();
  }

  @Override
  public int attachment() {
    return attachment;
  }
}
