package net.frooastside.engine.graphicobjects.framebuffer.attachments;

import net.frooastside.engine.graphicobjects.framebuffer.FrameBufferAttachment;
import net.frooastside.engine.graphicobjects.texture.Texture;

public class TextureAttachment extends Texture implements FrameBufferAttachment {

  private final int attachment;

  public TextureAttachment(int attachment, Texture texture) {
    this.attachment = attachment;
    set(texture);
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
