package net.frooastside.engine.graphicobjects.framebuffer.attachments;

import net.frooastside.engine.graphicobjects.framebuffer.FrameBufferAttachment;
import net.frooastside.engine.graphicobjects.texture.Texture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class TextureAttachment extends Texture implements FrameBufferAttachment {

  private int attachment;

  public TextureAttachment(Texture texture) {
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
  public void appendToFrameBuffer() {
    generateIdentifier();
    bind();
    store();
    GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, attachment, GL11.GL_TEXTURE_2D, identifier(), 0);
    unbind();
  }

  @Override
  public int attachment() {
    return attachment;
  }

  @Override
  public void setAttachment(int attachment) {
    this.attachment = attachment;
  }
}
