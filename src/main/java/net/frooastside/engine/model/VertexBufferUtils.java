package net.frooastside.engine.model;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class VertexBufferUtils {

  public static IntBuffer store3fAsGL_2_10_10_10_REV(float[] floats) {
    IntBuffer intBuffer = BufferUtils.createIntBuffer(floats.length / 3);
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
    IntBuffer intBuffer = BufferUtils.createIntBuffer(floats.length / 4);
    for (int i = 0; i < floats.length / 4; i++) {
      intBuffer.put(convert4fTo1i(floats[i * 4], floats[i * 4 + 1], floats[i * 4 + 2], floats[i * 4 + 3]));
    }
    intBuffer.flip();
    return intBuffer;
  }

  public static ByteBuffer store1fAs1b(float[] floats) {
    ByteBuffer byteBuffer = BufferUtils.createByteBuffer(floats.length);
    for (float floatValue : floats) {
      byteBuffer.put((byte) convertFloatToByte(floatValue));
    }
    byteBuffer.flip();
    return byteBuffer;
  }

  public static int convert4fTo1i(float f1, float f2, float f3, float f4) {
    int compressedInt =             ((int) (f4 * 255) << 24);
    compressedInt = compressedInt | ((int) (f3 * 255) << 16);
    compressedInt = compressedInt | ((int) (f2 * 255) << 8);
    compressedInt = compressedInt | ((int) (f1 * 255));
    return compressedInt;
  }

  public static int convertFloatToByte(float floatValue) {
    return (int) (floatValue * 255);
  }

  public static FloatBuffer store(float[] floats) {
    FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(floats.length);
    floatBuffer.put(floats);
    floatBuffer.flip();
    return floatBuffer;
  }

  public static IntBuffer store(int[] ints) {
    IntBuffer intBuffer = BufferUtils.createIntBuffer(ints.length);
    intBuffer.put(ints);
    intBuffer.flip();
    return intBuffer;
  }

  public static ShortBuffer store(short[] shorts) {
    ShortBuffer shortBuffer = BufferUtils.createShortBuffer(shorts.length);
    shortBuffer.put(shorts);
    shortBuffer.flip();
    return shortBuffer;
  }

  public static ByteBuffer store(byte[] bytes) {
    ByteBuffer byteBuffer = BufferUtils.createByteBuffer(bytes.length);
    byteBuffer.put(bytes);
    byteBuffer.flip();
    return byteBuffer;
  }

}
