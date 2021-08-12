package net.frooastside.engine.resource.settings;

import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class IntegerSpinnerSetting extends Setting {

  private final int minValue;
  private final int maxValue;
  private final int defaultValue;

  public IntegerSpinnerSetting(String name, int minValue, int maxValue, int defaultValue) {
    super(name);
    this.minValue = minValue;
    this.maxValue = maxValue;
    this.defaultValue = defaultValue;
  }

  @Override
  public Node create() {
    Spinner<Integer> integerSpinner = new Spinner<>();
    SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory
      .IntegerSpinnerValueFactory(minValue, maxValue, defaultValue);
    integerSpinner.setValueFactory(valueFactory);
    return integerSpinner;
  }
}
