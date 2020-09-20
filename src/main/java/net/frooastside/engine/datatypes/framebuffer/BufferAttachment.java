package net.frooastside.engine.datatypes.framebuffer;

import org.lwjgl.opengl.GL30;

public class BufferAttachment extends FrameBufferAttachment {

  private final int samples;

  public BufferAttachment(int samples, int internalFormat) {
    this.samples = samples;
    this.internalFormat = internalFormat;
  }

  @Override
  public void generateIdentifier() {
    identifier = GL30.glGenRenderbuffers();
  }

  @Override
  public void bind() {
    GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, identifier);
  }

  @Override
  public void unbind() {
    GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);
  }

  @Override
  public void delete() {
    GL30.glDeleteRenderbuffers(identifier);
  }

  @Override
  public void store() {
    GL30.glRenderbufferStorageMultisample(GL30.GL_RENDERBUFFER, samples, internalFormat, width, height);
  }

  @Override
  public void appendToFrameBuffer() {
    GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, attachment, GL30.GL_RENDERBUFFER, identifier);
  }
}
