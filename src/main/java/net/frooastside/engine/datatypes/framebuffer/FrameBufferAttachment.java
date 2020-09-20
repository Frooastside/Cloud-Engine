package net.frooastside.engine.datatypes.framebuffer;

import net.frooastside.engine.datatypes.GLObject;

public abstract class FrameBufferAttachment extends GLObject {

  protected int internalFormat;
  protected int attachment;
  protected int width;
  protected int height;

  public void resize(int width, int height) {
    this.width = width;
    this.height = height;
    bind();
    store();
    unbind();
  }

  public abstract void bind();

  public abstract void unbind();

  public abstract void delete();

  public abstract void store();

  public abstract void appendToFrameBuffer();

  public void setAttachment(int attachment) {
    this.attachment = attachment;
  }
}
