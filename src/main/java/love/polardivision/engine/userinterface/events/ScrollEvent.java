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
