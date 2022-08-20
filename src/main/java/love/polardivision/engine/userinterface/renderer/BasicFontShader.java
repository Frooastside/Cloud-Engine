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
import love.polardivision.engine.shader.Shader;
import love.polardivision.engine.shader.ShaderType;
import love.polardivision.engine.shader.uniforms.UniformFloat;
import love.polardivision.engine.shader.uniforms.UniformTexture;
import love.polardivision.engine.shader.uniforms.UniformVector2f;
import love.polardivision.engine.shader.uniforms.UniformVector4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class BasicFontShader extends FontShader {

  private final UniformVector2f uniformOffset = new UniformVector2f("offset");
  private final UniformTexture uniformTexture = new UniformTexture("fontAtlas");
  private final UniformVector4f uniformColor = new UniformVector4f("color");
  private final UniformFloat uniformAlpha = new UniformFloat("alpha");
  private final UniformFloat uniformWidth = new UniformFloat("width");
  private final UniformFloat uniformEdge = new UniformFloat("edge");

  @Override
  protected void addShaders() {
    addShader(Shader.createShader(BasicFontShader.class.getResourceAsStream("basicfontshader/vertexshader.glsl"), ShaderType.VERTEX_SHADER));
    addShader(Shader.createShader(BasicFontShader.class.getResourceAsStream("basicfontshader/fragmentshader.glsl"), ShaderType.FRAGMENT_SHADER));
  }

  @Override
  protected void bindAttributes() {
    bindAttribute(0, "position");
    bindAttribute(1, "textureCoordinates");
  }

  @Override
  protected void storeUniformLocations() {
    storeUniformLocation(uniformOffset);
    storeUniformLocation(uniformTexture);
    storeUniformLocation(uniformColor);
    storeUniformLocation(uniformAlpha);
    storeUniformLocation(uniformWidth);
    storeUniformLocation(uniformEdge);
  }

  @Override
  protected void loadTextureUnits() {
    uniformTexture.loadTextureUnit(0);
    loadWidth(0.5f);
    loadEdge(0.5f);
  }

  public void loadOffset(Vector2f offset) {
    loadOffset(offset.x, offset.y);
  }

  public void loadOffset(float x, float y) {
    uniformOffset.loadVector2f(x, y);
  }

  public void loadTexture(Texture texture) {
    uniformTexture.activeTextureUnit();
    texture.bind();
  }

  public void loadColor(Vector4f color) {
    loadColor(color.x, color.y, color.z, color.w);
  }

  public void loadColor(float r, float g, float b, float a) {
    uniformColor.loadVector4f(r, g, b, a);
  }

  public void loadAlpha(float alpha) {
    uniformAlpha.loadFloat(alpha);
  }

  public void loadWidth(float width) {
    uniformWidth.loadFloat(width);
  }

  public void loadEdge(float edge) {
    uniformEdge.loadFloat(edge);
  }
}
