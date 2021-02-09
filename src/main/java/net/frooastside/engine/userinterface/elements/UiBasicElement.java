package net.frooastside.engine.userinterface.elements;

import net.frooastside.engine.glfw.callbacks.KeyCallback;
import net.frooastside.engine.userinterface.ClickEvent;
import net.frooastside.engine.userinterface.SelectionEvent;

public abstract class UiBasicElement extends UiElement {

  private ClickEvent.Listener clickListener;
  private SelectionEvent.Listener selectionListener;

  public void onKeyEvent(int key, int scancode, KeyCallback.Modifier modifier, KeyCallback.Action buttonState) {}

  public void onCharEvent(int codepoint) {}

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
