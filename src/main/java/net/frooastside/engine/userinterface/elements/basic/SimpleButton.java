package net.frooastside.engine.userinterface.elements.basic;

import net.frooastside.engine.graphicobjects.Font;
import net.frooastside.engine.userinterface.Color;
import net.frooastside.engine.userinterface.constraints.ElementConstraints;
import net.frooastside.engine.userinterface.constraints.RawConstraint;
import net.frooastside.engine.userinterface.constraints.RelativeConstraint;
import net.frooastside.engine.userinterface.elements.render.Box;
import net.frooastside.engine.userinterface.elements.render.Text;
import net.frooastside.engine.userinterface.events.ClickEvent;

public class SimpleButton extends Button {

  private final Color backgroundColor;
  private final Color textColor;
  private final Font font;
  private final boolean constantTextSize;
  private final float textSize;

  private String text;

  public SimpleButton(Color backgroundColor, Font font, Color textColor, String text, boolean constantTextSize, float textSize) {
    this.backgroundColor = backgroundColor;
    this.textColor = textColor;
    this.font = font;
    this.text = text;
    this.textSize = textSize;
    this.constantTextSize = constantTextSize;
  }

  @Override
  public void initialize() {
    Box background = new Box(backgroundColor);
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

  }

  public void setText(String text) {
    this.text = text;
    recalculateBounds();
  }
}
