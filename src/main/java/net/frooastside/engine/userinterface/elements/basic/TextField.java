package net.frooastside.engine.userinterface.elements.basic;

import net.frooastside.engine.glfw.Window;
import net.frooastside.engine.glfw.callbacks.KeyCallback;
import net.frooastside.engine.resource.ResourceFont;
import net.frooastside.engine.userinterface.constraints.Constraint;
import net.frooastside.engine.userinterface.constraints.ElementConstraints;
import net.frooastside.engine.userinterface.elements.FunctionalElement;
import net.frooastside.engine.userinterface.events.SelectionEvent;
import net.frooastside.engine.userinterface.ColorSet;
import net.frooastside.engine.userinterface.constraints.PixelConstraint;
import net.frooastside.engine.userinterface.constraints.RawConstraint;
import net.frooastside.engine.userinterface.constraints.RelativeConstraint;
import net.frooastside.engine.userinterface.elements.render.Box;
import net.frooastside.engine.userinterface.elements.render.Text;
import org.lwjgl.glfw.GLFW;

public class TextField extends FunctionalElement implements SelectionEvent.Listener {

  private final ColorSet colorSet;
  private final ResourceFont font;

  private Text uiText;
  private Box selectionBox;
  private Box cursorBox;

  private final float textSize;
  private final boolean constantTextSize;

  private String text;
  private int cursor;
  private int selectionStart;
  private int selectionEnd;

  private boolean selected;

  private boolean increment;
  private double cursorAlpha;

  public TextField(ColorSet colorSet, ResourceFont font, String text, float textSize, boolean constantTextSize) {
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
  public void recalculateBounds() {
    updateRenderElements();
    super.recalculateBounds();
  }

  public void updateRenderElements() {
    uiText.setText(this.text);
    float selectionStartX = (float) uiText.lineLength(0, selectionStart);
    float selectionEndX = (float) uiText.lineLength(0, selectionEnd);
    selectionBox.constraints().getConstraint(Constraint.ConstraintType.X).setValue(selectionStartX);
    selectionBox.constraints().getConstraint(Constraint.ConstraintType.WIDTH).setValue(selectionEndX - selectionStartX);
    float cursorX = (float) uiText.lineLength(0, cursor);
    cursorBox.constraints().getConstraint(Constraint.ConstraintType.X).setValue(cursorX - (pixelSize().x * 1));
  }

  @Override
  public void update(double delta) {
    super.update(delta);
    if (selected) {
      if (increment) {
        if (cursorAlpha >= 1) {
          increment = false;
        }
        cursorAlpha += delta * 3;
      } else {
        if (cursorAlpha <= 0) {
          increment = true;
        }
        cursorAlpha -= delta * 3;
      }
      cursorBox.setAlpha((float) cursorAlpha);
    }
  }

  @Override
  public void initialize() {
    ElementConstraints backgroundConstraints = ElementConstraints.getDefault();
    Box background = new Box(colorSet.element());
    addElement(background, backgroundConstraints);

    ElementConstraints textConstraints = new ElementConstraints(
      new RelativeConstraint(0),
      new RelativeConstraint(0.5f),
      new RelativeConstraint(1),
      constantTextSize ? new RawConstraint(textSize) : new RelativeConstraint(textSize));
    uiText = new Text(font, this.text, colorSet.text(), false);
    addElement(uiText, textConstraints);

    ElementConstraints selectionBoxConstraints = new ElementConstraints(
      new RawConstraint(0),
      new RelativeConstraint(0.1f),
      new RawConstraint(0),
      new RelativeConstraint(0.8f));
    selectionBox = new Box(colorSet.accent());
    selectionBox.setAlpha(0.2f);
    addElement(selectionBox, selectionBoxConstraints);

    ElementConstraints cursorBoxConstraints = new ElementConstraints(
      new RawConstraint(0),
      new RelativeConstraint(0.1f),
      new PixelConstraint(2),
      new RelativeConstraint(0.8f));
    cursorBox = new Box(colorSet.accent());
    cursorBox.setAlpha(0.0f);
    addElement(cursorBox, cursorBoxConstraints);
  }

  @Override
  public void handleKeyEvent(Window window, int key, int scancode, int modifiers, KeyCallback.Action action) {
    if (action != KeyCallback.Action.RELEASE) {
      if (key == GLFW.GLFW_KEY_BACKSPACE) {
        if (selectionStart == selectionEnd) {
          if (cursor > 0 && text.length() >= cursor) {
            text = text.substring(0, cursor - 1) + text.substring(cursor);
            cursor--;
            resetSelection();
            recalculateBounds();
          }
        } else {
          deleteSelection();
          resetSelection();
          recalculateBounds();
        }
      } else if (key == GLFW.GLFW_KEY_DELETE) {
        if (selectionStart == selectionEnd) {
          if (cursor < text.length()) {
            text = text.substring(0, cursor) + text.substring(cursor + 1);
            resetSelection();
            recalculateBounds();
          }
        } else {
          deleteSelection();
          resetSelection();
          recalculateBounds();
        }
      } else if (key == GLFW.GLFW_KEY_LEFT) {
        if (KeyCallback.Modifier.checkModifier(modifiers, KeyCallback.Modifier.SHIFT)) {
          if (cursor == selectionStart) {
            if (cursor > 0 && text.length() >= cursor) {
              selectionStart--;
              cursor = selectionStart;
              recalculateBounds();
            }
          } else if (cursor == selectionEnd) {
            if (cursor > selectionStart) {
              selectionEnd--;
              cursor = selectionEnd;
              recalculateBounds();
            }
          }
        } else {
          if (selectionStart == selectionEnd) {
            if (cursor > 0) {
              cursor--;
            }
          }
          resetSelection();
          recalculateBounds();
        }
      } else if (key == GLFW.GLFW_KEY_RIGHT) {
        if (KeyCallback.Modifier.checkModifier(modifiers, KeyCallback.Modifier.SHIFT)) {
          if (cursor == selectionEnd) {
            if (cursor < text.length()) {
              selectionEnd++;
              cursor = selectionEnd;
              recalculateBounds();
            }
          } else if (cursor == selectionStart) {
            if (selectionStart < selectionEnd) {
              selectionStart++;
              cursor = selectionStart;
              recalculateBounds();
            }
          }
        } else {
          if (selectionStart == selectionEnd) {
            if (cursor < text.length()) {
              cursor++;
            }
          }
          resetSelection();
          recalculateBounds();
        }
      } else if (key == GLFW.GLFW_KEY_A && KeyCallback.Modifier.checkModifier(modifiers, KeyCallback.Modifier.CONTROL)) {
        if (selectionStart == 0 && selectionEnd == text.length()) {
          cursor = text.length();
          resetSelection();
        } else {
          selectionStart = 0;
          selectionEnd = text.length();
          cursor = selectionEnd;
        }
        recalculateBounds();
      } else if (key == GLFW.GLFW_KEY_C && KeyCallback.Modifier.checkModifier(modifiers, KeyCallback.Modifier.CONTROL)) {
        if (selectionStart != selectionEnd) {
          window.input().setClipboard(selection());
        }
      } else if (key == GLFW.GLFW_KEY_V && KeyCallback.Modifier.checkModifier(modifiers, KeyCallback.Modifier.CONTROL)) {
        if (selectionStart != selectionEnd) {
          deleteSelection();
        }
        String clipboard = window.input().getClipboard();
        this.text = textBeforeCursor() + clipboard + textAfterCursor();
        this.cursor += clipboard.length();
        resetSelection();
        recalculateBounds();
      } else if (key == GLFW.GLFW_KEY_X && KeyCallback.Modifier.checkModifier(modifiers, KeyCallback.Modifier.CONTROL)) {
        if (selectionStart != selectionEnd) {
          window.input().setClipboard(selection());
          deleteSelection();
        }
        resetSelection();
        recalculateBounds();
      }
    }
  }

  @Override
  public void handleCharEvent(Window window, int codepoint) {
    if (selectionStart != selectionEnd) {
      deleteSelection();
    }
    this.text = textBeforeCursor() + (char) codepoint + textAfterCursor();
    this.cursor++;
    resetSelection();
    recalculateBounds();
  }

  @Override
  public void handleSelection(SelectionEvent event) {
    this.selected = event.selected();
    cursorAlpha = 0;
    increment = true;
    cursorBox.setAlpha((float) cursorAlpha);
  }

  private void deleteSelection() {
    text = textWithoutSelection();
    if (cursor == selectionEnd) {
      cursor = selectionStart;
    }
    resetSelection();
    recalculateBounds();
  }

  private void resetSelection() {
    selectionStart = cursor;
    selectionEnd = cursor;
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
    recalculateBounds();
  }
}
