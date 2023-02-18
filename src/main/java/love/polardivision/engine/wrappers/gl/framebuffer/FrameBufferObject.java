/*
 * Copyright © 2023 @Frooastside
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package love.polardivision.engine.wrappers.gl.framebuffer;

import love.polardivision.engine.wrappers.SizedObject;
import love.polardivision.engine.wrappers.gl.SizedGraphicalObject;
import love.polardivision.engine.wrappers.gl.framebuffer.attachments.BufferAttachment;
import love.polardivision.engine.wrappers.gl.framebuffer.attachments.TextureAttachment;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;

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
    attachment.setSize(width(), height());
    GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, attachment.attachment(), GL11.GL_TEXTURE_2D, attachment.identifier(), 0);
  }

  public void appendRenderBufferAttachment(BufferAttachment attachment) {
    attachments.add(attachment);
    attachment.setSize(width(), height());
    GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, attachment.attachment(), GL30.GL_RENDERBUFFER, attachment.identifier());
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

  public static void adjustViewport(SizedObject sizedObject) {
    GL11.glViewport(0, 0, sizedObject.width(), sizedObject.height());
  }

  public static void clearDefault() {
    clearColorBuffer();
    clearDepthBuffer();
  }

  public static void clearAll() {
    clearColorBuffer();
    clearDepthBuffer();
    clearStencilBuffer();
  }

  public static void clearColorBuffer() {
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
  }

  public static void clearDepthBuffer() {
    GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
  }

  public static void clearStencilBuffer() {
    GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
  }

  public List<FrameBufferAttachment> attachments() {
    return attachments;
  }
}