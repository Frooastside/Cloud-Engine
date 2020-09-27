package net.frooastside.engine.resource;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;

public class BufferUtils {

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
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bytes.length);
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
    return wrapDirect(copyToArray(originalByteBuffer));
  }

  public static IntBuffer store3fAsGL_2_10_10_10_REV(float[] floats) {
    IntBuffer intBuffer = org.lwjgl.BufferUtils.createIntBuffer(floats.length / 3);
    for (int i = 0; i < floats.length / 3; i++) {
      int compressedInt = ((int) (floats[i * 3] * 512) << 20);
      compressedInt = compressedInt | ((int) (floats[i * 3 + 1] * 512) << 10);
      compressedInt = compressedInt | ((int) (floats[i * 3 + 2] * 512));
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
    int compressedInt = ((int) (f4 * 255) << 24);
    compressedInt = compressedInt | ((int) (f3 * 255) << 16);
    compressedInt = compressedInt | ((int) (f2 * 255) << 8);
    compressedInt = compressedInt | ((int) (f1 * 255));
    return compressedInt;
  }

  public static int convertFloatToByte(float floatValue) {
    return (int) (floatValue * 255);
  }

  public static FloatBuffer store(float[] floats) {
    FloatBuffer floatBuffer = org.lwjgl.BufferUtils.createFloatBuffer(floats.length);
    floatBuffer.put(floats);
    floatBuffer.flip();
    return floatBuffer;
  }

  public static IntBuffer store(int[] ints) {
    IntBuffer intBuffer = org.lwjgl.BufferUtils.createIntBuffer(ints.length);
    intBuffer.put(ints);
    intBuffer.flip();
    return intBuffer;
  }

  public static ShortBuffer store(short[] shorts) {
    ShortBuffer shortBuffer = org.lwjgl.BufferUtils.createShortBuffer(shorts.length);
    shortBuffer.put(shorts);
    shortBuffer.flip();
    return shortBuffer;
  }

  public static ByteBuffer store(byte[] bytes) {
    ByteBuffer byteBuffer = org.lwjgl.BufferUtils.createByteBuffer(bytes.length);
    byteBuffer.put(bytes);
    byteBuffer.flip();
    return byteBuffer;
  }

}
