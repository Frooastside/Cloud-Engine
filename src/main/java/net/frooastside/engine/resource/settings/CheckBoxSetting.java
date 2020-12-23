package net.frooastside.engine.resource.settings;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;

public class CheckBoxSetting extends Setting {

  private final boolean defaultValue;

  public CheckBoxSetting(String name, boolean defaultValue) {
    super(name);
    this.defaultValue = defaultValue;
  }

  @Override
  public Node create() {
    CheckBox checkBox = new CheckBox();
    checkBox.setSelected(defaultValue);
    return checkBox;
  }
}
