package net.frooastside.engine.userinterface.elements.basic;

import net.frooastside.engine.glfw.Window;
import net.frooastside.engine.glfw.callbacks.KeyCallback;
import net.frooastside.engine.resource.ResourceFont;
import net.frooastside.engine.userinterface.Constraint;
import net.frooastside.engine.userinterface.ElementConstraints;
import net.frooastside.engine.userinterface.SelectionEvent;
import net.frooastside.engine.userinterface.UiColorSet;
import net.frooastside.engine.userinterface.constraints.PixelConstraint;
import net.frooastside.engine.userinterface.constraints.RawConstraint;
import net.frooastside.engine.userinterface.constraints.RelativeConstraint;
import net.frooastside.engine.userinterface.elements.UiBasicElement;
import net.frooastside.engine.userinterface.elements.UiRenderElement;
import net.frooastside.engine.userinterface.elements.render.UiBox;
import net.frooastside.engine.userinterface.elements.render.UiText;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class UiTextField extends UiBasicElement implements SelectionEvent.Listener {

  private final UiRenderElement[] renderElements = new UiRenderElement[4];

  private final UiColorSet colorSet;
  private final ResourceFont font;
  private final float textSize;
  private final boolean constantTextSize;

  private String text;
  private int cursor;
  private int selectionStart;
  private int selectionEnd;

  private boolean selected;

  private boolean increment;
  private double cursorVisibility;

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
    UiText uiText = ((UiText) renderElements[1]);
    uiText.setText(this.text);

    UiBox selectionBox = ((UiBox) renderElements[2]);
    float selectionStartX = (float) uiText.lineLength(0, selectionStart);
    float selectionEndX = (float) uiText.lineLength(0, selectionEnd);
    selectionBox.constraints().getConstraint(Constraint.ConstraintType.X).setRawValue(selectionStartX);
    selectionBox.constraints().getConstraint(Constraint.ConstraintType.WIDTH).setRawValue(selectionEndX - selectionStartX);
    selectionBox.recalculate(pixelSize());

    UiBox cursorBox = ((UiBox) renderElements[3]);
    float cursorX = (float) uiText.lineLength(0, cursor);
    cursorBox.constraints().getConstraint(Constraint.ConstraintType.X).setRawValue(cursorX - (pixelSize().x * 1));
    cursorBox.recalculate(pixelSize());
  }

  @Override
  public void update(double delta) {
    if(selected) {
      if(increment) {
        if(cursorVisibility >= 1) {
          increment = false;
        }
        cursorVisibility += delta * 3;
      }else {
        if(cursorVisibility <= 0) {
          increment = true;
        }
        cursorVisibility -= delta * 3;
      }
      renderElements[3].setVisibility((float) cursorVisibility);
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

    ElementConstraints selectionBoxConstraints = new ElementConstraints(
      new RawConstraint(0),
      new RelativeConstraint(0.1f),
      new RawConstraint(0),
      new RelativeConstraint(0.8f));
    selectionBoxConstraints.setParent(constraints());
    UiBox selectionBox = new UiBox(colorSet.accent());
    selectionBox.setVisibility(0.2f);
    selectionBox.setConstraints(selectionBoxConstraints);
    renderElements[2] = selectionBox;

    ElementConstraints cursorBoxConstraints = new ElementConstraints(
      new RawConstraint(0),
      new RelativeConstraint(0.1f),
      new PixelConstraint(2),
      new RelativeConstraint(0.8f));
    cursorBoxConstraints.setParent(constraints());
    UiBox cursorBox = new UiBox(colorSet.accent());
    cursorBox.setVisibility(0.0f);
    cursorBox.setConstraints(cursorBoxConstraints);
    renderElements[3] = cursorBox;
  }

  @Override
  public void onKeyEvent(Window window, int key, int scancode, int modifiers, KeyCallback.Action action) {
    if (action != KeyCallback.Action.RELEASE) {
      if (key == GLFW.GLFW_KEY_BACKSPACE) {
        if (selectionStart == selectionEnd) {
          if (cursor > 0 && text.length() >= cursor) {
            text = text.substring(0, cursor - 1) + text.substring(cursor);
            cursor--;
            resetSelection();
            recalculate();
          }
        } else {
          deleteSelection();
          resetSelection();
          recalculate();
        }
      } else if (key == GLFW.GLFW_KEY_DELETE) {
        if (selectionStart == selectionEnd) {
          if (cursor < text.length()) {
            text = text.substring(0, cursor) + text.substring(cursor + 1);
            resetSelection();
            recalculate();
          }
        } else {
          deleteSelection();
          resetSelection();
          recalculate();
        }
      } else if (key == GLFW.GLFW_KEY_LEFT) {
        if (KeyCallback.Modifier.checkModifier(modifiers, KeyCallback.Modifier.SHIFT)) {
          if (cursor == selectionStart) {
            if (cursor > 0 && text.length() >= cursor) {
              selectionStart--;
              cursor = selectionStart;
              recalculate();
            }
          } else if (cursor == selectionEnd) {
            if (cursor > selectionStart) {
              selectionEnd--;
              cursor = selectionEnd;
              recalculate();
            }
          }
        } else {
          if (selectionStart == selectionEnd) {
            if (cursor > 0) {
              cursor--;
            }
          }
          resetSelection();
          recalculate();
        }
      } else if (key == GLFW.GLFW_KEY_RIGHT) {
        if (KeyCallback.Modifier.checkModifier(modifiers, KeyCallback.Modifier.SHIFT)) {
          if (cursor == selectionEnd) {
            if (cursor < text.length()) {
              selectionEnd++;
              cursor = selectionEnd;
              recalculate();
            }
          } else if (cursor == selectionStart) {
            if (selectionStart < selectionEnd) {
              selectionStart++;
              cursor = selectionStart;
              recalculate();
            }
          }
        } else {
          if (selectionStart == selectionEnd) {
            if (cursor < text.length()) {
              cursor++;
            }
          }
          resetSelection();
          recalculate();
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
        recalculate();
      }else if(key == GLFW.GLFW_KEY_C && KeyCallback.Modifier.checkModifier(modifiers, KeyCallback.Modifier.CONTROL)) {
        if (selectionStart != selectionEnd) {
          window.input().setClipboard(selection());
        }
      }else if(key == GLFW.GLFW_KEY_V && KeyCallback.Modifier.checkModifier(modifiers, KeyCallback.Modifier.CONTROL)) {
        if (selectionStart != selectionEnd) {
          deleteSelection();
        }
        String clipboard = window.input().getClipboard();
        this.text = textBeforeCursor() + clipboard + textAfterCursor();
        this.cursor += clipboard.length();
        resetSelection();
        recalculate();
      }else if(key == GLFW.GLFW_KEY_X && KeyCallback.Modifier.checkModifier(modifiers, KeyCallback.Modifier.CONTROL)) {
        if (selectionStart != selectionEnd) {
          window.input().setClipboard(selection());
          deleteSelection();
        }
        resetSelection();
        recalculate();
      }
    }
  }

  @Override
  public void onCharEvent(Window window, int codepoint) {
    if (selectionStart != selectionEnd) {
      deleteSelection();
    }
    this.text = textBeforeCursor() + (char) codepoint + textAfterCursor();
    this.cursor++;
    resetSelection();
    recalculate();
  }

  @Override
  public void onSelection(SelectionEvent event) {
    this.selected = event.selected();
    cursorVisibility = selected ? 0 : 0;
    increment = true;
    renderElements[3].setVisibility((float) cursorVisibility);
  }

  private void deleteSelection() {
    text = textWithoutSelection();
    if (cursor == selectionEnd) {
      cursor = selectionStart;
    }
    resetSelection();
    recalculate();
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

  @Override
  public UiRenderElement[] renderElements() {
    return renderElements;
  }

  public void setText(String text) {
    this.text = text;
    recalculate(pixelSize());
  }
}
