package net.frooastside.engine.resource.settings;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

import java.util.Map;

public abstract class Setting {

  private final String name;

  public Setting(String name) {
    this.name = name;
  }

  public abstract Node create();

  public static Object getComboBoxItem(Map<String, Node> settings, String key) {
    if (settings.get(key) instanceof ComboBox<?>) {
      ComboBox<?> comboBox = (ComboBox<?>) settings.get(key);
      return comboBox.getSelectionModel().getSelectedItem();
    } else {
      System.err.println("Nicht ComboBox");
    }
    return null;
  }

  public static <T> void setComboBoxItem(Map<String, Node> settings, String key, T item) {
    if (settings.get(key) instanceof ComboBox<?>) {
      ComboBox<T> comboBox = (ComboBox<T>) settings.get(key);
      comboBox.setValue(item);
    } else {
      System.err.println("Nicht ComboBox");
    }
  }

  public static boolean getCheckBoxBoolean(Map<String, Node> settings, String key) {
    if (settings.get(key) instanceof CheckBox) {
      CheckBox checkBox = (CheckBox) settings.get(key);
      return checkBox.isSelected();
    } else {
      System.err.println("Nicht CheckBox");
    }
    return false;
  }

  public static void setCheckBoxBoolean(Map<String, Node> settings, String key, boolean value) {
    if (settings.get(key) instanceof CheckBox) {
      CheckBox checkBox = (CheckBox) settings.get(key);
      checkBox.setSelected(value);
    } else {
      System.err.println("Nicht CheckBox");
    }
  }

  public static int getSpinnerInteger(Map<String, Node> settings, String key) {
    if (settings.get(key) instanceof Spinner<?>) {
      Spinner<?> spinner = (Spinner<?>) settings.get(key);
      if (spinner.getValue() instanceof Integer) {
        return (Integer) spinner.getValue();
      } else {
        System.err.println("Nicht Integer");
      }
    } else {
      System.err.println("Nicht Spinner");
    }
    return -1;
  }

  public static double getSpinnerDouble(Map<String, Node> settings, String key) {
    if (settings.get(key) instanceof Spinner<?>) {
      Spinner<?> spinner = (Spinner<?>) settings.get(key);
      if (spinner.getValue() instanceof Double) {
        return (Double) spinner.getValue();
      } else {
        System.err.println("Nicht Double");
      }
    } else {
      System.err.println("Nicht Spinner");
    }
    return -1;
  }

  public static <T> void setSpinnerValue(Map<String, Node> settings, String key, T value) {
    if (settings.containsKey(key) && settings.get(key) instanceof Spinner) {
      Spinner<T> spinner = (Spinner<T>) settings.get(key);
      spinner.getValueFactory().setValue(value);
    } else {
      System.err.println("Nicht Spinner");
    }
  }

  public static int getTextFieldInteger(Map<String, Node> settings, String key) {
    if (settings.get(key) instanceof TextField) {
      TextField textField = (TextField) settings.get(key);
      return (Integer) Integer.parseInt(textField.getText());
    } else {
      System.err.println("Nicht TextField");
    }
    return -1;
  }

  public static <T> void setTextFieldInteger(Map<String, Node> settings, String key, int value) {
    if (settings.containsKey(key) && settings.get(key) instanceof Spinner) {
      TextField textField = (TextField) settings.get(key);
      textField.setText("" + value);
    } else {
      System.err.println("Nicht TextField");
    }
  }

  public String name() {
    return name;
  }
}
