package love.polardivision.engine.userinterface.elements.basic;

/*
import love.polardivision.engine.userinterface.animation.Animation;
 import love.polardivision.engine.userinterface.elements.FunctionalElement;
 import love.polardivision.engine.userinterface.elements.RenderElement;
 import love.polardivision.engine.userinterface.elements.render.Text;
 import love.polardivision.engine.userinterface.events.ClickEvent;
 import love.polardivision.engine.userinterface.events.Event;
 import love.polardivision.engine.userinterface.events.SelectionEvent;
 import love.polardivision.engine.window.Key;
 import love.polardivision.engine.window.MouseButton;
 import love.polardivision.engine.window.Window;
 import love.polardivision.engine.window.callbacks.KeyCallback;
 import org.joml.Vector2f;
*/

public class TextField /*extends FunctionalElement implements ClickEvent.Handler, SelectionEvent.Handler*/ {

  /*
  private RenderElement background;
   private Text text;
   private final ElementConstraints textConstraints = new ElementConstraints(
     new RelativeConstraint(0f),
     new RelativeConstraint(0f),
     new RelativeConstraint(1f),
     new RelativeConstraint(1f));

   private RenderElement selector;
   private final ElementConstraints selectorConstraints = new ElementConstraints(
     new RawConstraint(0f),
     new RelativeConstraint(0f),
     new RawConstraint(1f),
     new RelativeConstraint(1f));

   private RenderElement cursor;
   private final ElementConstraints cursorConstraints = new ElementConstraints(
     new RawConstraint(0f),
     new RelativeConstraint(0f),
     new RelativeConstraint(1f),
     new RelativeConstraint(1f));
   private Animation cursorOscillation;
   private boolean cursorOscillationReverse;

   private int cursorPosition;
   private int selectionStart;
   private int selectionEnd;
   private boolean selected;

   @Override
   public void calculateChildBounds() {
     if (background != null) {
       background.bounds().set(this.bounds());
     }
     if (text != null) {
       textConstraints.setPixelSize(pixelSize());
       text.bounds().set(
         textConstraints.x().calculate(bounds()) + bounds().x,
         textConstraints.y().calculate(bounds()) + bounds().y + (bounds().w / 2 - textConstraints.w().calculate(bounds()) / 4),
         textConstraints.z().calculate(bounds()),
         textConstraints.w().calculate(bounds()));
       text.recalculate();
       if (selector != null) {
         float selectionStartX = (float) text.lineLength(0, selectionStart);
         float selectionEndX = (float) text.lineLength(0, selectionEnd);
         selectorConstraints.setPixelSize(pixelSize());
         selectorConstraints.x().setValue(selectionStartX);
         selectorConstraints.z().setValue(selectionEndX - selectionStartX);
         selector.bounds().set(
           selectorConstraints.x().calculate(bounds()) + bounds().x,
           selectorConstraints.y().calculate(bounds()) + bounds().y + (bounds().w - selectorConstraints.w().calculate(bounds())) / 2f,
           selectorConstraints.z().calculate(bounds()),
           selectorConstraints.w().calculate(bounds()));
         selector.recalculate();
       }
       if (cursor != null) {
         float cursorX = (float) text.lineLength(0, cursorPosition);
         cursorConstraints.setPixelSize(pixelSize());
         cursorConstraints.x().setValue(cursorX);
         cursor.bounds().set(
           cursorConstraints.x().calculate(bounds()) + bounds().x - (cursorConstraints.z().calculate(bounds()) / 2),
           cursorConstraints.y().calculate(bounds()) + bounds().y + (bounds().w - cursorConstraints.w().calculate(bounds())) / 2f,
           cursorConstraints.z().calculate(bounds()),
           cursorConstraints.w().calculate(bounds()));
         cursor.recalculate();
       }
     }
   }

   @Override
   public void update(double delta) {
     super.update(delta);
     if (selected && cursor != null && cursorOscillation != null && !cursor.animator().doingAnimation(cursorOscillation)) {
       cursorOscillationReverse = !cursorOscillationReverse;
       cursor.animator().applyAnimation(cursorOscillation, cursorOscillationReverse, 0);
     }
   }

   @Override
   public void handleKey(Window window, Key key, int scancode, int modifiers, KeyCallback.Action action) {
     if (action != KeyCallback.Action.RELEASE && selected) {
       if (key == Key.KEY_BACKSPACE) {
         if (selectionStart == selectionEnd) {
           if (cursorPosition > 0 && text.text().length() >= cursorPosition) {
             text.setText(text.text().substring(0, cursorPosition - 1) + text.text().substring(cursorPosition));
             cursorPosition--;
             resetSelection();
             calculateChildBounds();
           }
         } else {
           deleteSelection();
           resetSelection();
           calculateChildBounds();
         }
       } else if (key == Key.KEY_DELETE) {
         if (selectionStart == selectionEnd) {
           if (cursorPosition < text.text().length()) {
             text.setText(text.text().substring(0, cursorPosition) + text.text().substring(cursorPosition + 1));
             resetSelection();
             calculateChildBounds();
           }
         } else {
           deleteSelection();
           resetSelection();
           calculateChildBounds();
         }
       } else if (key == Key.KEY_LEFT) {
         if (KeyCallback.Modifier.checkModifier(modifiers, KeyCallback.Modifier.SHIFT)) {
           if (cursorPosition == selectionStart) {
             if (cursorPosition > 0 && text.text().length() >= cursorPosition) {
               selectionStart--;
               cursorPosition = selectionStart;
               calculateChildBounds();
             }
           } else if (cursorPosition == selectionEnd) {
             if (cursorPosition > selectionStart) {
               selectionEnd--;
               cursorPosition = selectionEnd;
               calculateChildBounds();
             }
           }
         } else {
           if (selectionStart == selectionEnd) {
             if (cursorPosition > 0) {
               cursorPosition--;
             }
           }
           resetSelection();
           calculateChildBounds();
         }
       } else if (key == Key.KEY_RIGHT) {
         if (KeyCallback.Modifier.checkModifier(modifiers, KeyCallback.Modifier.SHIFT)) {
           if (cursorPosition == selectionEnd) {
             if (cursorPosition < text.text().length()) {
               selectionEnd++;
               cursorPosition = selectionEnd;
               calculateChildBounds();
             }
           } else if (cursorPosition == selectionStart) {
             if (selectionStart < selectionEnd) {
               selectionStart++;
               cursorPosition = selectionStart;
               calculateChildBounds();
             }
           }
         } else {
           if (selectionStart == selectionEnd) {
             if (cursorPosition < text.text().length()) {
               cursorPosition++;
             }
           }
           resetSelection();
           calculateChildBounds();
         }
       } else if (key == Key.KEY_A && KeyCallback.Modifier.checkModifier(modifiers, KeyCallback.Modifier.CONTROL)) {
         if (selectionStart == 0 && selectionEnd == text.text().length()) {
           cursorPosition = text.text().length();
           resetSelection();
         } else {
           selectionStart = 0;
           selectionEnd = text.text().length();
           cursorPosition = selectionEnd;
         }
         calculateChildBounds();
       } else if (key == Key.KEY_C && KeyCallback.Modifier.checkModifier(modifiers, KeyCallback.Modifier.CONTROL)) {
         if (selectionStart != selectionEnd) {
           window.input().setClipboard(selection());
         }
       } else if (key == Key.KEY_V && KeyCallback.Modifier.checkModifier(modifiers, KeyCallback.Modifier.CONTROL)) {
         if (selectionStart != selectionEnd) {
           deleteSelection();
         }
         String clipboard = window.input().getClipboard();
         text.setText(textBeforeCursor() + clipboard + textAfterCursor());
         cursorPosition += clipboard.length();
         resetSelection();
         calculateChildBounds();
       } else if (key == Key.KEY_X && KeyCallback.Modifier.checkModifier(modifiers, KeyCallback.Modifier.CONTROL)) {
         if (selectionStart != selectionEnd) {
           window.input().setClipboard(selection());
           deleteSelection();
         }
         resetSelection();
         calculateChildBounds();
       }
     }
   }

   @Override
   public void handleChar(Window window, char codepoint) {
     if (selected) {
       if (selectionStart != selectionEnd) {
         deleteSelection();
       }
       text.setText(textBeforeCursor() + codepoint + textAfterCursor());
       cursorPosition++;
       resetSelection();
       calculateChildBounds();
     }
   }

   @Override
   public void handle(Event event) {
     if (event instanceof ClickEvent) {
       ClickEvent.Handler.super.handle(event);
     } else if (event instanceof SelectionEvent) {
       SelectionEvent.Handler.super.handle(event);
     }
   }

   @Override
   public boolean handleClick(ClickEvent event) {
     if (event.key() == MouseButton.MOUSE_BUTTON_LEFT) {
       int textLength = text.text().length();
       float lineLength = relativeWidth((float) text.lineLength(0, textLength));
       Vector2f relativeClickPosition = relativePixelPosition(event.x(), event.y());
       if (relativeClickPosition.x >= lineLength) {
         cursorPosition = textLength;
       } else {
         cursorPosition = nearestNumberBinarySearch(0, textLength, relativeClickPosition.x);
       }
       if (event.pressed()) {
         resetSelection();
       } else {
         if (selectionStart > cursorPosition) {
           selectionEnd = selectionStart;
           selectionStart = cursorPosition;
         } else {
           selectionEnd = cursorPosition;
         }
       }
       calculateChildBounds();
       return true;
     }
     return false;
   }

   private int nearestNumberBinarySearch(int start, int end, float number) {
     int middle = (start + end) / 2;
     float startNumber = relativeWidth((float) text.lineLength(0, start));
     float middleNumber = relativeWidth((float) text.lineLength(0, middle));
     float endNumber = relativeWidth((float) text.lineLength(0, end));
     if (middleNumber == number) {
       return middle;
     }
     if (start == end - 1) {
       if (Math.abs(endNumber - number) >= Math.abs(startNumber - number)) {
         return start;
       } else {
         return end;
       }
     }
     if (middleNumber > number) {
       return nearestNumberBinarySearch(start, middle, number);
     } else {
       return nearestNumberBinarySearch(middle, end, number);
     }
   }

   private float relativeWidth(float width) {
     return Math.max(Math.min(width / bounds().z, 1), 0);
   }

   @Override
   public void handleSelection(SelectionEvent event) {
     this.selected = event.selected();
     if (cursor != null) {
       cursor.setAlpha(selected ? 1 : 0);
       if (cursorOscillation != null) {
         cursor.animator().cancelAnimation(cursorOscillation);
         cursorOscillationReverse = selected;
       }
     }
   }

   private void deleteSelection() {
     text.setText(textWithoutSelection());
     if (cursorPosition == selectionEnd) {
       cursorPosition = selectionStart;
     }
     resetSelection();
     calculateChildBounds();
   }

   private void resetSelection() {
     selectionStart = cursorPosition;
     selectionEnd = cursorPosition;
   }

   private String textBeforeCursor() {
     return text.text().substring(0, cursorPosition);
   }

   private String textAfterCursor() {
     return text.text().substring(cursorPosition);
   }

   private String textWithoutSelection() {
     return text.text().substring(0, selectionStart) + text.text().substring(selectionEnd);
   }

   private String selection() {
     return text.text().substring(selectionStart, selectionEnd);
   }

   public String value() {
     return text.text();
   }

   public RenderElement background() {
     return background;
   }

   public void setBackground(RenderElement background) {
     if (this.background != null) {
       children().remove(background);
     }
     if (background != null) {
       addElement(background);
     }
     this.background = background;
   }

   public RenderElement cursor() {
     return cursor;
   }

   public void setCursor(RenderElement cursor, Constraint cursorHeight, Constraint cursorThickness) {
     if (this.cursor != null) {
       children().remove(cursor);
     }
     if (cursor != null) {
       addElement(cursor);
       cursor.setAlpha(0.0f);
     }
     if (cursorThickness != null) {
       cursorThickness.setConstraints(cursorConstraints);
       cursorConstraints.setZ(cursorThickness);
     }
     if (cursorHeight != null) {
       cursorHeight.setConstraints(cursorConstraints);
       cursorConstraints.setW(cursorHeight);
     }
     this.cursor = cursor;
   }

   public Animation cursorOscillation() {
     return cursorOscillation;
   }

   public void setCursorOscillation(Animation cursorOscillation) {
     this.cursorOscillation = cursorOscillation;
   }

   public RenderElement selector() {
     return selector;
   }

   public void setSelector(RenderElement selector, Constraint selectorHeight) {
     if (this.selector != null) {
       children().remove(selector);
     }
     if (selector != null) {
       addElement(selector);
     }
     if (selectorHeight != null) {
       selectorHeight.setConstraints(selectorConstraints);
       selectorConstraints.setW(selectorHeight);
     }
     this.selector = selector;
   }

   public Text text() {
     return text;
   }

   public void setText(Text text, Constraint fontSize) {
     if (this.text != null) {
       children().remove(this.text);
     }
     if (text != null) {
       addElement(text);
     }
     if (fontSize != null) {
       fontSize.setConstraints(textConstraints);
       textConstraints.setW(fontSize);
     }
     this.text = text;
   }

   public void setText(String text) {
     if (this.text != null) {
       this.text.setText(text);
       selectionStart = text.length();
       selectionEnd = text.length();
       cursorPosition = text.length();
     }
   }
  */
}
