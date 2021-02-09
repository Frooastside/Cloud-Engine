package test;

import net.frooastside.engine.glfw.Window;
import net.frooastside.engine.resource.ResourceFont;
import net.frooastside.engine.userinterface.ElementConstraints;
import net.frooastside.engine.userinterface.UiColorSet;
import net.frooastside.engine.userinterface.constraints.CenterConstraint;
import net.frooastside.engine.userinterface.constraints.PixelConstraint;
import net.frooastside.engine.userinterface.constraints.RelativeConstraint;
import net.frooastside.engine.userinterface.elements.render.UiBox;
import net.frooastside.engine.userinterface.UiScreen;
import net.frooastside.engine.userinterface.elements.render.UiText;
import net.frooastside.engine.userinterface.elements.render.UiTextArea;

public class UiScreenTest extends UiScreen {

  public UiScreenTest(Window window, ResourceFont font) {
    super(window, font, UiColorSet.DARK_MODE);
  }

  @Override
  public void initialize() {
    ElementConstraints titleConstraints = new ElementConstraints(
      new RelativeConstraint(0.0f),
      new RelativeConstraint(0.04f),
      new RelativeConstraint(1),
      new RelativeConstraint(3));
    addElement(new UiText(font(), "Überschrift", colorSet().text(), true), titleConstraints);

    ElementConstraints backgroundConstraints = new ElementConstraints(
      new PixelConstraint(40),
      new PixelConstraint(40),
      new CenterConstraint(),
      new CenterConstraint());
    UiBox background = new UiBox(colorSet().background());
    addElement(background, backgroundConstraints);

    ElementConstraints buttonConstraints = new ElementConstraints(
      new CenterConstraint(),
      new CenterConstraint(),
      new RelativeConstraint(0.5f),
      new RelativeConstraint(0.125f));
    UiBox button = new UiBox(colorSet().element());
    background.addElement(button, buttonConstraints);

    ElementConstraints labelConstraints = new ElementConstraints(
      new RelativeConstraint(0.0f),
      new RelativeConstraint(0.5f),
      new RelativeConstraint(1),
      new RelativeConstraint(30));
    button.addElement(new UiText(font(), "Klick mich", colorSet().text(), true), labelConstraints);

    ElementConstraints areaConstraints = new ElementConstraints(
      new RelativeConstraint(0.0f),
      new RelativeConstraint(0.0f),
      new RelativeConstraint(0.6f),
      new RelativeConstraint(1.7f));
    background.addElement(new UiTextArea(font(), Main.TEXT, colorSet().text()), areaConstraints);
  }
}
