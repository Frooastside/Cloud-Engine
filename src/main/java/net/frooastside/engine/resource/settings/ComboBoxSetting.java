package net.frooastside.engine.resource.settings;

import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;

public class ComboBoxSetting<T> extends Setting {

  private final List<T> values;
  private final T defaultValue;

  public ComboBoxSetting(String name, List<T> values, T defaultValue) {
    super(name);
    this.values = values;
    this.defaultValue = defaultValue;
  }

  @Override
  public Node create() {
    ComboBox<T> comboBox = new ComboBox<>();
    comboBox.getItems().addAll(values);
    if (values.contains(defaultValue)) {
      comboBox.setValue(defaultValue);
    }
    return comboBox;
  }
}
