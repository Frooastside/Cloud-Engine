package love.polardivision.engine.glwrapper.framebuffer.attachments;

import love.polardivision.engine.glwrapper.framebuffer.FrameBufferAttachment;
import love.polardivision.engine.glwrapper.texture.Texture;

public class TextureAttachment extends Texture implements FrameBufferAttachment {

  private final int attachment;

  public TextureAttachment(int attachment, Texture texture) {
    this.attachment = attachment;
    set(texture);
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
