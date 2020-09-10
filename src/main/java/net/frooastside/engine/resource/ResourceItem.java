package net.frooastside.engine.resource;

import java.io.Externalizable;

public abstract class ResourceItem implements Externalizable {

  public abstract Runnable getThreadSpecificLoader();

}
