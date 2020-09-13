package net.frooastside.engine.resource;

import org.lwjgl.BufferUtils;

import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class ResourceItem implements Externalizable {

  protected ByteBuffer rawFile;

  public abstract Runnable getThreadSpecificLoader();

  public abstract Runnable getThreadUnspecificLoader();

  public static ByteBuffer readFile(File file) throws IOException {
    Path path = file.toPath();
    if (Files.isReadable(path)) {
      try (SeekableByteChannel seekableByteChannel = Files.newByteChannel(path)) {
        ByteBuffer buffer = BufferUtils.createByteBuffer((int) seekableByteChannel.size());
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

  public static ByteBuffer copyDirect(ByteBuffer originalByteBuffer) {
    byte[] bytes = new byte[originalByteBuffer.remaining()];
    originalByteBuffer.asReadOnlyBuffer().get(bytes);
    return wrapDirect(bytes);
  }
}
