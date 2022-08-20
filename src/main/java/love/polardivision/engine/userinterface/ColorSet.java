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

package love.polardivision.engine.userinterface;

public record ColorSet(Color background, Color baseElement, Color element, Color text, Color accent,
                       Color negativeAccent) {

  public static final ColorSet DARK_MODE = new ColorSet(
    new Color(0.2f, 0.2f, 0.2f),
    new Color(0.24f, 0.24f, 0.24f),
    new Color(0.28f, 0.28f, 0.28f),
    new Color(1.0f, 1.0f, 1.0f),
    new Color(0.1215f, 0.5529f, 0.1215f),
    new Color(0.7f, 0.02f, 0.02f));

}
