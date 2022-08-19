/*
 * Copyright 2022 @Frooastside
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package love.polardivision.engine.userinterface.elements.basic;

import love.polardivision.engine.userinterface.elements.FunctionalElement;
import love.polardivision.engine.userinterface.events.ClickEvent;
import love.polardivision.engine.window.MouseButton;

public class Button extends FunctionalElement implements ClickEvent.Handler {

  private boolean wasClicked;

  public Button() {
    super(nodeType);
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
      new ClickEvent(event.key(), event.inside(), event.pressed(), event.x(), event.y()).caller(this),
      ClickEvent.Handler.class,
      clickEventTarget());
  }
}
