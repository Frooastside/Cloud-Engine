package net.frooastside.engine.resource.settings;

import javafx.scene.Node;
import javafx.scene.control.TextField;

public class IntegerTextField extends Setting {

  private final int defaultValue;

  public IntegerTextField(String name, int defaultValue) {
    super(name);
    this.defaultValue = defaultValue;
  }

  @Override
  public Node create() {
    TextField textField = new TextField();
    textField.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue.matches("\\d*")) {
        textField.setText(newValue.replaceAll("[^\\d]", ""));
      }
    });
    textField.setText("" + defaultValue);
    return textField;
  }
}
