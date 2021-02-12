package net.frooastside.engine.resource.settings;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

public class SettingsCreator {

  private Map<String, Setting> layout;

  public SettingsCreator(Map<String, Setting> settings) {
    this.layout = settings;
  }

  public Map<String, Node> createSettings() {
    Map<String, Node> settings = new HashMap<>();
    for (Map.Entry<String, Setting> layout : this.layout.entrySet()) {
      settings.put(layout.getKey(), layout.getValue().create());
    }
    return settings;
  }

  public static Node getBox(Map<String, Node> settings) {
    VBox vBox = new VBox();
    for (Map.Entry<String, Node> entry : settings.entrySet()) {
      vBox.getChildren().addAll(new Label(entry.getKey()), entry.getValue());
    }
    return vBox;
  }

  public static SettingsCreator createLayout(Setting... settings) {
    Map<String, Setting> settingsMap = new HashMap<>();
    for (Setting setting : settings) {
      settingsMap.put(setting.name(), setting);
    }
    return new SettingsCreator(settingsMap);
  }
}
