package net.frooastside.engine.userinterface.elements;

import net.frooastside.engine.glfw.Window;
import net.frooastside.engine.glfw.callbacks.KeyCallback;
import net.frooastside.engine.userinterface.ClickEvent;
import net.frooastside.engine.userinterface.SelectionEvent;
import org.joml.Vector2f;

public abstract class UiBasicElement extends UiElement {

  private ClickEvent.Listener clickListener;
  private SelectionEvent.Listener selectionListener;

  @Override
  public void recalculate(Vector2f pixelSize) {
    super.recalculate(pixelSize);
    for (UiRenderElement renderElement : renderElements()) {
      renderElement.recalculate(pixelSize);
    }
  }

  public void onKeyEvent(Window window, int key, int scancode, int modifiers, KeyCallback.Action action) {
  }

  public void onCharEvent(Window window, int codepoint) {
  }

  public abstract UiRenderElement[] renderElements();

  public boolean clickable() {
    return this instanceof ClickEvent.Listener;
  }

  public boolean selectable() {
    return this instanceof SelectionEvent.Listener;
  }

  public boolean hasClickListener() {
    return clickable() && clickListener != null;
  }

  public boolean hasSelectionListener() {
    return selectable() && selectionListener != null;
  }

  public ClickEvent.Listener clickListener() {
    return clickListener;
  }

  public void setClickListener(ClickEvent.Listener clickListener) {
    this.clickListener = clickListener;
  }

  public SelectionEvent.Listener selectionListener() {
    return selectionListener;
  }

  public void setSelectionListener(SelectionEvent.Listener selectionListener) {
    this.selectionListener = selectionListener;
  }
}
