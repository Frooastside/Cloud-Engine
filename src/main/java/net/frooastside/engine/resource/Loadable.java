package net.frooastside.engine.resource;

import java.util.Queue;
import java.util.concurrent.ExecutorService;

public interface Loadable {

  default void loadContextSpecific() {}

  default void loadUnspecific(ExecutorService executorService) {}

  default void addQueueTasks(ExecutorService executorService, Queue<Runnable> contextSpecificQueue) {
    executorService.submit(() -> {
      loadUnspecific(executorService);
      contextSpecificQueue.offer(this::loadContextSpecific);
    });
  }

}
