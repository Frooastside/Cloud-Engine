package net.frooastside.engine.graphicobjects.framebuffer.attachments;

import net.frooastside.engine.graphicobjects.SizedGraphicObject;
import net.frooastside.engine.graphicobjects.framebuffer.FrameBufferAttachment;
import org.lwjgl.opengl.GL30;

public class BufferAttachment extends SizedGraphicObject implements FrameBufferAttachment {

  private final int attachment;

  private final int internalFormat;
  private final int samples;

  public BufferAttachment(int attachment, int samples, int internalFormat, int width, int height) {
    this.attachment = attachment;
    this.samples = samples;
    this.internalFormat = internalFormat;
    setSize(width, height);
  }

  @Override
  public void generateIdentifier() {
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
  public void resize(int width, int height) {
    setSize(width, height);
    bind();
    store();
    unbind();
  }

  @Override
  public int attachment() {
    return attachment;
  }
}
