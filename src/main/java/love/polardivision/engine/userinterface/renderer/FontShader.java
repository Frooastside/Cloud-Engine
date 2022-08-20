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

import love.polardivision.engine.glwrapper.texture.Texture;
import love.polardivision.engine.shader.ShaderProgram;
import org.joml.Vector2f;
import org.joml.Vector4f;

public abstract class FontShader extends ShaderProgram {

  public abstract void loadOffset(Vector2f offset);

  public abstract void loadOffset(float x, float y);

  public abstract void loadTexture(Texture texture);

  public abstract void loadColor(Vector4f color);

  public abstract void loadColor(float r, float g, float b, float a);

  public abstract void loadAlpha(float alpha);

  public abstract void loadWidth(float width);

  public abstract void loadEdge(float edge);
}
