package love.polardivision.engine.userinterface;

public record ColorSet(Color background, Color baseElement, Color element, Color text, Color accent,
                       Color negativeAccent) {

  public static final ColorSet DARK_MODE = new ColorSet(
    new Color(0.2f, 0.2f, 0.2f),
    new Color(0.24f, 0.24f, 0.24f),
    new Color(0.28f, 0.28f, 0.28f),
    new Color(1.0f, 1.0f, 1.0f),
    new Color(0.1215f, 0.5529f, 0.1215f),
    new Color(0.7f, 0.02f, 0.02f));

}
