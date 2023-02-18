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

import love.polardivision.engine.wrappers.gl.DataType;
import love.polardivision.engine.wrappers.gl.framebuffer.FrameBufferAttachment;
import love.polardivision.engine.wrappers.gl.texture.ColorFormat;
import love.polardivision.engine.wrappers.gl.texture.Texture;

public class TextureAttachment extends Texture implements FrameBufferAttachment {

  private final int attachment;

  public TextureAttachment(int attachment, int filter, int width, int height, int channels) {
    this(attachment, filter, width, height, ColorFormat.formatFromChannelCount(channels), ColorFormat.formatFromChannelCount(channels));
  }

  public TextureAttachment(int attachment, int filter, int width, int height, ColorFormat internalFormat, ColorFormat inputFormat) {
    this(attachment, filter, width, height, internalFormat, inputFormat, DataType.UNSIGNED_BYTE);
  }

  public TextureAttachment(int attachment, int filter, int width, int height, ColorFormat internalFormat, ColorFormat inputFormat, DataType dataType) {
    this.attachment = attachment;
    this.setWidth(width);
    this.setHeight(height);
    this.setFilter(filter);
    this.setInternalFormat(internalFormat);
    this.setInputFormat(inputFormat);
    this.setDataType(dataType);
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
