package test;

import net.frooastside.engine.Window;
import net.frooastside.engine.resource.ResourceFont;
import net.frooastside.engine.userinterface.ElementConstraints;
import net.frooastside.engine.userinterface.UiColor;
import net.frooastside.engine.userinterface.constraints.CenterConstraint;
import net.frooastside.engine.userinterface.constraints.PixelConstraint;
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

    ElementConstraints boxConstraints = new ElementConstraints();
    boxConstraints.setX(new PixelConstraint(40));
    boxConstraints.setY(new PixelConstraint(40));
    boxConstraints.setWidth(new CenterConstraint());
    boxConstraints.setHeight(new CenterConstraint());
    UiBox boxxx = new UiBox(UiColor.BACKGROUND);
    addElement(boxxx, boxConstraints);

    ElementConstraints boxConstraints2 = new ElementConstraints();
    boxConstraints2.setX(new RelativeConstraint(0.25f));
    boxConstraints2.setY(new RelativeConstraint(0.25f));
    boxConstraints2.setWidth(new RelativeConstraint(0.5f));
    boxConstraints2.setHeight(new RelativeConstraint(0.125f));
    UiBox secondBOX = new UiBox(UiColor.ELEMENT);
    boxxx.addElement(secondBOX, boxConstraints2);

    ElementConstraints bigTextConstraints = new ElementConstraints();
    bigTextConstraints.setX(new RelativeConstraint(0.0f));
    bigTextConstraints.setY(new RelativeConstraint(0.0f));
    bigTextConstraints.setWidth(new RelativeConstraint(10));
    bigTextConstraints.setHeight(new RelativeConstraint(4));
    boxxx.addElement(new UiText(font(), Main.TEXT, UiColor.ACCENT, false), bigTextConstraints);

    ElementConstraints constraints = new ElementConstraints();
    constraints.setX(new RelativeConstraint(0.0f));
    constraints.setY(new RelativeConstraint(0.5f));
    constraints.setWidth(new RelativeConstraint(1.0f));
    constraints.setHeight(new RelativeConstraint(20.0f));
    secondBOX.addElement(new UiText(font(), "HALLO LEUTE !karqwee534", UiColor.WHITE, true), constraints);
  }
}
