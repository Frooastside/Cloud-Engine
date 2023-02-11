/*
 * Copyright © 2022-2023 @Frooastside
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package love.polardivision.engine.userinterface.elements.basic;

import love.polardivision.engine.userinterface.elements.FunctionalElement;
import love.polardivision.engine.userinterface.events.ClickEvent;
import love.polardivision.engine.window.MouseButton;
import love.polardivision.engine.ygwrapper.NodeType;

public class Button extends FunctionalElement implements ClickEvent.Handler {

  private boolean wasClicked;

  public Button() {
    super(NodeType.DEFAULT);
  }

  @Override
  public void update(double delta) {
    super.update(delta);
  }

  @Override
  public boolean handleClick(ClickEvent event) {
    if (event.inside() && event.key() == MouseButton.MOUSE_BUTTON_LEFT) {
      if (event.pressed()) {
        wasClicked = true;
      } else {
        if (wasClicked) {
          wasClicked = false;
          handleInternalClick(event);
        }
      }
    } else {
      wasClicked = false;
    }
    return false;
  }

  public void handleInternalClick(ClickEvent event) {
    emitEvent(
      new ClickEvent(event.key(), event.inside(), event.pressed(), event.x(), event.y()).emitter(this),
      ClickEvent.Handler.class,
      clickEventTarget());
  }
}
