package net.frooastside.engine.userinterface.elements.basic;

import net.frooastside.engine.graphicobjects.Font;
import net.frooastside.engine.userinterface.Color;
import net.frooastside.engine.userinterface.constraints.ElementConstraints;
import net.frooastside.engine.userinterface.constraints.RawConstraint;
import net.frooastside.engine.userinterface.constraints.RelativeConstraint;
import net.frooastside.engine.userinterface.elements.render.Box;
import net.frooastside.engine.userinterface.elements.render.Text;
import net.frooastside.engine.userinterface.events.ClickEvent;

public class SimpleToggleButton extends Button {

  private final Color backgroundColor;
  private final Color enabledColor;
  private final Color textColor;
  private final Font font;
  private final boolean constantTextSize;
  private final float textSize;

  private String text;
  private boolean enabled;
  private Box background;

  public SimpleToggleButton(Color backgroundColor, Color enabledColor, Font font, Color textColor, String text, boolean constantTextSize, float textSize) {
    this.backgroundColor = backgroundColor;
    this.enabledColor = enabledColor;
    this.textColor = textColor;
    this.font = font;
    this.text = text;
    this.textSize = textSize;
    this.constantTextSize = constantTextSize;
  }

  @Override
  public void initialize() {
    background = new Box(backgroundColor);
    addElement(background);

    ElementConstraints textConstraints = new ElementConstraints(
      new RelativeConstraint(0),
      new RelativeConstraint(0.5f),
      new RelativeConstraint(1),
      constantTextSize ? new RawConstraint(textSize) : new RelativeConstraint(textSize));
    Text text = new Text(font, this.text, textColor, true);
    addElement(text, textConstraints);
  }

  @Override
  public void handleInternalClick(ClickEvent event) {
    enabled = !enabled;
    background.setColor(enabled ? enabledColor : backgroundColor);
  }

  public void setText(String text) {
    this.text = text;
    recalculateBounds();
  }

  public boolean enabled() {
    return enabled;
  }
}
