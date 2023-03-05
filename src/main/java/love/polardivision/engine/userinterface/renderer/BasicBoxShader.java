/*
 * Copyright © 2023 @Frooastside
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package love.polardivision.engine.userinterface.renderer;

import love.polardivision.engine.shader.Shader;
import love.polardivision.engine.shader.ShaderType;
import love.polardivision.engine.shader.uniforms.UniformBoolean;
import love.polardivision.engine.shader.uniforms.UniformFloat;
import love.polardivision.engine.shader.uniforms.UniformTexture;
import love.polardivision.engine.shader.uniforms.UniformVector4f;
import love.polardivision.engine.wrappers.gl.texture.Texture;
import org.joml.Vector4f;

public class BasicBoxShader extends BoxShader {

  private final UniformVector4f uniformTranslation = new UniformVector4f("translation");
  private final UniformFloat uniformAlpha = new UniformFloat("alpha");
  private final UniformBoolean uniformUseColor = new UniformBoolean("useColor");
  private final UniformVector4f uniformColor = new UniformVector4f("color");
  private final UniformBoolean uniformUseTexture = new UniformBoolean("useTexture");
  private final UniformTexture uniformTexture = new UniformTexture("guiTexture");

  @Override
  protected void addShaders() {
    addShader(
        Shader.createShader(
            BasicBoxShader.class.getResourceAsStream("basicboxshader/vertexshader.glsl"),
            ShaderType.VERTEX_SHADER,
            130));
    addShader(
        Shader.createShader(
            BasicBoxShader.class.getResourceAsStream("basicboxshader/fragmentshader.glsl"),
            ShaderType.FRAGMENT_SHADER,
            130));
  }

  @Override
  protected void bindAttributes() {
    bindAttribute(0, "position");
    bindAttribute(1, "textureCoordinates");
  }

  @Override
  protected void storeUniformLocations() {
    storeUniformLocation(uniformTranslation);
    storeUniformLocation(uniformAlpha);
    storeUniformLocation(uniformUseColor);
    storeUniformLocation(uniformColor);
    storeUniformLocation(uniformUseTexture);
    storeUniformLocation(uniformTexture);
  }

  @Override
  protected void loadTextureUnits() {
    uniformTexture.loadTextureUnit(0);
  }

  @Override
  public void loadTranslation(Vector4f translation) {
    loadTranslation(translation.x, translation.y, translation.z, translation.w);
  }

  @Override
  public void loadTranslation(float x, float y, float z, float w) {
    uniformTranslation.loadVector4f(x, y, z, w);
  }

  @Override
  public void loadAlpha(float alpha) {
    uniformAlpha.loadFloat(alpha);
  }

  @Override
  public void loadUseColor(boolean useColor) {
    uniformUseColor.loadBoolean(useColor);
  }

  @Override
  public void loadColor(Vector4f color) {
    loadColor(color.x, color.y, color.z, color.w);
  }

  @Override
  public void loadColor(float r, float g, float b, float a) {
    uniformColor.loadVector4f(r, g, b, a);
  }

  @Override
  public void loadUseTexture(boolean useTexture) {
    uniformUseTexture.loadBoolean(useTexture);
  }

  @Override
  public void loadTexture(Texture texture) {
    uniformTexture.activeTextureUnit();
    texture.bind();
  }
}
