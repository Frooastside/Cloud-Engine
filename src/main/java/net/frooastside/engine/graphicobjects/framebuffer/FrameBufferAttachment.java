package net.frooastside.engine.graphicobjects.framebuffer;

import net.frooastside.engine.graphicobjects.GraphicObject;

public abstract class FrameBufferAttachment extends GraphicObject {

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

  public int width() {
    return width;
  }

  public int height() {
    return height;
  }
}
