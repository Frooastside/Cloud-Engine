package net.frooastside.engine.resource.settings;

import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class DoubleSpinnerSetting extends Setting {

  private final double minValue;
  private final double maxValue;
  private final double defaultValue;

  public DoubleSpinnerSetting(String name, double minValue, double maxValue, double defaultValue) {
    super(name);
    this.minValue = minValue;
    this.maxValue = maxValue;
    this.defaultValue = defaultValue;
  }

  @Override
  public Node create() {
    Spinner<Double> doubleSpinner = new Spinner<>();
    SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory
      .DoubleSpinnerValueFactory(minValue, maxValue, defaultValue);
    doubleSpinner.setValueFactory(valueFactory);
    return doubleSpinner;
  }
}
