package test;

import net.frooastside.engine.Window;
import net.frooastside.engine.resource.ResourceFont;
import net.frooastside.engine.userinterface.ElementConstraints;
import net.frooastside.engine.userinterface.UiColor;
import net.frooastside.engine.userinterface.constraints.RelativeConstraint;
import net.frooastside.engine.userinterface.elements.UiText;
import net.frooastside.engine.userinterface.UiScreen;

public class UiScreenTest extends UiScreen {

  public UiScreenTest(Window window, ResourceFont font) {
    super(window, font);
  }

  @Override
  public void initialize() {
    ElementConstraints constraints = new ElementConstraints();
    constraints.setX(new RelativeConstraint(1.5f));
    constraints.setY(new RelativeConstraint(1.5f));
    constraints.setWidth(new RelativeConstraint(5));
    constraints.setHeight(new RelativeConstraint(5));
    addElement(new UiText(font(), Main.TEXT, UiColor.GREEN, false), constraints);
    recalculate();
  }
}
