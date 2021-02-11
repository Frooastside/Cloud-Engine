package net.frooastside.engine.userinterface.elements.basic;

import net.frooastside.engine.glfw.callbacks.KeyCallback;
import net.frooastside.engine.resource.ResourceFont;
import net.frooastside.engine.userinterface.ElementConstraints;
import net.frooastside.engine.userinterface.SelectionEvent;
import net.frooastside.engine.userinterface.UiColorSet;
import net.frooastside.engine.userinterface.constraints.RawConstraint;
import net.frooastside.engine.userinterface.constraints.RelativeConstraint;
import net.frooastside.engine.userinterface.elements.UiBasicElement;
import net.frooastside.engine.userinterface.elements.UiRenderElement;
import net.frooastside.engine.userinterface.elements.render.UiBox;
import net.frooastside.engine.userinterface.elements.render.UiText;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class UiTextField extends UiBasicElement implements SelectionEvent.Listener {

  private final UiRenderElement[] renderElements = new UiRenderElement[2];

  private final UiColorSet colorSet;
  private final ResourceFont font;
  private final float textSize;
  private final boolean constantTextSize;

  private String text;
  private int cursor = 0;
  private int selectionStart = 0;
  private int selectionEnd = 0;

  public UiTextField(UiColorSet colorSet, ResourceFont font, String text, float textSize, boolean constantTextSize) {
    this.colorSet = colorSet;
    this.font = font;
    this.text = text;
    this.cursorStart = text.length();
    this.cursorEnd = text.length();
    this.textSize = textSize;
    this.constantTextSize = constantTextSize;
  }

  @Override
  public void onKeyEvent(int key, int scancode, int modifiers, KeyCallback.Action action) {
    System.out.println("onKeyEvent");
    if(action != KeyCallback.Action.RELEASE) {
      System.out.println("!RELEASE");
      if(key == GLFW.GLFW_KEY_BACKSPACE) {
        System.out.println("GLFW_KEY_BACKSLASH");
        if(selectionStart == selectionEnd) {
          System.out.println("==");
          if(cursor > 0 && text.length() >= cursor) {
            System.out.println("> 0 && length >= cursorEnd");
            text = text.substring(0, cursor - 1) + text.substring(cursor);
            reloadText();
            cursor--;
          }
        }
      }else if(key == GLFW.GLFW_KEY_DELETE) {
        System.out.println("GLFW_KEY_DELETE");
        if(selectionStart == selectionEnd) {
          System.out.println("==");
          if(cursor < text.length()) {
            System.out.println("cursorStart < length");
            text = text.substring(0, cursor) + text.substring(cursor + 1);
            reloadText();
          }
        }else {
          text = textWithoutSelection();
          if(cursor == selectionEnd) {
            cursor = selectionStart;
          }
          reloadText();
          resetSelection();
        }
      }else if(key == GLFW.GLFW_KEY_LEFT) {
        if(KeyCallback.Modifier.checkModifier(modifiers, KeyCallback.Modifier.SHIFT)) {
          if(cursor == selectionStart) {
            if(cursor > 0 && text.length() >= cursor) {
              cursor--;
              resetSelection();
              reloadText();
            }
          }else {

          }
        }else {

        }
      }else if(key == GLFW.GLFW_KEY_RIGHT) {
        if(KeyCallback.Modifier.checkModifier(modifiers, KeyCallback.Modifier.SHIFT)) {

        }else {

        }
      }
    }
  }

  @Override
  public void onCharEvent(int codepoint) {
    if(selectionStart == selectionEnd) {
      this.text = this.text + (char) codepoint;
      this.cursor++;
      this.selectionStart = cursor;
      this.selectionEnd = cursor;
    }else {
      this.text = textWithoutSelection();
    }
    reloadText();
  }

  public void reloadText() {
    ((UiText) renderElements[1]).setText(this.text);
  }

  private void resetSelection() {
    selectionStart = cursor;
    selectionEnd = cursor;
  }

  @Override
  public void onSelection(SelectionEvent event) {

  }

  @Override
  public void recalculate(Vector2f pixelSize) {
    super.recalculate(pixelSize);
    for(UiRenderElement renderElement : renderElements) {
      if (renderElement != null) {
        renderElement.recalculate(pixelSize);
      }
    }
  }

  @Override
  public void initialize() {
    ElementConstraints backgroundConstraints = ElementConstraints.getDefault();
    backgroundConstraints.setParent(constraints());
    UiBox background = new UiBox(colorSet.element());
    background.setConstraints(backgroundConstraints);
    renderElements[0] = background;

    ElementConstraints textConstraints = new ElementConstraints();
    textConstraints.setParent(constraints());
    textConstraints.setX(new RelativeConstraint(0));
    textConstraints.setY(new RelativeConstraint(0.5f));
    textConstraints.setWidth(new RelativeConstraint(1));
    textConstraints.setHeight(constantTextSize ? new RawConstraint(textSize) : new RelativeConstraint(textSize));
    UiText text = new UiText(font, this.text, colorSet.text(), false);
    text.setConstraints(textConstraints);
    renderElements[1] = text;
  }

  @Override
  public UiRenderElement[] renderElements() {
    return renderElements;
  }

  private String textWithoutSelection() {
    return text.substring(0, selectionStart) + text.substring(selectionEnd);
  }

  private String selection() {
    return text.substring(selectionStart, selectionEnd);
  }

  public void setText(String text) {
    this.text = text;
    recalculate(pixelSize());
  }
}
