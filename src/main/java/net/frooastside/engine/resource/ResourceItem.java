package net.frooastside.engine.resource;

import javafx.scene.Node;

import java.io.Externalizable;

public interface ResourceItem extends Externalizable {

  Runnable contextSpecificLoader();

  Runnable unspecificLoader();

  Node settingsBox();

  Node informationBox();

  void recalculate();
}
