/*
 * Copyright 2022 @Frooastside
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package love.polardivision.engine.glwrapper.framebuffer;

import java.util.ArrayList;
import java.util.List;
import love.polardivision.engine.glwrapper.SizedGraphicalObject;
import love.polardivision.engine.glwrapper.framebuffer.attachments.BufferAttachment;
import love.polardivision.engine.glwrapper.framebuffer.attachments.TextureAttachment;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class FrameBufferObject extends SizedGraphicalObject {

  private final List<FrameBufferAttachment> attachments = new ArrayList<>();

  public FrameBufferObject(int width, int height) {
    setSize(width, height);
  }

  @Override
  public void initialize() {
    setIdentifier(GL30.glGenFramebuffers());
  }

  @Override
  public void bind() {
    bind(GL30.GL_FRAMEBUFFER);
  }

  public void bind(int target) {
    GL30.glBindFramebuffer(target, identifier());
  }

  @Override
  public void unbind() {
    unbind(GL30.GL_FRAMEBUFFER);
  }

  public void unbind(int target) {
    GL30.glBindFramebuffer(target, 0);
  }

  @Override
  public void delete() {
    GL30.glDeleteFramebuffers(identifier());
  }

  public void appendTextureAttachment(TextureAttachment attachment) {
    attachments.add(attachment);
    attachment.initialize();
    attachment.bind();
    attachment.store();
    GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, attachment.attachment(), GL11.GL_TEXTURE_2D, identifier(), 0);
    attachment.unbind();
  }

  public void appendRenderBufferAttachment(BufferAttachment attachment) {
    attachments.add(attachment);
    attachment.initialize();
    attachment.bind();
    attachment.store();
    GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, attachment.attachment(), GL30.GL_RENDERBUFFER, identifier());
    attachment.unbind();
  }

  public void resize(int width, int height) {
    setSize(width, height);
    attachments.forEach(attachment -> attachment.setSize(width, height));
  }

  public void resetViewport() {
    GL11.glViewport(0, 0, width(), height());
  }

  public void selectDrawOutputs(int... attachments) {
    GL30.glDrawBuffers(attachments);
  }

  public void copyFrameBuffer(FrameBufferObject output) {
    output.bind(GL30.GL_DRAW_FRAMEBUFFER);
    bind(GL30.GL_READ_FRAMEBUFFER);
    blitFrameBuffer(0, 0, output.width(), output.height(), GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT, GL11.GL_NEAREST);
  }

  public void blitFrameBuffer(int outputX, int outputY, int outputWidth, int outputHeight, int mask, int filter) {
    blitFrameBuffer(0, 0, width(), height(), outputX, outputY, outputWidth, outputHeight, mask, filter);
  }

  public void blitFrameBuffer(int x, int y, int width, int height, int outputX, int outputY, int outputWidth, int outputHeight, int mask, int filter) {
    GL30.glBlitFramebuffer(x, y, width, height, outputX, outputY, outputWidth, outputHeight, mask, filter);
  }

  public List<FrameBufferAttachment> attachments() {
    return attachments;
  }
}