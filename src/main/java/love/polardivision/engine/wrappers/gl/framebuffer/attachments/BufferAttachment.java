/*
 * Copyright © 2023 @Frooastside
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package love.polardivision.engine.wrappers.gl.framebuffer.attachments;

import love.polardivision.engine.wrappers.gl.SizedGraphicalObject;
import love.polardivision.engine.wrappers.gl.framebuffer.FrameBufferAttachment;
import love.polardivision.engine.wrappers.gl.texture.ColorFormat;
import org.lwjgl.opengl.GL30;

public class BufferAttachment extends SizedGraphicalObject implements FrameBufferAttachment {

  private final int attachment;
  private final int internalFormat;
  private final int samples;

  public BufferAttachment(
      int attachment, int samples, ColorFormat internalFormat, int width, int height) {
    this(attachment, samples, internalFormat.value(), width, height);
  }

  public BufferAttachment(int attachment, int samples, int internalFormat, int width, int height) {
    this.attachment = attachment;
    this.samples = samples;
    this.internalFormat = internalFormat;
    super.setSize(width, height);
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
      GL30.glRenderbufferStorageMultisample(
          GL30.GL_RENDERBUFFER, samples, internalFormat, width(), height());
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
