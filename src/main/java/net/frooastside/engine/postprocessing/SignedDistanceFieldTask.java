package net.frooastside.engine.postprocessing;

import net.frooastside.engine.resource.BufferUtils;
import org.joml.Vector2f;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SignedDistanceFieldTask {

  private static final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

  private final ByteBuffer sourceBuffer;
  private final int imageSize;
  private final int downscale;
  private final float spread;
  private final int length;

  private final AtomicInteger progress = new AtomicInteger();
  private final ByteBuffer distanceFieldBuffer;

  public SignedDistanceFieldTask(ByteBuffer sourceBuffer, int imageSize, int downscale, float spread) {
    this.sourceBuffer = sourceBuffer;
    this.imageSize = imageSize;
    this.downscale = downscale;
    this.spread = spread;
    this.length = (int) Math.pow((float) imageSize / (float) downscale, 2);
    this.distanceFieldBuffer = ByteBuffer.allocateDirect(length);
  }

  public void generate() {
    byte[] pixelArray = BufferUtils.copyToArray(sourceBuffer);
    boolean[][] bitmap = new boolean[imageSize][imageSize];
    for (int y = 0; y < imageSize; y++) {
      for (int x = 0; x < imageSize; x++) {
        byte rgb = pixelArray[y * imageSize + x];
        bitmap[x][y] = rgb < 0;
      }
    }
    int downscaledImageSize = imageSize / downscale;
    for (int y = 0; y < downscaledImageSize; y++) {
      for (int x = 0; x < downscaledImageSize; x++) {
        final int centerX = x * downscale + downscale / 2;
        final int centerY = y * downscale + downscale / 2;
        final boolean base = bitmap[centerX][centerY];
        final int delta = (int) Math.ceil(spread);
        final int startX = Math.max(0, centerX - delta);
        final int startY = Math.max(0, centerY - delta);
        final int endX = Math.min(imageSize - 1, centerX + delta);
        final int endY = Math.min(imageSize - 1, centerY + delta);
        int finalY = y;
        int finalX = x;
        executorService.submit(() -> {
          int closestSquareDistance = delta * delta;
          for (int j = startY; j < endY; j++) {
            for (int i = startX; i < endX; i++) {
              if (base != bitmap[i][j]) {
                int squareDistance = (int) Vector2f.distanceSquared(centerX, centerY, i, j);
                if (squareDistance < closestSquareDistance) {
                  closestSquareDistance = squareDistance;
                }
              }
            }
          }
          float closestDistance = (float) Math.sqrt(closestSquareDistance);
          float distance = (base ? 1 : -1) * Math.min(closestDistance, spread);
          float alpha = 0.5f + 0.5f * distance / spread;
          int alphaByte = (int) (Math.min(1.0f, Math.max(0.0f, alpha)) * 255.0f);
          synchronized (distanceFieldBuffer) {
            distanceFieldBuffer.put(finalY * downscaledImageSize + finalX, (byte) alphaByte);
          }
          synchronized (progress) {
            progress.incrementAndGet();
          }
        });
      }
    }
  }

  public void waitForCompletion() {
    try {
      executorService.shutdown();
      executorService.awaitTermination(1, TimeUnit.HOURS);
    } catch (InterruptedException ignored) {
    }
  }

  public int length() {
    return length;
  }

  public synchronized float progress() {
    return ((float) ((1.0 / length) * progress.get()));
  }

  public synchronized boolean finished() {
    return progress.get() == length;
  }

  public ByteBuffer distanceFieldBuffer() {
    return finished() ? distanceFieldBuffer : null;
  }
}
