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

package love.polardivision.engine.glwrapper.framebuffer.attachments;

import love.polardivision.engine.glwrapper.SizedGraphicalObject;
import love.polardivision.engine.glwrapper.framebuffer.FrameBufferAttachment;
import love.polardivision.engine.glwrapper.texture.ColorFormat;
import org.lwjgl.opengl.GL30;

public class BufferAttachment extends SizedGraphicalObject implements FrameBufferAttachment {

  private final int attachment;
  private final int internalFormat;
  private final int samples;

  public BufferAttachment(int attachment, int samples, ColorFormat internalFormat, int width, int height) {
    this(attachment, samples, internalFormat.value(), width, height);
  }

  public BufferAttachment(int attachment, int samples, int internalFormat, int width, int height) {
    this.attachment = attachment;
    this.samples = samples;
    this.internalFormat = internalFormat;
    setSize(width, height);
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
      GL30.glRenderbufferStorageMultisample(GL30.GL_RENDERBUFFER, samples, internalFormat, width(), height());
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
