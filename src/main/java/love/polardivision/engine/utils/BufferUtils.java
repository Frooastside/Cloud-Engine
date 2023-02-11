/*
 * Copyright © 2022-2023 @Frooastside
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package love.polardivision.engine.utils;

import java.io.*;
import java.nio.*;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;

public class BufferUtils {

  public static InputStream fileToStream(File file) throws FileNotFoundException {
    return new FileInputStream(file);
  }

  public static String streamToString(InputStream inputStream) {
    if (inputStream != null) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
      StringBuilder shaderSource = new StringBuilder();
      try {
        while (reader.ready()) {
          shaderSource.append(reader.readLine()).append(System.lineSeparator());
        }
        return shaderSource.toString();
      } catch (IOException exception) {
        throw new IllegalArgumentException(exception);
      }
    }
    return null;
  }

  public static ByteBuffer readFile(File file) throws IOException {
    Path path = file.toPath();
    if (Files.isReadable(path)) {
      try (SeekableByteChannel seekableByteChannel = Files.newByteChannel(path)) {
        ByteBuffer buffer = org.lwjgl.BufferUtils.createByteBuffer((int) seekableByteChannel.size());
        while (true) {
          int bytesRead = seekableByteChannel.read(buffer);
          if (bytesRead == -1 || bytesRead == 0) break;
        }
        buffer.flip();
        return buffer;
      }
    }
    return null;
  }

  public static ByteBuffer wrapDirect(byte[] bytes) {
    ByteBuffer byteBuffer = org.lwjgl.BufferUtils.createByteBuffer(bytes.length);
    byteBuffer.put(bytes);
    byteBuffer.flip();
    return byteBuffer;
  }

  public static byte[] copyToArray(ByteBuffer originalByteBuffer) {
    byte[] bytes = new byte[originalByteBuffer.remaining()];
    originalByteBuffer.asReadOnlyBuffer().get(bytes);
    return bytes;
  }

  public static ByteBuffer copyDirect(ByteBuffer originalByteBuffer) {
    ByteBuffer byteBuffer = org.lwjgl.BufferUtils.createByteBuffer(originalByteBuffer.remaining());
    byteBuffer.put(originalByteBuffer.asReadOnlyBuffer().duplicate());
    byteBuffer.flip();
    return byteBuffer;
  }

  public static IntBuffer store3fAsGL_2_10_10_10_REV(float[] floats) {
    IntBuffer intBuffer = org.lwjgl.BufferUtils.createIntBuffer(floats.length / 3);
    for (int i = 0; i < floats.length / 3; i++) {
      int compressedInt = (convertFloatToDoubleByte(floats[i * 3]) << 20);
      compressedInt = compressedInt | (convertFloatToDoubleByte(floats[i * 3 + 1]) << 10);
      compressedInt = compressedInt | (convertFloatToDoubleByte(floats[i * 3 + 2]));
      intBuffer.put(compressedInt);
    }
    intBuffer.flip();
    return intBuffer;
  }

  public static IntBuffer store4fAs1i(float[] floats) {
    IntBuffer intBuffer = org.lwjgl.BufferUtils.createIntBuffer(floats.length / 4);
    for (int i = 0; i < floats.length / 4; i++) {
      intBuffer.put(convert4fTo1i(floats[i * 4], floats[i * 4 + 1], floats[i * 4 + 2], floats[i * 4 + 3]));
    }
    intBuffer.flip();
    return intBuffer;
  }

  public static ByteBuffer store1fAs1b(float[] floats) {
    ByteBuffer byteBuffer = org.lwjgl.BufferUtils.createByteBuffer(floats.length);
    for (float floatValue : floats) {
      byteBuffer.put((byte) convertFloatToByte(floatValue));
    }
    byteBuffer.flip();
    return byteBuffer;
  }

  public static int convert4fTo1i(float f1, float f2, float f3, float f4) {
    int compressedInt = (convertFloatToByte(f4) << 24);
    compressedInt = compressedInt | (convertFloatToByte(f3) << 16);
    compressedInt = compressedInt | (convertFloatToByte(f2) << 8);
    compressedInt = compressedInt | (convertFloatToByte(f1));
    return compressedInt;
  }

  public static int convertFloatToByte(float floatValue) {
    return (int) (Math.min(Math.max(floatValue * 255, 0), 255));
  }

  public static int convertFloatToDoubleByte(float floatValue) {
    return (int) (Math.min(Math.max(floatValue * 511, 0), 511));
  }

  public static ByteBuffer store(byte[] bytes) {
    ByteBuffer byteBuffer = org.lwjgl.BufferUtils.createByteBuffer(bytes.length);
    byteBuffer.put(bytes);
    byteBuffer.flip();
    return byteBuffer;
  }

  public static ShortBuffer store(short[] shorts) {
    ShortBuffer shortBuffer = org.lwjgl.BufferUtils.createShortBuffer(shorts.length);
    shortBuffer.put(shorts);
    shortBuffer.flip();
    return shortBuffer;
  }

  public static IntBuffer store(int[] ints) {
    IntBuffer intBuffer = org.lwjgl.BufferUtils.createIntBuffer(ints.length);
    intBuffer.put(ints);
    intBuffer.flip();
    return intBuffer;
  }

  public static FloatBuffer store(float[] floats) {
    FloatBuffer floatBuffer = org.lwjgl.BufferUtils.createFloatBuffer(floats.length);
    floatBuffer.put(floats);
    floatBuffer.flip();
    return floatBuffer;
  }

  public static DoubleBuffer store(double[] doubles) {
    DoubleBuffer doubleBuffer = org.lwjgl.BufferUtils.createDoubleBuffer(doubles.length);
    doubleBuffer.put(doubles);
    doubleBuffer.flip();
    return doubleBuffer;
  }

}
