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

import love.polardivision.engine.userinterface.Color;
import love.polardivision.engine.userinterface.Font;
import love.polardivision.engine.userinterface.elements.FunctionalElement;
import love.polardivision.engine.userinterface.elements.render.Text;
import love.polardivision.engine.ygwrapper.NodeType;

public class Label extends FunctionalElement {

  protected Label(Font font, String text, Color color, boolean centered) {
    super(NodeType.TEXT);
    setBackground(new Text(font, text, color, centered));
  }

  public Text textElement() {
    return (Text) background();
  }
}
