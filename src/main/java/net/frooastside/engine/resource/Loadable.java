package net.frooastside.engine.resource;

import java.util.Queue;
import java.util.concurrent.ExecutorService;

public interface Loadable {

  void loadContextSpecific();

  void loadUnspecific(ExecutorService executorService);

  void addQueueTasks(ExecutorService executorService, Queue<Runnable> contextSpecificQueue);

}
