package test;

import net.frooastside.engine.Window;
import net.frooastside.engine.resource.ResourceFont;
import net.frooastside.engine.userinterface.ElementConstraints;
import net.frooastside.engine.userinterface.UiColor;
import net.frooastside.engine.userinterface.constraints.RelativeConstraint;
import net.frooastside.engine.userinterface.elements.UiBox;
import net.frooastside.engine.userinterface.elements.UiText;
import net.frooastside.engine.userinterface.UiScreen;

public class UiScreenTest extends UiScreen {

  public UiScreenTest(Window window, ResourceFont font) {
    super(window, font);
  }

  @Override
  public void initialize() {
    ElementConstraints bigTextConstraints = new ElementConstraints();
    bigTextConstraints.setX(new RelativeConstraint(0.0f));
    bigTextConstraints.setY(new RelativeConstraint(0.0f));
    bigTextConstraints.setWidth(new RelativeConstraint(10));
    bigTextConstraints.setHeight(new RelativeConstraint(10));
    addElement(new UiText(font(), Main.TEXT, UiColor.ACCENT, false), bigTextConstraints);

    ElementConstraints constraints = new ElementConstraints();
    constraints.setX(new RelativeConstraint(0.0f));
    constraints.setY(new RelativeConstraint(0.5f));
    constraints.setWidth(new RelativeConstraint(1));
    constraints.setHeight(new RelativeConstraint(1.2f));
    addElement(new UiText(font(), "Example Text", UiColor.WHITE, true), constraints);

    ElementConstraints boxConstraints = new ElementConstraints();
    boxConstraints.setX(new RelativeConstraint(0.0f));
    boxConstraints.setY(new RelativeConstraint(0.0f));
    boxConstraints.setWidth(new RelativeConstraint(1.0f));
    boxConstraints.setHeight(new RelativeConstraint(0.25f));
    addElement(new UiBox(UiColor.BACKGROUND), boxConstraints);

    ElementConstraints boxConstraints2 = new ElementConstraints();
    boxConstraints2.setX(new RelativeConstraint(0.25f));
    boxConstraints2.setY(new RelativeConstraint(0.25f));
    boxConstraints2.setWidth(new RelativeConstraint(0.5f));
    boxConstraints2.setHeight(new RelativeConstraint(0.5f));
    addElement(new UiBox(UiColor.ELEMENT), boxConstraints2);
  }
}
