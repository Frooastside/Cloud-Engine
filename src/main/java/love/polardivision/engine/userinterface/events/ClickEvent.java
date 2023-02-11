/*
 * Copyright © 2022-2023 @Frooastside
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package love.polardivision.engine.userinterface.events;

import love.polardivision.engine.window.MouseButton;

public class ClickEvent extends Event {

  private final MouseButton button;
  private final boolean inside;
  private final boolean pressed;
  private final float x, y;

  public ClickEvent(MouseButton button, boolean inside, boolean pressed, float x, float y) {
    this.button = button;
    this.inside = inside;
    this.pressed = pressed;
    this.x = x;
    this.y = y;
  }

  public MouseButton key() {
    return button;
  }

  public boolean inside() {
    return inside;
  }

  public boolean pressed() {
    return pressed;
  }

  public float x() {
    return x;
  }

  public float y() {
    return y;
  }

  public interface Handler extends EventHandler {

    @Override
    default void handle(Event event) {
      handleClick((ClickEvent) event);
    }

    boolean handleClick(ClickEvent event);

  }
}
