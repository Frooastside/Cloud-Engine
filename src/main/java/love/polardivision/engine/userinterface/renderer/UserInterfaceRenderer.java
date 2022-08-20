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

package love.polardivision.engine.userinterface.renderer;

import love.polardivision.engine.userinterface.Screen;
import love.polardivision.engine.utils.NativeObject;
import org.lwjgl.opengl.GL11;

public abstract class UserInterfaceRenderer implements NativeObject {

  protected void clearStencilBuffer() {
    GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
  }

  public abstract void render(Screen screen);

}
