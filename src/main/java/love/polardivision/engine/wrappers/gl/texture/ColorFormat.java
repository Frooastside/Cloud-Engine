/*
 * Copyright © 2023 @Frooastside
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package love.polardivision.engine.wrappers.gl.texture;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GL42;

public enum ColorFormat {
  RGBA(GL11.GL_RGBA, 4),
  RGB5_A1(GL11.GL_RGB5_A1, 4),
  RGBA8(GL11.GL_RGBA8, 4),
  RGBA8_SNORM(GL31.GL_RGBA8_SNORM, 4),
  RGB10_A2(GL11.GL_RGB10_A2, 4),
  RGB10_A2UI(GL33.GL_RGB10_A2UI, 4),
  RGBA12(GL11.GL_RGBA12, 4),
  RGBA16(GL11.GL_RGBA16, 4),
  SRGB8_ALPHA8(GL21.GL_SRGB8_ALPHA8, 4),
  RGBA16F(GL30.GL_RGBA16F, 4),
  RGBA32F(GL30.GL_RGBA32F, 4),
  RGBA8I(GL30.GL_RGBA8I, 4),
  RGBA8UI(GL30.GL_RGBA8UI, 4),
  RGBA16I(GL30.GL_RGBA16I, 4),
  RGBA16UI(GL30.GL_RGBA16UI, 4),
  RGBA32I(GL30.GL_RGBA32I, 4),
  RGBA32UI(GL30.GL_RGBA32UI, 4),
  COMPRESSED_RGBA(GL13.GL_COMPRESSED_RGBA, 4),
  COMPRESSED_SRGB_ALPHA(GL21.GL_COMPRESSED_SRGB_ALPHA, 4),
  COMPRESSED_RGBA_BPTC_UNORM(GL42.GL_COMPRESSED_RGBA_BPTC_UNORM, 4),
  COMPRESSED_SRGB_ALPHA_BPTC_UNORM(GL42.GL_COMPRESSED_SRGB_ALPHA_BPTC_UNORM, 4),
  RGB(GL11.GL_RGB, 3),
  R3_G3_B2(GL11.GL_R3_G3_B2, 3),
  RGB4(GL11.GL_RGB4, 3),
  RGB5(GL11.GL_RGB5, 3),
  RGB8(GL11.GL_RGB8, 3),
  RGB8_SNORM(GL31.GL_RGB8_SNORM, 3),
  RGB10(GL11.GL_RGB10, 3),
  RGB12(GL11.GL_RGB12, 3),
  RGB16_SNORM(GL31.GL_RGB16_SNORM, 3),
  SRGB8(GL21.GL_SRGB8, 3),
  RGB16F(GL30.GL_RGB16F, 3),
  RGB32F(GL30.GL_RGB32F, 3),
  R11F_G11F_B10F(GL30.GL_R11F_G11F_B10F, 3),
  RGB9_E5(GL30.GL_RGB9_E5, 3),
  RGB8I(GL30.GL_RGB8I, 3),
  RGB8UI(GL30.GL_RGB8UI, 3),
  RGB16I(GL30.GL_RGB16I, 3),
  RGB16UI(GL30.GL_RGB16UI, 3),
  RGB32I(GL30.GL_RGB32I, 3),
  RGB32UI(GL30.GL_RGB32UI, 3),
  COMPRESSED_RGB(GL13.GL_COMPRESSED_RGB, 3),
  COMPRESSED_SRGB(GL21.GL_COMPRESSED_SRGB, 3),
  COMPRESSED_RGB_BPTC_SIGNED_FLOAT(GL42.GL_COMPRESSED_RGB_BPTC_SIGNED_FLOAT, 3),
  RED(GL11.GL_RED, 1),
  R8(GL30.GL_R8, 1),
  R8_SNORM(GL31.GL_R8_SNORM, 1),
  R16(GL30.GL_R16, 1),
  R16_SNORM(GL31.GL_R16_SNORM, 1),
  R16F(GL30.GL_R16F, 1),
  R8I(GL30.GL_R8I, 1),
  R8UI(GL30.GL_R8UI, 1),
  R16I(GL30.GL_R16I, 1),
  R16UI(GL30.GL_R16UI, 1),
  R32I(GL30.GL_R32I, 1),
  R32UI(GL30.GL_R32UI, 1),
  COMPRESSED_RED(GL30.GL_COMPRESSED_RED, 1),
  COMPRESSED_RED_RGTC1(GL30.GL_COMPRESSED_RED_RGTC1, 1),
  COMPRESSED_SIGNED_RED_RGTC1(GL30.GL_COMPRESSED_SIGNED_RED_RGTC1, 1);

  public static final ColorFormat DEFAULT_4 = RGBA;
  public static final ColorFormat DEFAULT_3 = RGB;
  public static final ColorFormat DEFAULT_1 = RED;

  private final int value;
  private final int channels;

  ColorFormat(int value, int channels) {
    this.value = value;
    this.channels = channels;
  }

  public static ColorFormat formatFromChannelCount(int channelCount) {
    return switch (channelCount) {
      case 1 -> DEFAULT_1;
      case 3 -> DEFAULT_3;
      case 4 -> DEFAULT_4;
      default -> null;
    };
  }

  public int value() {
    return value;
  }

  public int channels() {
    return channels;
  }
}
