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

public class ScrollEvent extends Event {

  private final boolean inside;
  private final float x, y;
  private final float scrollX, scrollY;

  public ScrollEvent(boolean inside, float x, float y, float scrollX, float scrollY) {
    this.inside = inside;
    this.x = x;
    this.y = y;
    this.scrollX = scrollX;
    this.scrollY = scrollY;
  }

  public boolean inside() {
    return inside;
  }

  public float x() {
    return x;
  }

  public float y() {
    return y;
  }

  public float scrollX() {
    return scrollX;
  }

  public float scrollY() {
    return scrollY;
  }

  public interface Handler extends EventHandler {

    @Override
    default void handle(Event event) {
      handleScroll((ScrollEvent) event);
    }

    boolean handleScroll(ScrollEvent event);
  }
}
