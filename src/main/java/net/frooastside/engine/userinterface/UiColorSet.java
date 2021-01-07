package net.frooastside.engine.userinterface;

public class UiColorSet {

  public static final UiColorSet DARK_MODE = new UiColorSet(
    new UiColor(0.2f, 0.2f, 0.2f),
    new UiColor(0.24f, 0.24f, 0.24f),
    new UiColor(0.28f, 0.28f, 0.28f),
    new UiColor(1.0f, 1.0f, 1.0f),
    new UiColor(0.1215f, 0.5529f, 0.1215f));

  private final UiColor background;
  private final UiColor baseElement;
  private final UiColor element;
  private final UiColor text;
  private final UiColor accent;

  public UiColorSet(UiColor background, UiColor baseElement, UiColor element, UiColor text, UiColor accent) {
    this.background = background;
    this.baseElement = baseElement;
    this.element = element;
    this.text = text;
    this.accent = accent;
  }

  public UiColor background() {
    return background;
  }

  public UiColor baseElement() {
    return baseElement;
  }

  public UiColor element() {
    return element;
  }

  public UiColor text() {
    return text;
  }

  public UiColor accent() {
    return accent;
  }
}
