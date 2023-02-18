/*
 * Copyright © 2022-2023 @Frooastside
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package love.polardivision.engine.userinterface.renderer;

import love.polardivision.engine.wrappers.gl.texture.Texture;
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
