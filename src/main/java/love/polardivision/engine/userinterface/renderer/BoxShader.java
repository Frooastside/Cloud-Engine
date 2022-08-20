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
import org.joml.Vector4f;

public abstract class BoxShader extends ShaderProgram {

  public abstract void loadTranslation(Vector4f translation);

  public abstract void loadTranslation(float x, float y, float z, float w);

  public abstract void loadAlpha(float alpha);

  public abstract void loadUseColor(boolean useColor);

  public abstract void loadColor(Vector4f color);

  public abstract void loadColor(float r, float g, float b, float a);

  public abstract void loadUseTexture(boolean useTexture);

  public abstract void loadTexture(Texture texture);

}
