package net.frooastside.engine.userinterface.elements.basic;

import net.frooastside.engine.glfw.Window;
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
    this.selectionStart = text.length();
    this.selectionEnd = text.length();
    this.cursor = text.length();
    this.textSize = textSize;
    this.constantTextSize = constantTextSize;
  }

  @Override
  public void recalculate(Vector2f pixelSize) {
    super.recalculate(pixelSize);
    for (UiRenderElement renderElement : renderElements) {
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
  public void onKeyEvent(Window window, int key, int scancode, int modifiers, KeyCallback.Action action) {
    if (action != KeyCallback.Action.RELEASE) {
      if (key == GLFW.GLFW_KEY_BACKSPACE) {
        if (selectionStart == selectionEnd) {
          if (cursor > 0 && text.length() >= cursor) {
            text = text.substring(0, cursor - 1) + text.substring(cursor);
            reloadText();
            cursor--;
          }
        } else {
          text = textWithoutSelection();
          if (cursor == selectionEnd) {
            cursor = selectionStart;
          }
          reloadText();
          resetSelection();
        }
      } else if (key == GLFW.GLFW_KEY_DELETE) {
        if (selectionStart == selectionEnd) {
          if (cursor < text.length()) {
            text = text.substring(0, cursor) + text.substring(cursor + 1);
            reloadText();
          }
        } else {
          text = textWithoutSelection();
          if (cursor == selectionEnd) {
            cursor = selectionStart;
          }
          reloadText();
          resetSelection();
        }
      } else if (key == GLFW.GLFW_KEY_LEFT) {
        if (KeyCallback.Modifier.checkModifier(modifiers, KeyCallback.Modifier.SHIFT)) {
          if (cursor == selectionStart) {
            if (cursor > 0 && text.length() >= cursor) {
              selectionStart--;
              cursor = selectionStart;
              reloadText();
            }
          } else if (cursor == selectionEnd) {
            if (cursor > selectionStart) {
              selectionEnd--;
              cursor = selectionEnd;
              reloadText();
            }
          }
        } else {
          if (selectionStart == selectionEnd) {
            if (cursor > 0) {
              cursor--;
            }
          }
          resetSelection();
          reloadText();
        }
      } else if (key == GLFW.GLFW_KEY_RIGHT) {
        if (KeyCallback.Modifier.checkModifier(modifiers, KeyCallback.Modifier.SHIFT)) {
          if (cursor == selectionEnd) {
            if (cursor < text.length()) {
              selectionEnd++;
              cursor = selectionEnd;
              reloadText();
            }
          } else if (cursor == selectionStart) {
            if (selectionStart < selectionEnd) {
              selectionStart++;
              cursor = selectionStart;
              reloadText();
            }
          }
        } else {
          if (selectionStart == selectionEnd) {
            if (cursor < text.length()) {
              cursor++;
            }
          }
          resetSelection();
          reloadText();
        }
      }else if(key == GLFW.GLFW_KEY_A && KeyCallback.Modifier.checkModifier(modifiers, KeyCallback.Modifier.CONTROL)) {
        if(selectionStart == 0 && selectionEnd == text.length()) {
          cursor = text.length();
          resetSelection();
        }else {
          selectionStart = 0;
          selectionEnd = text.length();
          cursor = selectionEnd;
        }
        reloadText();
      }else if(key == GLFW.GLFW_KEY_C && KeyCallback.Modifier.checkModifier(modifiers, KeyCallback.Modifier.CONTROL)) {
        if (selectionStart != selectionEnd) {
          window.input().setClipboard(selection());
        }
      }else if(key == GLFW.GLFW_KEY_V && KeyCallback.Modifier.checkModifier(modifiers, KeyCallback.Modifier.CONTROL)) {
        if (selectionStart != selectionEnd) {
          this.text = textWithoutSelection();
          cursor = selectionStart;
        }
        String clipboard = window.input().getClipboard();
        this.text = textBeforeCursor() + clipboard + textAfterCursor();
        this.cursor += clipboard.length();
        resetSelection();
        reloadText();
      }else if(key == GLFW.GLFW_KEY_X && KeyCallback.Modifier.checkModifier(modifiers, KeyCallback.Modifier.CONTROL)) {
        if (selectionStart != selectionEnd) {
          window.input().setClipboard(selection());
          this.text = textWithoutSelection();
          cursor = selectionStart;
        }
        resetSelection();
        reloadText();
      }
    }
  }

  @Override
  public void onCharEvent(Window window, int codepoint) {
    if (selectionStart != selectionEnd) {
      this.text = textWithoutSelection();
      cursor = selectionStart;
    }
    this.text = textBeforeCursor() + (char) codepoint + textAfterCursor();
    this.cursor++;
    resetSelection();
    reloadText();
  }

  @Override
  public void onSelection(SelectionEvent event) {

  }

  public void reloadText() {
    ((UiText) renderElements[1]).setText(this.text);
  }

  private void resetSelection() {
    selectionStart = cursor;
    selectionEnd = cursor;
  }

  private void deleteSelection() {
    text = textWithoutSelection();
    if (cursor == selectionEnd) {
      cursor = selectionStart;
    }
    reloadText();
    resetSelection();
  }

  @Override
  public UiRenderElement[] renderElements() {
    return renderElements;
  }

  private String textBeforeCursor() {
    return text.substring(0, cursor);
  }

  private String textAfterCursor() {
    return text.substring(cursor);
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
