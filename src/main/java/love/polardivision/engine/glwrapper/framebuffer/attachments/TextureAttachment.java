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

import love.polardivision.engine.glwrapper.DataType;
import love.polardivision.engine.glwrapper.framebuffer.FrameBufferAttachment;
import love.polardivision.engine.glwrapper.texture.ColorFormat;
import love.polardivision.engine.glwrapper.texture.Texture;

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
