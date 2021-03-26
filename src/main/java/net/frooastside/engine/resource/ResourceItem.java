package net.frooastside.engine.resource;

import java.io.Externalizable;

public interface ResourceItem extends Externalizable, Loadable {

  float progress();

}
